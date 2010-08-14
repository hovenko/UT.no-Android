package no.ut.trip;

import no.ut.trip.content.Pois;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Start extends Activity {

    private static final String TAG = "Start";

    private static final int ACTIVITY_MAP_PICK = 1;
    private static final int ACTIVITY_TAKE_PICTURE = 2;

    /**
     * Called when the activity is first created.
     */
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

	Button btnMyPois = (Button) findViewById(R.id.btn_my_pois);
	btnMyPois.setOnClickListener(new Button.OnClickListener() {
	    public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setData(Pois.CONTENT_URI);
		startActivity(intent);
	    }
	});

	Button btnMap = (Button) findViewById(R.id.btn_map);
	btnMap.setOnClickListener(new Button.OnClickListener() {
	    public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setData(Uri.parse("geo:60.5,10.123"));
		startActivityForResult(intent, ACTIVITY_MAP_PICK);
	    }
	});

	Button btnMapMarkers = (Button) findViewById(R.id.btn_map_markers);
	btnMapMarkers.setOnClickListener(new Button.OnClickListener() {
	    public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("geo:60.5,10.123"));
		intent.setType("vnd.android.cursor.dir/vnd.ut.map.marker");
		startActivity(intent);
	    }
	});

	Button btnTrips = (Button) findViewById(R.id.btn_trips);
	btnTrips.setOnClickListener(new Button.OnClickListener() {
	    public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_SEARCH);
		Uri uri = Uri.parse(WS.TRIP);
		intent.setDataAndType(uri,
			"vnd.android.cursor.dir/vnd.ut.listings");
		startActivity(intent);
	    }
	});

	Button btnTakePicture = (Button) findViewById(R.id.btn_take_picture);
	btnTakePicture.setOnClickListener(new Button.OnClickListener() {
	    public void onClick(View v) {
		// Intent intent = new Intent(Intent.ACTION_PICK);
		// intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// startActivityForResult(intent, ACTIVITY_TAKE_PICTURE);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
			MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, ACTIVITY_TAKE_PICTURE);

		// File f;
		// try {
		// f = File.createTempFile("UTno-start", "jpg");
		// Intent intent = new Intent(
		// "android.media.action.IMAGE_CAPTURE");
		// intent.putExtra(MediaStore.EXTRA_OUTPUT,
		// Uri.fromFile(f));
		// intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		// startActivityForResult(intent, ACTIVITY_TAKE_PICTURE);
		// } catch (IOException e) {
		// Log.e(TAG, "Failed to create tmp file", e);
		// }

	    }
	});

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
	    Intent result) {
	switch (requestCode) {
	case ACTIVITY_MAP_PICK:
	    if (result != null) {
		Uri data = result.getData();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Result");
		builder.setMessage("" + data);
		AlertDialog alert = builder.create();
		alert.show();
	    }

	    return;

	case ACTIVITY_TAKE_PICTURE:
	    if (result == null) {
		Log.v(TAG, "Camera NO RETURNED INTENT");
		return;
	    } else {
		Uri data = result.getData();

		Log
			.v(TAG, "Camera I GOT SOMETHING BACK!!!! "
				+ data
				+ " : "
				+ result.getExtras().getString(
					MediaStore.EXTRA_OUTPUT));
		return;
	    }
	}

	super.onActivityResult(requestCode, resultCode, result);
    }
}