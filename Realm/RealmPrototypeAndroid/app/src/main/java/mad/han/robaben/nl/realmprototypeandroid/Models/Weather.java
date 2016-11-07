package mad.han.robaben.nl.realmprototypeandroid.Models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import mad.han.robaben.nl.realmprototypeandroid.RealmListAdapter;

public class Weather extends RealmObject{
    @PrimaryKey
    private int id;
    private String date;
    private String forecast;
    private String humidity;
    private Wind wind;

    private static Realm realm = Realm.getDefaultInstance();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getForecast() {
        return forecast;
    }

    public void setForecast(String forecast) {
        this.forecast = forecast;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }


    /**
     *  Create a new realmObject
     *  This should ofcourse be done async!
     */
    public static void createDummyWeatherObject(final RealmListAdapter adapter){
        final Calendar c = Calendar.getInstance();
        final SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());

        // Asynchronously update objects on a background thread
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                String formattedDate = df.format(c.getTime());

                // Create an object
                Weather weather = bgRealm.createObject(Weather.class);
                Wind wind = bgRealm.createObject(Wind.class);

                // Set the fields
                wind.setDirection("NE");
                wind.setSpeed("100");

                weather.setId(getNextKey());
                weather.setDate(formattedDate);
                weather.setForecast("Very nice weather");
                weather.setHumidity("40%");
                weather.setWind(wind);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction completed
                adapter.notifyDataSetChanged();
            }
        });
    }

    /** Workaround for not having auto increment for key, yet */
    public static int getNextKey()
    {
        return realm.where(Weather.class).max("id").intValue() + 1;
    }
}
