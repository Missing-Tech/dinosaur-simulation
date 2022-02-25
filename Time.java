public class Time {

    int seconds;
    int minutes;
    int hours;

    public Time() {
        seconds = 0;
        minutes = 0;
        hours = 0;
    }

    public void incrementTime() {
// seconds = seconds + 48;
    //    if (seconds >= 60) {
      //      seconds = seconds - 60;
            minutes = minutes + 20;
            if (minutes == 60) {
                minutes = 0;
                hours++;
                if (hours == 24) {
                    hours = 0;
                }
            }
        //}
    }

    private String getSecondsInString() {
        if (seconds < 10) {
            return "0" + seconds;
        } else {
            return "" + seconds;
        }
    }

    private String getMinutesInString() {
        if (minutes < 10) {
            return "0" + minutes;
        } else {
            return "" + minutes;
        }
    }

    private String getHoursInString() {
        if (hours < 10) {
            return "0" + hours;
        } else {
            return "" + hours;
        }
    }

    public String getTime() {
        return getHoursInString() + ":" + getMinutesInString() + ":" + getSecondsInString();
    }

    public int getHour() {
        return hours;
    }

    public void resetTime() {
        seconds = 0;
        minutes = 0;
        hours = 0;
    }
}