import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class TRex extends Predator {
    // Characteristics shared by all foxes (class variables).

    // The age at which a fox can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a fox can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.2;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int PREY_FOOD_VALUE = 30;

    protected static final int SEARCH_RADIUS = 2;

    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     */
    public TRex(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location, BREEDING_AGE, MAX_AGE, BREEDING_PROBABILITY, MAX_LITTER_SIZE,
                PREY_FOOD_VALUE);
    }

    @Override
    protected Location findFood(int SEARCH_RADIUS) {
        Field field = getField();
        List<Location> adjacent = findNearbyLocations(SEARCH_RADIUS);

        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if (animal instanceof Stegosaurus || animal instanceof Brontosaurus) {
                Prey prey = (Prey) animal;
                if (prey.isAlive()) {
                    prey.setDead();
                    super.foodLevel = PREY_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    @Override
    protected TRex copyThis(Location loc) {
        return new TRex(false, getField(), loc);
    }

}
