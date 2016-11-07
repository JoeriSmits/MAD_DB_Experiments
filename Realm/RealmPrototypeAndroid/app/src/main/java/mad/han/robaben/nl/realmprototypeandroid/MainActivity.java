package mad.han.robaben.nl.realmprototypeandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import mad.han.robaben.nl.realmprototypeandroid.Models.Weather;

public class MainActivity extends AppCompatActivity {
    private Button writeToDBButton;
    private ListView mListView;
    private RealmListAdapter adapter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list_view);
        writeToDBButton = (Button) findViewById(R.id.writeDataBtn);

        Realm.init(this);

        /**
         * When u change a model you have to update the schemaVersion!
         * Be sure to also make a new migration schema in the migration helper :)
         */
        RealmConfiguration config = new RealmConfiguration.Builder()
                .build();

        /**
         * We can now use Realm.getDefaultInstance in other classes
         */
        Realm.setDefaultConfiguration(config);

        /**
         * Get the Realm instance with above config
         * It is also possible to have more configs (thus more realms)
         * Then use getInstance(configName)
         */
        realm = Realm.getDefaultInstance();

        // Retrieve current items from realm and bind to the adapter
        RealmResults<Weather> weatherItems = realm.where(Weather.class).findAll();

        // Sorting is real easy :D
        weatherItems.sort("id");

        adapter = new RealmListAdapter(weatherItems, this);

        mListView.setAdapter(adapter);

        setupClickListeners();
    }

    private void setupClickListeners(){
        // Remove item on long click item
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int id = adapter.getItem(i).getId();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Weather.class).equalTo("id", id).findAll().deleteAllFromRealm();
                    }
                });
                return true;
            }
        });

        // Create item on button click
        writeToDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Weather.createDummyWeatherObject(adapter);
            }
        });
    }

    // Don't forgot to close the realm when your done!
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
