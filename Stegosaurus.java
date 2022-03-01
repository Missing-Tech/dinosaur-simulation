/**
 * A simple model of a Stegosaurus.
 * Stegosauruss age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling and Joseph Grabski and Yukesh Shrestha
 * @version 2022.03.01 (3)
 */
public class Stegosaurus extends Prey {
    // Characteristics shared by all Stegosaurus (class variables).

    // The age at which a Stegosaurus can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a Stegosaurus can live.
    private static final int MAX_AGE = 70;
    // The likelihood of a Stegosaurus breeding.
    private static final double BREEDING_PROBABILITY = 0.3;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // Age of this Stegosaurus instance
    protected int age;

    /**
     * Create a new Stegosaurus. A Stegosaurus may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the Stegosaurus will have a random age.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     */
    public Stegosaurus(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location, BREEDING_AGE, MAX_AGE, BREEDING_PROBABILITY, MAX_LITTER_SIZE);
    }

    @Override
    protected Stegosaurus copyThis(Location loc) {
        return new Stegosaurus(false, getField(), loc);
    }

}
