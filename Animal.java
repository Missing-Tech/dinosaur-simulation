
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal {
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;

    private boolean isMale;

    private Random rand = Randomizer.getRandom();

    // The age at which a fox can start to breed.
    protected static int BREEDING_AGE;
    // The age to which a fox can live.
    protected static int MAX_AGE;
    // The likelihood of a fox breeding.
    protected static double BREEDING_PROBABILITY;
    // The maximum number of births.
    protected static int MAX_LITTER_SIZE;

    private static final double INFECTED_PROBABILITY = 0.05;
    private static final double INFECTIVITY = 0.2;
    private static final double FATALITY_CHANCE = 0.01;
    private static final int DAYS_TILL_IMMUNE = 10;

    protected int age;
    protected int foodLevel;

    private boolean isInfected = false;
    private boolean isImmune = false;
    private int daysInfected = 0;

    /**
     * Create a new animal at location in field.
     * 
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location) {
        alive = true;
        this.field = field;
        setLocation(location);
        decideGender();

        if(rand.nextDouble() <= INFECTED_PROBABILITY){
            isInfected = true;
        }
    }

    private void decideGender() {
        isMale = rand.nextBoolean();
    }

    public void infect(){
        isInfected = true;
    }

    public boolean isInfected(){
        return isInfected;
    }

    public boolean isImmune(){
        return isImmune;
    }

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * 
     * @param newAnimals A list to receive newly born animals.
     */
    public void act(List<Animal> newAnimals, int SEARCH_RADIUS) {
        handleInfection();
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            // Move towards a source of food if found.
            Location newLocation = null;
            boolean foundMate = false;

            // If food is abundant then look for a mate, otherwise find food
            if (foodLevel > 15) {
                newLocation = findMate(1);
                // If a mate is found, then try and give birth
                if (newLocation != null) {
                    foundMate = true;
                    giveBirth(newAnimals);
                }
            }

            if (newLocation == null) {
                newLocation = findFood(SEARCH_RADIUS);   
            }

            // If there's neither food or a mate, then look for a free location to move to
            if (newLocation == null) {
                newLocation = getField().freeAdjacentLocation(getLocation(), true);
            }

            // See if it was possible to move.
            if (newLocation != null) {
                // Don't move if this animal has found a mate
                if(!foundMate){
                    setLocation(newLocation);
                }
            } else {
                // Overcrowding.
                setDead();
            }

        }
    };

    protected abstract Location findFood(int SEARCH_RADIUS);

    /**
     * Increase the age. This could result in the fox's death.
     */
    protected abstract void incrementAge();

    /**
     * Check whether or not this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * 
     * @param newAnimals A list to return newly born foxes.
     */
    protected void giveBirth(List<Animal> newAnimals) {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation(), true);
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Animal young = copyThis(loc);
            newAnimals.add(young);
        }
    }

    private void handleInfection(){
        if(isInfected && isAlive()){
            Animal animalToInfect = attemptToInfect();
            if(animalToInfect != null && rand.nextDouble() <= INFECTIVITY){
                animalToInfect.infect();
            }
            daysInfected++;
            if(daysInfected >= DAYS_TILL_IMMUNE){
                isInfected = false;
                isImmune = true;
            }
            if(rand.nextDouble() <= FATALITY_CHANCE){
                setDead();
            }
        }
    }

    protected List<Location> findNearbyLocations(int searchRadius){
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());

        Set<Location> nearbyLocations = new HashSet<>();
        // Increase search radius by one
        for (int i = 0; i < searchRadius; i++) {
            for (Location adjacentLocation : adjacent) {
                nearbyLocations.addAll(field.adjacentLocations(adjacentLocation));
            }
        }
        
        adjacent.addAll(nearbyLocations);

        return adjacent;
    }

    /**
     * Provides a function for a subclass to create a copy of itself
     */
    protected abstract Animal copyThis(Location loc);

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * 
     * @return The number of births (may be zero).
     */
    private int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A fox can breed if it has reached the breeding age.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    protected void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Check whether the animal is alive or not.
     * 
     * @return true if the animal is still alive.
     */
    protected boolean isAlive() {
        return alive;
    }

    public boolean isOppositeGender(boolean gender) {
        return gender ^ isMale;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead() {
        alive = false;
        if (location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * 
     * @return The animal's location.
     */
    protected Location getLocation() {
        return location;
    }

    protected Location findMate(int searchRadius) {
        List<Location> adjacent = findNearbyLocations(searchRadius);

        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Object object = field.getObjectAt(where);
            Animal animal;
            if (object instanceof Animal) {
                animal = (Animal) object;
                if (animal.getClass().equals(this.getClass()) && animal.isOppositeGender(isMale)) {
                    if (animal.isAlive()) {
                        return where;
                    }
                }
            }
        }
        return null;
    }

    protected Animal attemptToInfect() {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());

        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Object object = field.getObjectAt(where);
            Animal animal;
            if (object instanceof Animal) {
                animal = (Animal) object;
                // Disease only spreads between the same species
                if (animal.getClass().equals(this.getClass()) && animal.isAlive() && !animal.isImmune()) {
                    return animal;
                }
            }
        }
        return null;
    }

    /**
     * Place the animal at the new location in the given field.
     * 
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation) {
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the animal's field.
     * 
     * @return The animal's field.
     */
    protected Field getField() {
        return field;
    }
}
