package nl.han.ica.mad.realm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmResults;
import nl.han.ica.mad.realm.model.Weather;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private RealmListAdapter adapter;
    private RealmResults<Weather> weatherItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = (ListView) findViewById(R.id.list);
        final Button writeDataButton = (Button) findViewById(R.id.writeDataBtn);
        writeDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        final Weather newWeather = Weather.create(realm);
                        realm.copyToRealm(newWeather);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        final Button clearDataButton = (Button) findViewById(R.id.clearDataBtn);
        clearDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        final RealmResults<Weather> results = realm.where(Weather.class).findAll();
                        results.deleteAllFromRealm();
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        realm = Realm.getDefaultInstance();
        weatherItems = realm.where(Weather.class).findAll();
        weatherItems.sort("id");
        adapter = new RealmListAdapter(weatherItems, this);
        listView.setAdapter(adapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
