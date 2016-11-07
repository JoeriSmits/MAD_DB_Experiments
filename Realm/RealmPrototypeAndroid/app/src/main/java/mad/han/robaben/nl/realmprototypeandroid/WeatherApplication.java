package mad.han.robaben.nl.realmprototypeandroid;


import android.app.Application;

import io.realm.Realm;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 7-11-2016
 */

public class WeatherApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
