package joerismits.mad.ica.han.nl.mad_research;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import joerismits.mad.ica.han.nl.mad_research.Models.Weather;
import joerismits.mad.ica.han.nl.mad_research.Models.Wind;

/**
 * Created by JoeriSmits on 12-10-16.
 */
public class WriteDatabase extends AsyncTask {

    WeakReference<Activity> weakReference;

    public WriteDatabase(Activity activity) {
        weakReference = new WeakReference<Activity>(activity);
    }

    @Override
    protected Boolean doInBackground(Object[] params) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("weatherForecast");

        ArrayList<Weather> weatherArrayList = new ArrayList<>();
        Weather weather = new Weather("2016-10-26", "Sunny", "80%", new Wind("280", "10 knots"));
        weatherArrayList.add(weather);

        myRef.setValue(weatherArrayList);
        return true;
    }

    @Override
    protected void onPostExecute(Object o) {
        Activity activity = weakReference.get();
        Button writeBtn = (Button) activity.findViewById(R.id.writeDataBtn);
        writeBtn.getBackground().setColorFilter(Color.parseColor("#006400"), PorterDuff.Mode.DARKEN);
        writeBtn.setTextColor(0xFFFFFFFF);
    }
}
