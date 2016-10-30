package nl.han.ica.mad.leveldb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.github.hf.leveldb.exception.LevelDBException;

import java.util.ArrayList;
import java.util.List;

import nl.han.ica.mad.leveldb.model.Weather;
import nl.han.ica.mad.leveldb.model.Wind;

public class MainActivity extends AppCompatActivity {

    private List<String> weatherArrayList = new ArrayList<>();
    private ArrayAdapter<String> weatherArrayAdapter;

    private Weather[] testData = new Weather[]{
            new Weather("nijmegen", "30-10-2016", "mistig", "vochtig", new Wind("NE", "13")),
            new Weather("oss", "27-06-2016", "zonnig", "droog", new Wind("NW", "3")),
            new Weather("amsterdam", "03-11-2016", "bewolkt", "regen", new Wind("SW", "7")),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set listview adapter
        ListView listView = (ListView) findViewById(R.id.list);
        weatherArrayAdapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                weatherArrayList
        );
        listView.setAdapter(weatherArrayAdapter);

        //Set button listener
        final Button writeDataButton = (Button) findViewById(R.id.writeDataBtn);
        writeDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeData();
            }
        });
        //Read data
        readData();
    }

    public void readData() {
        final LevelDBHandler handler = new LevelDBHandler(this);
        weatherArrayList.clear();
        try {
            final List<String> weatherIds = handler.getAllWeatherIds();
            for (String weatherId : weatherIds) {
                weatherArrayList.add(handler.get(weatherId).toString());
            }
        } catch (LevelDBException e) {
            Log.e("LevelDBRead", e.getMessage());
        }
        weatherArrayAdapter.notifyDataSetChanged();
    }

    private void writeData() {
        final LevelDBHandler handler = new LevelDBHandler(this);
        for (Weather weather : testData) {
            try {
                handler.put(weather);
            } catch (LevelDBException e) {
                Log.e("LevelDBWrite", e.getMessage());
            }
        }
    }
}
