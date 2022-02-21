import java.util.List;
import java.util.Random;

public class Tree extends Plant {
   // Characteristics shared by all rabbits (class variables).
    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 20;
    // The age to which a fox can live.
    private static final int MAX_AGE = 20;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.01;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // A shared random number generator to control breeding.
    private static Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The rabbit's age.
    private int age;

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Tree(boolean randomAge, Field field, Location location)
    {
        super(field, location, BREEDING_AGE, MAX_AGE, BREEDING_PROBABILITY, MAX_LITTER_SIZE);

        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        } else {
            age = 0;
        }
    }

    @Override
    protected Plant copyThis(Location loc) {
        return new Tree(false, getField(), loc);
    }

}
