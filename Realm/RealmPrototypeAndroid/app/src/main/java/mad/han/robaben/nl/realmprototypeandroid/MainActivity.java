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

        realm = Realm.getDefaultInstance();

        RealmResults<Weather> weatherItems = realm.where(Weather.class).findAll();
        weatherItems.sort("id");

        adapter = new RealmListAdapter(weatherItems, this);
        mListView.setAdapter(adapter);
        setupClickListeners();
    }

    private void setupClickListeners(){
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
        writeToDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Weather.createDummyWeatherObject(adapter);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
