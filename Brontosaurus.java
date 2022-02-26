import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Brontosaurus extends Prey
{
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 35;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 80;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.07;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;


    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Brontosaurus(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location, BREEDING_AGE, MAX_AGE, BREEDING_PROBABILITY, MAX_LITTER_SIZE);
    }
    
    @Override
    protected Brontosaurus copyThis(Location loc){
        return new Brontosaurus(false, getField(), loc);
    }
    
}
