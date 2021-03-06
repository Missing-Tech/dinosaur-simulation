import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Prey class containing shared prey behaviour
 * 
 * @author David J. Barnes and Michael Kölling and Joseph Grabski and Yukesh Shrestha
 * @version 2022.03.01 
 */
public abstract class Prey extends Animal {
    // Characteristics shared by all Preys (class variables).

    // The food value of a single Prey. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static int PLANT_FOOD_VALUE = 20;
    // A shared random number generator to control breeding.
    private static Random rand = Randomizer.getRandom();

    /**
     * Create a new Prey. A Prey may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the Prey will have a random age.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     */
    public Prey(boolean randomAge, Field field, Location location, int breedingAge, int maxAge, double breedingProb,
            int maxLitterSize) {
        super(field, location);

        BREEDING_AGE = breedingAge;
        MAX_AGE = maxAge;
        BREEDING_PROBABILITY = breedingProb;
        MAX_LITTER_SIZE = maxLitterSize;

        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(PLANT_FOOD_VALUE);
        } else {
            age = 0;
            foodLevel = PLANT_FOOD_VALUE;
        }
    }

    @Override
    protected abstract Prey copyThis(Location loc);

    /**
     * Look for Preys adjacent to the current location.
     * Only the first live Prey is eaten.
     * 
     * @return Where food was found, or null if it wasn't.
     */
    @Override
    protected Location findFood() {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Object food = field.getObjectAt(where);
            if (food instanceof Plant) {
                Plant plant = (Plant) food;
                if (plant.isAlive()) {
                    plant.setDead();
                    super.foodLevel = PLANT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Checks the nearby cells for predators
     * @return Returns true if a predator is nearby
     */
    public boolean checkForPredators() {
        if (isAlive()) {
            Field field = getField();
            List<Location> adjacent = field.adjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            while (it.hasNext()) {
                Location where = it.next();
                Object animal = field.getObjectAt(where);
                if (animal instanceof Predator) {
                    return true;
                }
            }
        }
        return false;
    }
}
