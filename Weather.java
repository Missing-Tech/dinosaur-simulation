import java.util.Random;

/**
 * Global Weather class to handle what weather it is
 * 
 * @author Joseph Grabski and Yukesh Shrestha
 * @version 2022.03.01 
 */
public class Weather {
    private static Weather INSTANCE;

    private static Random rand = Randomizer.getRandom();

    private static final double FOG_PROBABILITY = 0.95;

    private static final double RAIN_PROBABILITY = 0.85;

    private static final double CLEAR_PROBABILITY = 0.7;

    public WeatherType weather;

    public Weather() {
        weather = WeatherType.CLEAR;
    }

    /**
     * Singleton pattern
     * @return Returns the global instance of this object
     */
    public static Weather getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Weather();
        }
        
        return INSTANCE;
    }

    /**
     * Chooses weather randomly
     * @param step Current step of the program
     */
    public void chooseWeather(int step) {
        // Changes the weather every 10 steps
        if (step % 10 == 0) {
            double randomNumber = rand.nextDouble();
            if (randomNumber <= CLEAR_PROBABILITY) {
                weather = WeatherType.CLEAR;
            } else if (randomNumber <= RAIN_PROBABILITY) {
                weather = WeatherType.RAIN;
            } else if (randomNumber <= FOG_PROBABILITY) {
                weather = WeatherType.FOG;
            } else {
                weather = WeatherType.HEATWAVE;
            }
        }
    }

    public WeatherType getWeather() {
        return weather;
    }

}