package coen.sqliteonderzoek.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class MySQLiteWeatherHelper extends SQLiteOpenHelper
{
//DATABASE CREATION
    // Database Version
    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "WeatherDB";

    public MySQLiteWeatherHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_WIND_TABLE = "CREATE TABLE "+TABLE_WIND+" ( " +
                WIND_KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WIND_KEY_SPEED+" TEXT, "+
                WIND_KEY_DIRECTION+" TEXT )";

        // SQL statement to create weather table
        String CREATE_WEATHER_TABLE = "CREATE TABLE "+TABLE_WEATHER+" ( " +
                WEATHER_KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WEATHER_KEY_DATE+" TEXT, "+
                WEATHER_KEY_FORECAST+" TEXT, "+
                WEATHER_KEY_HUMIDITY+" TEXT, "+
                WEATHER_KEY_WIND+" INTEGER, " +
                "FOREIGN KEY("+WEATHER_KEY_WIND+") REFERENCES "+TABLE_WIND+ "("+WIND_KEY_ID+") ON UPDATE CASCADE ON DELETE CASCADE )";

        // create tables
        db.execSQL(CREATE_WIND_TABLE);
        db.execSQL(CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WEATHER);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WIND);

        // create new tables
        this.onCreate(db);
    }

//TABLE WEATHER
    private static final String TABLE_WEATHER = "weather";

    private static final String WEATHER_KEY_ID = "id";
    private static final String WEATHER_KEY_DATE = "date";
    private static final String WEATHER_KEY_FORECAST = "forecast";
    private static final String WEATHER_KEY_HUMIDITY = "humidity";
    private static final String WEATHER_KEY_WIND = "wind";

    private static final String[] WEATHER_COLUMNS = {WEATHER_KEY_ID,WEATHER_KEY_DATE,WEATHER_KEY_FORECAST,WEATHER_KEY_HUMIDITY,WEATHER_KEY_WIND};

    public void addWeather(Weather weather){
        //for logging
        Log.d("addWeather", weather.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(WEATHER_KEY_DATE, weather.getDate()); // get title
        values.put(WEATHER_KEY_FORECAST, weather.getForecast()); // get author
        values.put(WEATHER_KEY_HUMIDITY, weather.getHumidity()); // get author
        values.put(WEATHER_KEY_WIND, weather.getWind().getId()); // get author

        // 3. insert
        db.insert(TABLE_WEATHER, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Weather getWeather(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_WEATHER, // a. table
                        WEATHER_COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build weather object
        Weather weather = new Weather();
        weather.setId(Integer.parseInt(cursor.getString(0)));
        weather.setDate(cursor.getString(1));
        weather.setForecast(cursor.getString(2));
        weather.setHumidity(cursor.getString(3));
        weather.setWind(getWind(cursor.getInt(4)));

        //log
        Log.d("getWeather("+id+")", weather.toString());

        return weather;
    }

    public List<Weather> getAllWeather() {
        List<Weather> weathers = new LinkedList<Weather>();

        // 1. build the query
        String query = "SELECT * FROM " + TABLE_WEATHER;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build weather and add it to list
        Weather weather = null;
        if (cursor.moveToFirst()) {
            do {
                weather = new Weather();
                weather.setId(Integer.parseInt(cursor.getString(0)));
                weather.setDate(cursor.getString(1));
                weather.setForecast(cursor.getString(2));
                weather.setHumidity(cursor.getString(3));
                weather.setWind(getWind(cursor.getInt(4)));

                weathers.add(weather);
            } while (cursor.moveToNext());
        }

        Log.d("getAllWeather()", weathers.toString());

        return weathers;
    }

    public int updateWeather(Weather weather) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(WEATHER_KEY_DATE, weather.getDate()); // get title
        values.put(WEATHER_KEY_FORECAST, weather.getForecast()); // get title
        values.put(WEATHER_KEY_HUMIDITY, weather.getHumidity()); // get title
        values.put(WEATHER_KEY_WIND, weather.getWind().getId()); // get title


        // 3. updating row
        int i = db.update(TABLE_WEATHER, //table
                values, // column/value
                WEATHER_KEY_ID+" = ?", // selections
                new String[] { String.valueOf(weather.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    public void deleteWeather(Weather weather) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_WEATHER, //table name
                WEATHER_KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(weather.getId()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteWeather", weather.toString());

    }


//TABLE WIND
    private static final String TABLE_WIND = "wind";

    private static final String WIND_KEY_ID = "id";
    private static final String WIND_KEY_SPEED = "speed";
    private static final String WIND_KEY_DIRECTION = "direction";

    private static final String[] WIND_COLUMNS = {WIND_KEY_ID,WIND_KEY_SPEED,WIND_KEY_DIRECTION};

    public void addWind(Wind wind){
        //for logging
        Log.d("addWind", wind.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(WIND_KEY_SPEED, wind.getSpeed()); // get title
        values.put(WIND_KEY_DIRECTION, wind.getDirection()); // get author

        // 3. insert
        long id = db.insert(TABLE_WIND, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values



        wind.setId(((int) id));

        // 4. close
        db.close();
    }

    public Wind getWind(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
Log.e("-->", id+"");
        // 2. build query
        Cursor cursor =
                db.query(TABLE_WIND, // a. table
                        WIND_COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();



        // 4. build wind object
        Wind wind = new Wind();
        wind.setId(cursor.getInt(0));
        wind.setSpeed(cursor.getString(1));
        wind.setDirection(cursor.getString(2));

        //log
        Log.d("getWind("+id+")", wind.toString());

        return wind;
    }

    public int updateWind(Wind wind) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(WIND_KEY_SPEED, wind.getSpeed()); // get title
        values.put(WIND_KEY_DIRECTION, wind.getDirection()); // get author

        // 3. updating row
        int i = db.update(TABLE_WIND, //table
                values, // column/value
                WIND_KEY_ID+" = ?", // selections
                new String[] { String.valueOf(wind.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    public void deleteWind(Wind wind) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_WIND, //table name
                WIND_KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(wind.getId()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteWind", wind.toString());

    }



}
