package joerismits.mad.ica.han.nl.mad_research;

import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import joerismits.mad.ica.han.nl.mad_research.Models.Weather;

/**
 * Created by JoeriSmits on 19-10-16.
 */
public class ReadDatabase extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("weatherForecast");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Weather weatherForecast = snapshot.getValue(Weather.class);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true;
    }
}
