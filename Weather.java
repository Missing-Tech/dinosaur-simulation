import java.util.Random;

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

    public static Weather getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Weather();
        }
        
        return INSTANCE;
    }

    public void chooseWeather(int step) {
        if (step % 3 == 0) {
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