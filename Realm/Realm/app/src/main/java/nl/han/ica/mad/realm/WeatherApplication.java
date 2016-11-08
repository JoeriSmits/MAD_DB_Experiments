package nl.han.ica.mad.realm;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 8-11-2016
 */

public class WeatherApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
