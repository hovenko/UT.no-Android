package no.ut.trip;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Start extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	Button btnOverview = (Button) findViewById(R.id.btn_overview);
	btnOverview.setOnClickListener(new Button.OnClickListener() {
	    public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_SEARCH);
		Uri uri = Uri.parse(WS.ROOT);
		intent.setDataAndType(uri,
			"vnd.android.cursor.dir/vnd.ut.subject");
		startActivity(intent);
	    }
	});

	Button btnTrips = (Button) findViewById(R.id.btn_trips);
	btnTrips.setOnClickListener(new Button.OnClickListener() {
	    public void onClick(View v) {
		startDefaultActivity();
	    }
	});

    }

    private void startDefaultActivity() {
	Intent intent = new Intent(Intent.ACTION_SEARCH);
	Uri uri = Uri.parse(WS.TRIP);
	intent.setDataAndType(uri, "application/xml");
	startActivity(intent);
    }
}