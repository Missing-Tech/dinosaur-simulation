import java.util.Random;

public abstract class Predator extends Animal{

// Characteristics shared by all foxes (class variables).
    
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static int PREY_FOOD_VALUE;
    // A shared random number generator to control breeding.
    private Random rand = Randomizer.getRandom();

    public Predator(boolean randomAge, Field field, Location location, int breedingAge, int maxAge, double breedingProb, int maxLitterSize, int preyFoodValue) {
        super(field, location);

        BREEDING_AGE = breedingAge;
        MAX_AGE = maxAge;
        BREEDING_PROBABILITY = breedingProb;
        MAX_LITTER_SIZE = maxLitterSize;
        PREY_FOOD_VALUE = preyFoodValue;

        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(PREY_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = PREY_FOOD_VALUE;
        }
    }
    
    
    /**
     * Look for prey adjacent to the current location.
     * Only the first live prey is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    @Override
    protected abstract Location findFood(int SEARCH_RADIUS);
    
    protected void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Provides a function for a subclass to create a copy of itself
     */
    @Override
    protected abstract Predator copyThis(Location loc);

    
}