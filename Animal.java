import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling and Joseph Grabski and Yukesh
 *         Shrestha
 * @version 2022.03.01 (3)
 */
public abstract class Animal {

    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    // A random number generator
    private Random rand = Randomizer.getRandom();

    // The age at which a animal can start to breed.
    protected static int BREEDING_AGE;
    // The age to which a animal can live.
    protected static int MAX_AGE;
    // The likelihood of a animal breeding.
    protected static double BREEDING_PROBABILITY;
    // The maximum number of births.
    protected static int MAX_LITTER_SIZE;

    // Probability to start off infected
    private static final double INFECTED_PROBABILITY = 0.05;
    // Probability to spread the infection to other animals nearby
    private static final double INFECTIVITY = 0.2;
    // Probability to die from the infection
    private static final double FATALITY_PROBABILITY = 0.01;
    // Days until the animal is cured of the infection
    private static final int DAYS_TILL_IMMUNE = 10;

    // Static reference to Time singleton
    private static Time timer;

    // Age of this animal
    protected int age;
    // Whether the animal is alive or not.
    private boolean alive;
    // The hunger level of the animal
    protected int foodLevel;
    // Flag for if the animal is male
    private boolean isMale;

    // Flag for if the animal is infected or not
    private boolean isInfected = false;
    // Flag for if the animal is immune to catching the infection
    private boolean isImmune = false;
    // Tracks the number of days the animal has had the infection
    private int daysInfected = 0;

    /**
     * Create a new animal at location in field.
     * 
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location) {
        alive = true;
        this.field = field;
        setLocation(location);
        decideGender();

        timer = Time.getInstance();

        if (rand.nextDouble() <= INFECTED_PROBABILITY) {
            isInfected = true;
        }
    }

    /**
     * Decide the gender of this animal
     */
    private void decideGender() {
        isMale = rand.nextBoolean();
    }

    /**
     * Public function to infect this animal
     */
    public void infect() {
        isInfected = true;
    }

    /**
     * @return The infection status
     */
    public boolean isInfected() {
        return isInfected;
    }

    /**
     * @return The immunity status
     */
    public boolean isImmune() {
        return isImmune;
    }

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * 
     * @param newAnimals A list to receive newly born animals.
     */
    public void act(List<Animal> newAnimals) {
        incrementAge();
        handleInfection();
        
        // If it's night time then don't act (unless this is a predator)
        if (timer.isNight() && !(this instanceof Predator))
            // For Prey at night, check if there are any nearby predators
            if (this instanceof Prey) {
                Prey prey = (Prey) this;
                // If there are no nearby predators then sleep
                if (!prey.checkForPredators())
                    return;
            }

        incrementHunger();
        if (isAlive()) {
            // Move towards a source of food if found.
            Location newLocation = null;
            boolean foundMate = false;

            // If food is abundant then look for a mate, otherwise find food
            if (foodLevel > 15) {
                newLocation = findMate(1);
                // If a mate is found, then try and give birth
                if (newLocation != null) {
                    foundMate = true;
                    giveBirth(newAnimals);
                }
            }

            // If a mate isn't found, or food is not abundant, then find food
            if (newLocation == null) {
                newLocation = findFood();
            }

            // If there's neither food or a mate, then look for a free location to move to
            if (newLocation == null) {
                newLocation = getField().freeAdjacentLocation(getLocation(), true);
            }

            // See if it was possible to move.
            if (newLocation != null) {
                // Don't move if this animal has found a mate
                if (!foundMate) {
                    setLocation(newLocation);
                }
            } else {
                // Overcrowding.
                setDead();
            }

        }
    };

    /**
     * Function to search food
     * @return Returns a location of nearby food to move to
     */
    protected abstract Location findFood();

    /**
     * Increase the age. This could result in the animal's death.
     */
    protected void incrementAge(){
        age++;
        if (age > MAX_AGE) {
            setDead();
        }
    };

    /**
     * Check whether or not this animal is to give birth at this step.
     * New births will be made into free adjacent locations.
     * 
     * @param newAnimals A list to return newly born animales.
     */
    protected void giveBirth(List<Animal> newAnimals) {
        // New animales are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation(), true);
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Animal young = copyThis(loc);
            newAnimals.add(young);
        }
    }

    /**
     * Handles logic regarding infection, and the spread of it
     */
    private void handleInfection() {
        // If this animal is infected and alive
        if (isInfected && isAlive()) {
            // Increment the number of days infected
            daysInfected++;

            // Attempt to find a nearby animal to infect
            Animal animalToInfect = attemptToInfect();
            if (animalToInfect != null && rand.nextDouble() <= INFECTIVITY) {
                // If one is found, then there's a chance it gets infected
                animalToInfect.infect();
            }
            // If the animal has been infected for long enough, then make it immune
            if (daysInfected >= DAYS_TILL_IMMUNE) {
                isInfected = false;
                isImmune = true;
            }

            // Chance of death
            if (rand.nextDouble() <= FATALITY_PROBABILITY) {
                setDead();
            }
        }
    }

    /**
     * Look for nearby locations to the animal
     * @param searchRadius Radius around the animal to look for locations
     * @return Returns a list of locations
     */
    protected List<Location> findNearbyLocations(int searchRadius) {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());

        // Uses a set so there's no duplicate locations
        Set<Location> nearbyLocations = new HashSet<>();
        // Increase search radius by one
        for (int i = 0; i < searchRadius; i++) {
            for (Location adjacentLocation : adjacent) {
                nearbyLocations.addAll(field.adjacentLocations(adjacentLocation));
            }
        }

        adjacent.addAll(nearbyLocations);

        return adjacent;
    }

    /**
     * Provides a function for a subclass to create a copy of itself
     */
    protected abstract Animal copyThis(Location loc);

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * 
     * @return The number of births (may be zero).
     */
    private int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * An animal can breed if it has reached the breeding age.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    /**
     * Make this animal more hungry. This could result in the animal's death.
     */
    protected void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Check whether the animal is alive or not.
     * 
     * @return true if the animal is still alive.
     */
    protected boolean isAlive() {
        return alive;
    }

    /**
     * @param gender Gender of the other animal
     * @return Returns whether the genders are different
     */
    public boolean isOppositeGender(boolean gender) {
        // XOR on both animals genders to get whether they're different
        return gender ^ isMale;
    }

    /**
     * Indicate that the animal is no longer alive.
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
     * Return the animal's location.
     * 
     * @return The animal's location.
     */
    protected Location getLocation() {
        return location;
    }

    /**
     * Looks for a valid mate in a radius around the animal
     * @param searchRadius Radius to search around the animal
     * @return Returns the location of a nearby mate
     */
    protected Location findMate(int searchRadius) {
        // Get all nearby locations in a radius
        List<Location> adjacent = findNearbyLocations(searchRadius);

        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Object object = field.getObjectAt(where);
            Animal animal;
            if (object instanceof Animal) {
                animal = (Animal) object;
                // If the animals are the same species, and opposite genders then they're a valid mate
                if (animal.getClass().equals(this.getClass()) && animal.isOppositeGender(isMale)) {
                    if (animal.isAlive()) {
                        return where;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Attempts to infect nearby animals
     * @return Returns an animal to infect
     */
    protected Animal attemptToInfect() {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());

        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Object object = field.getObjectAt(where);
            Animal animal;
            if (object instanceof Animal) {
                animal = (Animal) object;
                // Disease only spreads between the same species
                if (animal.getClass().equals(this.getClass()) && animal.isAlive() && !animal.isImmune()) {
                    return animal;
                }
            }
        }
        return null;
    }

    /**
     * Place the animal at the new location in the given field.
     * 
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation) {
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the animal's field.
     * 
     * @return The animal's field.
     */
    protected Field getField() {
        return field;
    }
}
