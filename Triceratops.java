
/**
 * A simple model of a Triceratops.
 * Triceratops' age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling and Joseph Grabski and Yukesh Shrestha
 * @version 2022.03.01 
 */
public class Triceratops extends Prey {
    // Characteristics shared by all Triceratops' (class variables).

    // The age at which a Triceratops can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a Triceratops can live.
    private static final int MAX_AGE = 60;
    // The likelihood of a Triceratops breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;

    /**
     * Create a new Triceratops. A Triceratops may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the Triceratops will have a random age.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     */
    public Triceratops(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location, BREEDING_AGE, MAX_AGE, BREEDING_PROBABILITY, MAX_LITTER_SIZE);
    }

    @Override
    protected Triceratops copyThis(Location loc) {
        return new Triceratops(false, getField(), loc);
    }
}
