package joerismits.mad.ica.han.nl.mad_research.Models;

/**
 * Created by JoeriSmits on 12-10-16.
 */
public class Weather {
    public String date;
    public String forecast;
    public String humidity;
    public Wind wind;

    public Weather(String date, String forecast, String humidity, Wind wind) {
        this.date = date;
        this.forecast = forecast;
        this.humidity = humidity;
        this.wind = wind;
    }

    public Weather() {
    }
}
