import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of plants.
 * 
 * @author David J. Barnes and Michael Kölling and Joseph Grabski and Yukesh Shrestha
 * @version 2022.03.01 (3)
 */
public abstract class Plant {
    // Whether the plant is alive or not.
    private boolean alive;
    protected int age;

    // The age at which a plant can start to breed.
    private static int BREEDING_AGE;
    // The age to which a plant can live.
    private static int MAX_AGE;
    // The likelihood of a plant breeding.
    private static double BREEDING_PROBABILITY;
    // The maximum number of births.
    private static int MAX_LITTER_SIZE;

    private Random rand = Randomizer.getRandom();

    // The plant's field.
    private Field field;
    // The plant's position in the field.
    private Location location;

    private Weather weather;

    /**
     * Create a new plant at location in field.
     * 
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public Plant(Field field, Location location, int breedingAge, int maxAge, double breedingProb, int maxLitterSize) {
        alive = true;
        this.field = field;

        BREEDING_AGE = breedingAge;
        MAX_AGE = maxAge;
        BREEDING_PROBABILITY = breedingProb;
        MAX_LITTER_SIZE = maxLitterSize;

        weather = Weather.getInstance();

        age = 0;
        setLocation(location);
    }

    public void grow(List<Plant> newPlants) {
        incrementAge();
        if (isAlive()) {
            giveBirth(newPlants);
        }
    }

    /**
     * Increase the age. This could result in the plant's death.
     */
    private void incrementAge() {
        age++;
        if (age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Check whether or not this plant is to give birth at this step.
     * New births will be made into free adjacent locations.
     * 
     * @param newPlants A list to return newly born plants.
     */
    private void giveBirth(List<Plant> newPlants) {
        // New plants are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation(), false);
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Plant young = copyThis(loc);
            newPlants.add(young);
        }
    }

    /**
     * Provides a function for a subclass to create a copy of itself
     */
    protected abstract Plant copyThis(Location loc);

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * 
     * @return The number of births (may be zero).
     */
    private int breed() {
        BREEDING_PROBABILITY = 0.15;

        if (weather.getWeather().equals(WeatherType.RAIN)) {
            BREEDING_PROBABILITY *= 1.3;
        } else if (weather.getWeather().equals(WeatherType.HEATWAVE)) {
            BREEDING_PROBABILITY *= 0.5;
        }

        int births = 0;

        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A plant can breed if it has reached the breeding age.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    /**
     * Check whether the plant is alive or not.
     * 
     * @return true if the plant is still alive.
     */
    protected boolean isAlive() {
        return alive;
    }

    /**
     * Indicate that the plant is no longer alive.
     * It is removed from the field.
     */
    protected void setDead() {
        alive = false;
        if (location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the plant's location.
     * 
     * @return The plant's location.
     */
    protected Location getLocation() {
        return location;
    }

    /**
     * Place the plant at the new location in the given field.
     * 
     * @param newLocation The plant's new location.
     */
    protected void setLocation(Location newLocation) {
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the plant's field.
     * 
     * @return The plant's field.
     */
    protected Field getField() {
        return field;
    }
}
