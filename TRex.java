import java.util.Iterator;
import java.util.List;

/**
 * A simple model of a TRex.
 * TRexs age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling and Joseph Grabski and Yukesh Shrestha
 * @version 2022.03.01 
 */
public class TRex extends Predator {

    // The age at which a TRex can start to breed.
    private static final int BREEDING_AGE = 20;
    // The age to which a TRex can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a TRex breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single prey. In effect, this is the
    // number of steps a TRex can go before it has to eat again.
    private static final int PREY_FOOD_VALUE = 25; 
    // Radius around the TRex to look for food
    protected static final int SEARCH_RADIUS = 2;
    // Global weather instance
    private static Weather weather;

    /**
     * Create a TRex. A TRex can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the TRex will have random age and hunger level.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     */
    public TRex(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location, BREEDING_AGE, MAX_AGE, BREEDING_PROBABILITY, MAX_LITTER_SIZE,
                PREY_FOOD_VALUE);
        weather = Weather.getInstance();
    }

    /**
     * Function to search for food in the surrounding area
     * @param SEARCH_RADIUS Radius around the animal to look for food
     * @return Returns the location of nearby food
     */
    @Override
    protected Location findFood(int SEARCH_RADIUS) {
        int searchRadius = weather.getWeather().equals(WeatherType.FOG) ? 1 : SEARCH_RADIUS;

        Field field = getField();
        List<Location> adjacent = findNearbyLocations(searchRadius);

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
