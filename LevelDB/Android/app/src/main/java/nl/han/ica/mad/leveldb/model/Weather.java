package nl.han.ica.mad.leveldb.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 30-10-2016
 */

public class Weather {

    public String id;
    public String date;
    public String forecast;
    public String humidity;
    public Wind wind;

    public Weather(String id, String date, String forecast, String humidity, Wind wind) {
        this.id = id;
        this.date = date;
        this.forecast = forecast;
        this.humidity = humidity;
        this.wind = wind;
    }

    public Map<byte[], byte[]> getSerialized() {
        final HashMap<byte[], byte[]> keyValueMap = new HashMap<>();
        keyValueMap.put((id + "-date").getBytes(), date.getBytes());
        keyValueMap.put((id + "-forecast").getBytes(), forecast.getBytes());
        keyValueMap.put((id + "-humidity").getBytes(), humidity.getBytes());
        keyValueMap.put((id + "-wind-direction").getBytes(), wind.direction.getBytes());
        keyValueMap.put((id + "-wind-speed").getBytes(), wind.speed.getBytes());
        return keyValueMap;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", id, date, forecast);
    }
}
