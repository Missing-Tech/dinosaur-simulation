import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing Predator and Prey.
 * 
 * @author David J. Barnes and Michael Kölling and Joseph Grabski and Yukesh Shrestha
 * @version 2022.03.01 
 */

public class Simulator {
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 200;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 100;
    // The probability that a fox will be created in any given grid position.
    private static final double PREDATOR_CREATION_PROBABILITY = 0.02;
    // The probability that a rabbit will be created in any given grid position.
    private static final double PREY_CREATION_PROBABILITY = 0.1;

    private static final double TREX_CREATION_PROBABILITY = 0.5;
    private static final double BRONTOSAURUS_CREATION_PROBABILITY = 0.2;
    private static final double TRICERATOPS_CREATION_PROBABILITY = 0.3;
    private static final double PLANT_CREATION_PROBABILITY = 0.3;

    // List of animals in the field.
    private List<Animal> animals;
    // List of animals in the field.
    private List<Plant> plants;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    // The timer for the simulation
    private static Time timer;

    private static Weather weather;

    private static final Color lightGreen = new Color(211, 255, 79);

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    public static void main(String args[]) // static method
    {
        Simulator simulator = new Simulator();
        simulator.runLongSimulation();
    }

    /**
     * Create a simulation field with the given size.
     * 
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        animals = new ArrayList<>();
        plants = new ArrayList<>();
        field = new Field(depth, width);
        weather = Weather.getInstance();
        timer = Time.getInstance();

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Stegosaurus.class, Color.BLUE);
        view.setColor(Triceratops.class, Color.ORANGE);
        view.setColor(Brontosaurus.class, Color.CYAN);
        view.setColor(Velociraptor.class, Color.MAGENTA);
        view.setColor(TRex.class, Color.RED);
        view.setColor(Grass.class, lightGreen);

        // Setup a valid starting point.
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation() {
        simulate(1000);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * 
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            delay(60); // uncomment this to run more slowly
        }
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep() {

        step++;
        weather.chooseWeather(step);
        timer.incrementTime();

        // Provide space for newborn animals.
        List<Animal> newAnimals = new ArrayList<>();

        for (Iterator<Animal> it = animals.iterator(); it.hasNext();) {
            Animal animal = it.next();
            animal.act(newAnimals);
        }

        // Add the newly born animals to the main lists.
        animals.addAll(newAnimals);
        // Provide space for newborn animals.
        List<Plant> newPlants = new ArrayList<>();
        // Let all rabbits act.
        for (Iterator<Plant> it = plants.iterator(); it.hasNext();) {
            Plant plant = it.next();
            plant.grow(newPlants);
            if (!plant.isAlive()) {
                it.remove();
            }
        }

        // Add the newly born plants to the main lists.
        plants.addAll(newPlants);

        view.showStatus(step, field);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        animals.clear();
        timer.resetTime();
        populate();

        // Show the starting state in the view.
        view.showStatus(step, field);
    }

    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate() {
        spawnPlants();
        spawnAnimals();
    }

    private void spawnPlants() {
        Random rand = Randomizer.getRandom();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (rand.nextDouble() <= PLANT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    PlantFactory plantFactory = new PlantFactory();

                    Plants plantType = Plants.GRASS;

                    Plant plant = plantFactory.getPlant(plantType, field, location);
                    plants.add(plant);
                }
                // else leave the location empty.
            }
        }
    }

    private void spawnAnimals() {
        Random rand = Randomizer.getRandom();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {

                if (rand.nextDouble() <= PREDATOR_CREATION_PROBABILITY) {
                    PredatorFactory predatorFactory = new PredatorFactory();
                    Location location = new Location(row, col);

                    Animals predatorType;

                    if (rand.nextDouble() <= TREX_CREATION_PROBABILITY) {
                        predatorType = Animals.TREX;
                    } else {
                        predatorType = Animals.VELOCIRAPTOR;
                    }

                    Predator predator = predatorFactory.getPredator(predatorType, field, location);
                    animals.add(predator);
                } else if (rand.nextDouble() <= PREY_CREATION_PROBABILITY) {
                    PreyFactory preyFactory = new PreyFactory();
                    Location location = new Location(row, col);

                    Animals preyType;

                    if (rand.nextDouble() <= BRONTOSAURUS_CREATION_PROBABILITY) {
                        preyType = Animals.BRONTOSAURUS;
                    } else if (rand.nextDouble() <= TRICERATOPS_CREATION_PROBABILITY) {
                        preyType = Animals.TRICERATOPS;
                    } else {
                        preyType = Animals.STEGOSAURUS;
                    }

                    Prey prey = preyFactory.getPrey(preyType, field, location);
                    animals.add(prey);
                }
            }
            // else leave the location empty.
        }
    }

    /**
     * Pause for a given time.
     * 
     * @param millisec The time to pause for, in milliseconds
     */
    private void delay(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException ie) {
            // wake up
        }
    }
}
