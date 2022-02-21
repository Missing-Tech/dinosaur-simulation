
import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Plant
{
    // Whether the animal is alive or not.
    private boolean alive;
    private int growthStage = 0;
    private int growthPeriod;
    protected int age;

    // The age at which a fox can start to breed.
    private static int BREEDING_AGE;
    // The age to which a fox can live.
    private static int MAX_AGE;
    // The likelihood of a fox breeding.
    private static double BREEDING_PROBABILITY;
    // The maximum number of births.
    private static int MAX_LITTER_SIZE;

    private Random rand = Randomizer.getRandom();

    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plant(Field field, Location location, int breedingAge, int maxAge, double breedingProb, int maxLitterSize)
    {
        alive = true;
        this.field = field;

        BREEDING_AGE = breedingAge;
        MAX_AGE = maxAge;
        BREEDING_PROBABILITY = breedingProb;
        MAX_LITTER_SIZE = maxLitterSize;
        
        age = 0;
        setLocation(location);
    }

    public void grow(List<Plant> newPlants){
        // if((age+1 % growthPeriod) == 0){
        //     growthStage++;
        // }
        incrementAge();
        if(isAlive()) {
            giveBirth(newPlants);            
            // Move towards a source of food if found.
            Location newLocation = getField().freeAdjacentLocation(getLocation());

            if(newLocation == null) {
                // Overcrowding.
                setDead();
            }
        }
    }

/**
     * Increase the age. This could result in the fox's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Check whether or not this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newPlants A list to return newly born foxes.
     */
    private void giveBirth(List<Plant> newPlants)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
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
     * @return The number of births (may be zero).
     */

    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A fox can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    protected int getGrowthStage()
    {
        return growthStage;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }
}
