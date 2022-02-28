import java.util.Random;

public class Weather {
    private static Random rand = Randomizer.getRandom();

    private static final double FOG_PROBABILITY = 0.95;

    private static final double RAIN_PROBABILITY = 0.9;

    private static final double NORMAL_PROBABILITY = 0.8;

    public String weather;

    public Weather() {
        weather = "NORMAL";
    }

    public void chooseWeather() {
        double randomNumber = rand.nextDouble();
        if (randomNumber <= NORMAL_PROBABILITY) {
            weather = "NORMAL";
        } else if (randomNumber <= RAIN_PROBABILITY) {
            weather = "RAIN";
        } else if (randomNumber <= FOG_PROBABILITY) {
            weather = "FOG";
        } else {
            weather = "HEATWAVE";
        }
    }

    public String getWeather() {
        return weather;
    }

}