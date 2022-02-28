import java.util.Iterator;
import java.util.List;
import java.util.Random;

public abstract class Prey extends Animal {
    // Characteristics shared by all rabbits (class variables).

    
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static int PLANT_FOOD_VALUE = 20;
    // A shared random number generator to control breeding.
    private static Random rand = Randomizer.getRandom();

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
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

    protected void incrementAge()
    {
        age++;
        if (age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * 
     * @return Where food was found, or null if it wasn't.
     */
    @Override
    protected Location findFood(int SEARCH_RADIUS)
    {
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

    public void checkPredator() {
        if (isAlive()) {
            Field field = getField();
            List<Location> adjacent = field.adjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            while (it.hasNext()) {
                Location where = it.next();
                Object animal = field.getObjectAt(where);
                if (animal instanceof Predator) {
                    Location newLocation = getField().freeAdjacentLocation(getLocation(), true);
                    if (newLocation != null) {
                        setLocation(newLocation);
                    }
                }
            }
        }
    }
}
