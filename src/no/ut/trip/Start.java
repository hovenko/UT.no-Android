package no.ut.trip;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Start extends Activity {
    static public final String WS_UT_TRIP_LISTINGS = "http://ut.no/solrsearch/ws/search_facet?subject=trip";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnTrips = (Button) findViewById(R.id.btn_trips);
        btnTrips.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startDefaultActivity();
            }
        });

    }

    private void startDefaultActivity() {
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        Uri uri = Uri.parse(WS_UT_TRIP_LISTINGS);
        intent.setDataAndType(uri, "application/xml");
        startActivity(intent);
    }
}