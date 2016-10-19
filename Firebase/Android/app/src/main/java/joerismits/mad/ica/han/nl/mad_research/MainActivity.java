package joerismits.mad.ica.han.nl.mad_research;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import joerismits.mad.ica.han.nl.mad_research.Models.Weather;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Write data button
        final Button writeBtn = (Button) findViewById(R.id.writeDataBtn);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteDatabase writeDatabase = new WriteDatabase(MainActivity.this);
                writeDatabase.execute();
            }
        });

        // Read real-time data
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("weatherForecast");

        final ArrayList<String> weatherArrayList = new ArrayList<String>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                weatherArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    weatherArrayList.add(snapshot.getValue(Weather.class).forecast);
                }

                ListView listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        weatherArrayList
                ));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MAIN", String.valueOf(databaseError));
            }
        });
    }
}
