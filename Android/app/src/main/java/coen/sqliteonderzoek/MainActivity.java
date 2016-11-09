package coen.sqliteonderzoek;

import android.app.Activity;
import android.os.Bundle;

import java.util.List;

import coen.sqliteonderzoek.model.MySQLiteWeatherHelper;
import coen.sqliteonderzoek.model.Weather;
import coen.sqliteonderzoek.model.Wind;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MySQLiteWeatherHelper db = new MySQLiteWeatherHelper(this);

        /**
         * CRUD Operations
         * */
        // add entries

        Wind wind1 = new Wind("North", "4");
        Wind wind2 = new Wind("South", "5");

        db.addWind(wind1);
        db.addWind(wind2);
        db.addWeather(new Weather("8-11-2016", "Sunny", "20%", wind1));
        db.addWeather(new Weather("9-11-2016", "Cloudy", "15%", wind2));




        // get all books
        List<Weather> list = db.getAllWeather();

        // delete one book
        db.deleteWeather(list.get(0));

        // get all books
        db.getAllWeather();

    }

}