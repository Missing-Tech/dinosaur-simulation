import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class TRex extends Predator
{
    // Characteristics shared by all foxes (class variables).
    
    // The age at which a fox can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a fox can live.
    private static final int MAX_AGE = 100;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int PREY_FOOD_VALUE = 10;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public TRex(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location, BREEDING_AGE, MAX_AGE, BREEDING_PROBABILITY, MAX_LITTER_SIZE, PREY_FOOD_VALUE);
    }
    
    @Override
    protected Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            //TODO: Implement being able to eat any type of prey
            if(animal instanceof Triceratops || animal instanceof Stegosaurus) {
                Prey prey = (Prey) animal;
                if(prey.isAlive()) { 
                    prey.setDead();
                    super.foodLevel = PREY_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    protected TRex copyThis(Location loc){
        return new TRex(false, getField(), loc);
    }

}
