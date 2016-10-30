package nl.han.ica.mad.leveldb.model;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 30-10-2016
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
}
