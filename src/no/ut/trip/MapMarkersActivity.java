package no.ut.trip;

import java.util.ArrayList;
import java.util.List;

import no.ut.trip.maps.MapEvent;
import no.ut.trip.maps.MapEventListener;
import no.ut.trip.maps.PoiMarkersOverlay;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class MapMarkersActivity extends MapActivity {
    static final String TAG = "MapMarkersActivity";

    public static final Uri DEFAULT_URI = Uri.parse("geo:60,10");

    public static final int MENU_ITEM_MYLOCATION = Menu.FIRST;

    MapView mapView;
    GeoPointVO geoPoint;
    MapController controller;
    GeoPoint oldCenter;

    private ArrayList<MapEventListener> listeners = new ArrayList<MapEventListener>();

    MyLocationOverlay myLocationOverlay;
    PoiMarkersOverlay markersOverlay;
    boolean refreshMarkers = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.maps);

	final Intent intent = getIntent();
	Uri uri = intent.getData();
	if (uri == null) {
	    uri = DEFAULT_URI;
	}

	geoPoint = GeoPointVO.fromUri(uri);

	mapView = (MapView) findViewById(R.id.mapview);
	mapView.setBuiltInZoomControls(true);
	controller = mapView.getController();

	GeoPoint center = geoPoint.getGeoPoint();
	Log.d(TAG, "Setting center of map to " + center);
	controller.setCenter(center);

	List<Overlay> overlays = mapView.getOverlays();

	myLocationOverlay = new MyLocationOverlay(this, mapView);
	overlays.add(myLocationOverlay);

	Drawable drawablePoi = this.getResources().getDrawable(
		R.drawable.custom_general);
	markersOverlay = new PoiMarkersOverlay(this, drawablePoi);
	overlays.add(markersOverlay);
	listeners.add(markersOverlay);
    }

    public MapView getMapView() {
	return mapView;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
	return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onResume() {
	super.onResume();

	myLocationOverlay.enableCompass();
	myLocationOverlay.enableMyLocation();
    }

    @Override
    protected void onPause() {
	super.onPause();

	myLocationOverlay.disableMyLocation();
	myLocationOverlay.disableCompass();
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
		new ComponentName(this, MapMarkersActivity.class), null,
		intent, 0, null);

	return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
	super.onPrepareOptionsMenu(menu);

	menu.removeItem(MENU_ITEM_MYLOCATION);

	GeoPoint myLocation = myLocationOverlay.getMyLocation();
	if (myLocation != null) {
	    menu.add(Menu.CATEGORY_SECONDARY, MENU_ITEM_MYLOCATION, 0,
		    R.string.menu_mylocation).setIcon(
		    android.R.drawable.ic_menu_mylocation);
	}

	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case MENU_ITEM_MYLOCATION:
	    GeoPoint myLocation = myLocationOverlay.getMyLocation();
	    if (myLocation != null) {
		MapEvent event = new MapEvent(MapEvent.ACTION_ANIMATE);
		for (MapEventListener listener : listeners) {
		    listener.onChange(event);
		}
		mapView.getController().animateTo(myLocation,
			new AnimationFinished());
		return true;
	    }
	}

	return super.onOptionsItemSelected(item);
    }

    private final class AnimationFinished implements Runnable {
	public void run() {
	    MapEvent event = new MapEvent(MapEvent.ACTION_ANIMATE_STOP);
	    for (MapEventListener listener : listeners) {
		listener.onChange(event);
	    }
	}
    }
}
