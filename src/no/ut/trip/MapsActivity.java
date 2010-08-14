package no.ut.trip;

import java.util.List;

import no.ut.trip.maps.MapPickOverlay;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class MapsActivity extends MapActivity {
    static final String TAG = "MapsActivity";

    public static final Uri DEFAULT_URI = Uri.parse("geo:60,10");

    public static final int MENU_ITEM_MYLOCATION = Menu.FIRST;
    public static final int MENU_ITEM_SELECTED = Menu.FIRST + 1;
    public static final int MENU_ITEM_PIN_HERE = Menu.FIRST + 2;
    public static final int MENU_ITEM_PIN_RESET = Menu.FIRST + 3;

    MapView mapView;
    GeoPointVO geoPoint;
    MapController controller;

    MyLocationOverlay myLocationOverlay;
    MapPickOverlay mapPickOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.maps);

	final Intent intent = getIntent();
	Uri uri = intent.getData();
	if (uri == null) {
	    uri = DEFAULT_URI;
	}

	try {
	    geoPoint = GeoPointVO.fromUri(uri);
	} catch (Exception e) {
	    Log.w(TAG, "Failed parsing data URI: " + uri, e);
	    geoPoint = GeoPointVO.fromUri(DEFAULT_URI);
	}

	mapView = (MapView) findViewById(R.id.mapview);
	mapView.setBuiltInZoomControls(true);
	controller = mapView.getController();

	GeoPoint center = geoPoint.getGeoPoint();
	Log.d(TAG, "Setting center of map to " + center);
	controller.setCenter(center);

	List<Overlay> overlays = mapView.getOverlays();

	myLocationOverlay = new MyLocationOverlay(this, mapView);
	overlays.add(myLocationOverlay);

	mapPickOverlay = new MapPickOverlay(this, center);
	overlays.add(mapPickOverlay);
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	menu.add("Context menu");
	Toast.makeText(MapsActivity.this, "activity context menu",
		Toast.LENGTH_SHORT).show();
    }

    public MapView getMapView() {
	return mapView;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
	return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
	if (MotionEvent.ACTION_DOWN == event.getAction()) {

	}

	return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
	super.onResume();

	myLocationOverlay.enableCompass();
	myLocationOverlay.enableMyLocation();
    }

    private void onBackButton() {
	String action = getIntent().getAction();
	if (Intent.ACTION_PICK.equals(action)
		|| Intent.ACTION_GET_CONTENT.equals(action)) {
	    Uri uri = createUri();
	    Log.d(TAG, "Closing window action PICK, returning uri: " + uri);
	    setResult(RESULT_OK, new Intent().setData(uri));
	}
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (KeyEvent.KEYCODE_BACK == keyCode) {
	    onBackButton();
	}

	return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
	super.onPause();

	myLocationOverlay.disableMyLocation();
	myLocationOverlay.disableCompass();
    }

    private Uri createUri() {
	GeoPoint point = mapPickOverlay.getLocation();
	GeoPointVO p = GeoPointVO.fromGeoPoint(point);
	return p.getUri();
    }

    @Override
    protected boolean isRouteDisplayed() {
	return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);

	Intent intent = new Intent(null, getIntent().getData());
	intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
	menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
		new ComponentName(this, MapsActivity.class), null, intent, 0,
		null);

	return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
	super.onPrepareOptionsMenu(menu);

	menu.removeItem(MENU_ITEM_SELECTED);
	menu.add(Menu.CATEGORY_ALTERNATIVE, MENU_ITEM_SELECTED, Menu.NONE,
		R.string.menu_selected)
		.setIcon(android.R.drawable.ic_menu_view);

	menu.removeItem(MENU_ITEM_PIN_HERE);
	menu.add(Menu.CATEGORY_ALTERNATIVE, MENU_ITEM_PIN_HERE, Menu.NONE,
		R.string.menu_pin_here).setIcon(R.drawable.custom_general);

	menu.removeItem(MENU_ITEM_PIN_RESET);
	if (mapPickOverlay.hasChanged()) {
	    menu.add(Menu.CATEGORY_SECONDARY, MENU_ITEM_PIN_RESET, Menu.NONE,
		    R.string.menu_reset).setIcon(
		    android.R.drawable.ic_menu_revert);
	}

	menu.removeItem(MENU_ITEM_MYLOCATION);
	GeoPoint myLocation = myLocationOverlay.getMyLocation();
	if (myLocation != null) {
	    menu.add(Menu.CATEGORY_SECONDARY, MENU_ITEM_MYLOCATION, Menu.NONE,
		    R.string.menu_mylocation).setIcon(
		    android.R.drawable.ic_menu_mylocation);
	}

	return true;
    }

    private boolean animateToMyLocation() {
	GeoPoint myLocation = myLocationOverlay.getMyLocation();
	if (myLocation != null) {
	    mapView.getController().animateTo(myLocation);
	    return true;
	}

	return false;
    }

    private boolean animateToPinLocation() {
	GeoPoint selectedLocation = mapPickOverlay.getLocation();
	if (selectedLocation != null) {
	    mapView.getController().animateTo(selectedLocation);
	    return true;
	}

	return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case MENU_ITEM_MYLOCATION:
	    return animateToMyLocation();

	case MENU_ITEM_SELECTED:
	    return animateToPinLocation();

	case MENU_ITEM_PIN_HERE:
	    GeoPoint mapCenter = mapView.getMapCenter();
	    mapPickOverlay.setLocation(mapCenter);
	    return animateToPinLocation();

	case MENU_ITEM_PIN_RESET:
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(R.string.alert_reset_pin_q).setCancelable(false)
		    .setPositiveButton("Yes",
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {
				    mapPickOverlay.resetLocation();
				    animateToPinLocation();
				}
			    }).setNegativeButton("No",
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {
				    dialog.cancel();
				}
			    });
	    AlertDialog alert = builder.create();
	    alert.show();
	    return true;
	}

	return super.onOptionsItemSelected(item);
    }
}
