package nl.han.ica.mad.leveldb;

import android.content.Context;

import com.github.hf.leveldb.Iterator;
import com.github.hf.leveldb.LevelDB;
import com.github.hf.leveldb.exception.LevelDBException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.han.ica.mad.leveldb.model.Weather;
import nl.han.ica.mad.leveldb.model.Wind;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 30-10-2016
 */

public class LevelDBHandler {

    private Context context;

    public LevelDBHandler(Context context) {
        this.context = context;
    }
    public List<String> getAllWeatherIds() throws LevelDBException {
        final LevelDB currentInstance = getInstance();
        List<String> knownIds = new ArrayList<>();
        Iterator iterator = currentInstance.iterator();
        for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            String currentKeyId = new String(iterator.key()).split("-")[0];
            if (!knownIds.contains(currentKeyId)) {
                knownIds.add(currentKeyId);
            }
        }
        currentInstance.close();
        return knownIds;
    }

    public Weather get(String weatherId) throws LevelDBException {
        final LevelDB currentInstance = getInstance();
        byte[] dateKey = getKey(weatherId, "date");
        byte[] forecastKey = getKey(weatherId, "forecast");
        byte[] humidityKey = getKey(weatherId, "humidity");
        byte[] windDirectionKey = getKey(weatherId, "wind-direction");
        byte[] windSpeedKey = getKey(weatherId, "wind-speed");
        String date = new String(currentInstance.get(dateKey));
        String forecast = new String(currentInstance.get(forecastKey));
        String humidity = new String(currentInstance.get(humidityKey));
        String windDirection = new String(currentInstance.get(windDirectionKey));
        String windSpeed = new String(currentInstance.get(windSpeedKey));
        currentInstance.close();

        Wind wind = new Wind(windDirection, windSpeed);
        return new Weather(weatherId, date, forecast, humidity, wind);
    }

    public void put(Weather weather) throws LevelDBException {
        final LevelDB currentInstance = getInstance();
        for (Map.Entry<byte[], byte[]> keyValues : weather.getSerialized().entrySet()) {
            currentInstance.put(keyValues.getKey(), keyValues.getValue());
        }
        currentInstance.close();
    }

    private byte[] getKey(String weatherId, String keyName) {
        return (weatherId + "-" + keyName).getBytes();
    }

    private LevelDB getInstance() throws LevelDBException {
        String path = context.getCacheDir().getPath();
        return LevelDB.open(context.getCacheDir().getPath() + "/leveldb.db", LevelDB.configure().createIfMissing(true));
    }

}
