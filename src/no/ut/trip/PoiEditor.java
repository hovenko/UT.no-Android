package no.ut.trip;

import java.io.File;
import java.io.IOException;

import no.ut.trip.content.Pois;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class PoiEditor extends Activity {

    private static final String[] PROJECTION = new String[] { Pois._ID,
	    Pois.NAME, Pois.TYPE, Pois.DESCRIPTION, Pois.ARRIVAL, Pois.GEO };

    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_TYPE = 2;
    private static final int COLUMN_DESCRIPTION = 3;
    private static final int COLUMN_ARRIVAL = 4;
    private static final int COLUMN_GEO = 5;

    public static final int MENU_ITEM_DELETE = Menu.FIRST;

    static final int DIALOG_PROGRESS = 0;

    private static final int ACTIVITY_MAP_PICK = 1;
    private static final int ACTIVITY_IMAGE_CAPTURE = 2;

    static final String TAG = "PoiCreate";

    private static final int STATE_EDIT = 0;
    private static final int STATE_INSERT = 1;

    ProgressDialog progressDialog;

    EditText txtName;
    Spinner spinnerType;
    EditText txtGeoPoisition;
    EditText txtDescription;
    EditText txtArrival;

    Button btnGeoLookup;
    Button btnMap;
    Button btnImageCapture;

    private int state;
    private Uri uri;
    private Cursor cursor;

    PoiEntity poiEntity = new PoiEntity();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	final Intent intent = getIntent();
	final String action = intent.getAction();
	if (Intent.ACTION_EDIT.equals(action)) {
	    state = STATE_EDIT;
	    uri = intent.getData();
	} else if (Intent.ACTION_INSERT.equals(action)) {
	    state = STATE_INSERT;
	    uri = getContentResolver().insert(intent.getData(), null);

	    if (uri == null) {
		Log.e(TAG, "Failed to insert new POI into "
			+ getIntent().getData());
		finish();
		return;
	    }

	    setResult(RESULT_OK, (new Intent()).setData(uri));
	} else {
	    Log.e(TAG, "Unknown action, exiting");
	    finish();
	    return;
	}

	setContentView(R.layout.poi_editor);

	txtName = (EditText) findViewById(R.id.txtName);
	spinnerType = (Spinner) findViewById(R.id.spinnerType);
	txtDescription = (EditText) findViewById(R.id.txtDescription);
	txtArrival = (EditText) findViewById(R.id.txtArrival);
	txtGeoPoisition = (EditText) findViewById(R.id.txtGeoPosition);

	// btnGeoLookup = (Button) findViewById(R.id.btnGeoLocate);
	// btnGeoLookup.setOnClickListener(new OnClickListener() {
	// public void onClick(View v) {
	// LocationServiceManager locationServiceManager = new
	// LocationServiceManager(
	// handler);
	// locationServiceManager.run();
	//
	// btnGeoLookup.setClickable(false);
	// showProgressDialog();
	// }
	// });

	btnMap = (Button) findViewById(R.id.btnMap);
	btnMap.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		String geoString = txtGeoPoisition.getText().toString();
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setData(Uri.parse("geo:" + geoString));
		startActivityForResult(intent, ACTIVITY_MAP_PICK);
	    }
	});

	btnImageCapture = (Button) findViewById(R.id.btnImageCapture);
	btnImageCapture.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		File f;
		try {
		    f = File.createTempFile("UTno-poi-", ".jpg");
		} catch (IOException e) {
		    Log.e(TAG, "Failed to create tmp file", e);
		    Toast.makeText(PoiEditor.this, "Failed to create tmp file",
			    Toast.LENGTH_LONG).show();
		    return;
		}

		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, ACTIVITY_IMAGE_CAPTURE);

		// File imageDirectory = new File("/sdcard/signifio");
		// String path = imageDirectory.toString().toLowerCase();
		// String name = imageDirectory.getName().toLowerCase();
		//
		//
		// ContentValues values = new ContentValues();
		// values.put(Media.TITLE, "Image");
		// values.put(Images.Media.BUCKET_ID, path.hashCode());
		// values.put(Images.Media.BUCKET_DISPLAY_NAME,name);
		//
		// values.put(Images.Media.MIME_TYPE, "image/jpeg");
		// values.put(Media.DESCRIPTION, "Image capture by camera");
		// values.put("_data", "/sdcard/signifio/1111.jpg");
		// uri = getContentResolver().insert( Media.EXTERNAL_CONTENT_URI
		// , values);
		// Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
		//
		// i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		//
		// startActivityForResult(i, 0);
	    }
	});

	cursor = managedQuery(uri, PROJECTION, null, null, null);
    }

    @Override
    protected void onResume() {
	super.onResume();

	if (cursor != null) {
	    Log.d(TAG, "Refreshing view from database cursor");
	    cursor.moveToFirst();

	    if (state == STATE_EDIT) {
		setTitle(getText(R.string.title_edit_poi));
	    } else if (state == STATE_INSERT) {
		setTitle(getText(R.string.title_create_poi));
	    }

	    String name = cursor.getString(COLUMN_NAME);
	    String type = cursor.getString(COLUMN_TYPE);
	    String description = cursor.getString(COLUMN_DESCRIPTION);
	    String arrival = cursor.getString(COLUMN_ARRIVAL);
	    String geo = cursor.getString(COLUMN_GEO);

	    txtName.setText(name);
	    txtDescription.setTextKeepState(description);
	    txtArrival.setTextKeepState(arrival);
	    txtGeoPoisition.setText(geo);
	}
    }

    @Override
    protected void onPause() {
	super.onPause();

	if (cursor != null) {
	    String name = txtName.getText().toString();
	    if ("".equals(name)) {
		Resources r = Resources.getSystem();
		name = r.getString(android.R.string.untitled);
	    }

	    ContentValues values = new ContentValues();
	    values.put(Pois.MODIFIED_DATE, System.currentTimeMillis());

	    values.put(Pois.NAME, name);
	    values.put(Pois.DESCRIPTION, txtDescription.getText().toString());
	    values.put(Pois.ARRIVAL, txtArrival.getText().toString());
	    values.put(Pois.GEO, txtGeoPoisition.getText().toString());

	    getContentResolver().update(uri, values, null, null);
	}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
	    Intent result) {
	switch (requestCode) {
	case ACTIVITY_MAP_PICK:
	    if (result != null) {
		Uri data = result.getData();
		GeoPointVO p = GeoPointVO.fromUri(data);
		String geoString = p.getLatitude() + "," + p.getLongitude();

		Log.d(TAG, "Got new geo position from PICK activity: "
			+ geoString);
		txtGeoPoisition.setText(geoString);

		if (cursor != null) {
		    ContentValues values = new ContentValues();
		    values.put(Pois.MODIFIED_DATE, System.currentTimeMillis());
		    values.put(Pois.GEO, geoString);

		    getContentResolver().update(uri, values, null, null);
		}
	    }

	    return;
	}

	super.onActivityResult(requestCode, resultCode, result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);

	menu.add(0, MENU_ITEM_DELETE, 0, R.string.menu_delete).setIcon(
		android.R.drawable.ic_menu_delete);

	Intent intent = new Intent(null, getIntent().getData());
	intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
	menu
		.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
			new ComponentName(this, PoiEditor.class), null, intent,
			0, null);

	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case MENU_ITEM_DELETE:
	    Uri poiUri = getIntent().getData();
	    getContentResolver().delete(poiUri, null, null);
	    setResult(RESULT_CANCELED);
	    finish();
	    return true;
	}

	return super.onOptionsItemSelected(item);
    }

    public void showProgressDialog() {
	showDialog(DIALOG_PROGRESS);
    }

    protected Dialog onCreateDialog(int id) {
	switch (id) {
	case DIALOG_PROGRESS:
	    progressDialog = new ProgressDialog(PoiEditor.this);
	    progressDialog.setMessage(getText(R.string.process_loading_data));
	    progressDialog.setIndeterminate(true);
	    return progressDialog;

	default:
	    return null;
	}
    }

    /**
     * Define the Handler that receives messages from the thread and update the
     * progress
     */
    final Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    boolean complete = msg.getData().getBoolean("complete");
	    if (complete) {
		dismissDialog(DIALOG_PROGRESS);

	    }
	}
    };

    // /**
    // * Nested class that finds the current location using the Location
    // Service.
    // */
    // private class LocationServiceManager {
    // Handler mHandler;
    // final LocationManager locationManager;
    // String bestProvider;
    //
    // LocationServiceManager(Handler h) {
    // locationManager = (LocationManager)
    // getSystemService(Context.LOCATION_SERVICE);
    //
    // Criteria criteria = new Criteria();
    // bestProvider = locationManager.getBestProvider(criteria, true);
    //
    // mHandler = h;
    // }
    //
    // public void run() {
    // Log.d(TAG, "Starting Location Service thread");
    //
    // txtGeoPoisition.setText("");
    // txtGeoPoisition.setHint("Søker GPS-posisjon...");
    //
    // try {
    // if (bestProvider != null) {
    // Log.d(TAG, "Provider found: " + bestProvider);
    // Location lastKnownLocation = locationManager
    // .getLastKnownLocation(bestProvider);
    // if (null != lastKnownLocation) {
    // double longitude = lastKnownLocation.getLongitude();
    // double latitude = lastKnownLocation.getLatitude();
    // Log.d(TAG, "Location manager last known location: "
    // + latitude + "," + longitude);
    // } else {
    // Log.e(TAG, "No last known location");
    // }
    // } else {
    // Log.w(TAG, "No provider found. strange...");
    // }
    //
    // LocationListener locationListener = new LocationListener() {
    // public void onStatusChanged(String provider, int status,
    // Bundle extras) {
    // }
    //
    // public void onProviderEnabled(String provider) {
    // }
    //
    // public void onProviderDisabled(String provider) {
    // }
    //
    // public void onLocationChanged(Location location) {
    // double longitude = location.getLongitude();
    // double latitude = location.getLatitude();
    //
    // txtGeoPoisition.setText(latitude + ", " + longitude);
    //
    // Log.d(TAG, "Location manager on location changed: "
    // + latitude + "," + longitude);
    //
    // locationManager.removeUpdates(this);
    // btnGeoLookup.setClickable(true);
    // }
    // };
    //
    // locationManager.requestLocationUpdates(bestProvider, 0, 0f,
    // locationListener);
    //
    // } catch (Exception e) {
    // Log.e(TAG, "Failed to get location", e);
    // new ExceptionHandler(PoiEditor.this, "Failed to get location",
    // e).setRethrow(true).show();
    // return;
    // }
    //
    // Message msg = mHandler.obtainMessage();
    // Bundle b = new Bundle();
    // b.putBoolean("complete", true);
    // msg.setData(b);
    // mHandler.sendMessage(msg);
    // }
    // }

}