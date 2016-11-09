package coen.sqliteonderzoek.model;

public class Weather
{
    private int id;
    private String date;
    private String forecast;
    private String humidity;
    private Wind wind;

    public Weather(){}

    public Weather(String date, String forecast, String humidity, Wind wind) {
        super();
        this.date = date;
        this.forecast = forecast;
        this.humidity = humidity;
        this.wind = wind;
    }

    //getters & setters

    @Override
    public String toString() {
        return "Wind [id=" + id + ", direction=" + date + ", forecast=" + forecast + ", humidity=" + humidity +", with wind: "+wind.toString()+"]";
    }


    public String getDate() {
        return date;
    }

    public String getForecast() {
        return forecast;
    }

    public String getHumidity() {
        return humidity;
    }

    public Wind getWind() {
        return wind;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setForecast(String forecast) {
        this.forecast = forecast;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public int getId() {
        return id;
    }
}
