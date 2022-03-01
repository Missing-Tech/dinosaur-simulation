/**
 * Simple model of a digital clock
 * Clock starts at 0 (midnight) and increases by 20 minutes every step.
 * 
 * @author Joseph Grabski and Yukesh Shrestha
 */

public class Time {
    private static Time INSTANCE;

    // The components needed to display the digital clock.
    int seconds;
    int minutes;
    int hours;

    public static Time getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Time();
        }
        
        return INSTANCE;
    }

    public Time() {
        // Seconds, Minutes and Hours set to 0 at the start.
        seconds = 0;
        minutes = 0;
        hours = 0;
    }

    // The time is incremented by 20 minutes each step
    public void incrementTime() {
        minutes = minutes + 30;
        if (minutes == 60) { // when the value stored in the minutes variable reaches 60, it refreshes to 0,
                             // signifying a new minute
            minutes = 0;
            hours++; // when new minute starts, hour increased by 1
            if (hours == 24) {
                hours = 0; // when value stored in the hours variable reaches 24, the value is set to 0,
                           // signifying a new day
            }
        }
    }

    /**
     *
     * @return The seconds in String format
     */
    private String getSecondsInString() {
        if (seconds < 10) {
            return "0" + seconds;
        } else {
            return "" + seconds;
        }
    }

    /**
     * @return The minutes in String format
     */
    private String getMinutesInString() {
        if (minutes < 10) {
            return "0" + minutes;
        } else {
            return "" + minutes;
        }
    }

    /**
     * @return The hours in String format
     */
    private String getHoursInString() {
        if (hours < 10) {
            return "0" + hours;
        } else {
            return "" + hours;
        }
    }

    /**
     * Concatenates the Seconds, Minutes and Hours together in a digital clock
     * format
     * 
     * @return The time in a digital clock format
     */
    public String getTime() {
        return getHoursInString() + ":" + getMinutesInString() + ":" + getSecondsInString();
    }

    /**
     * @return The current hour
     */
    public int getHour() {
        return hours;
    }

    /**
     * Resets the time (seconds, minutes, hours) to 0
     */
    public void resetTime() {
        seconds = 0;
        minutes = 0;
        hours = 0;
    }

    /**
     * @return Whether it's currently night time
     */
    public boolean isNight(){
        return (hours < 6 || hours > 22);
    }
}