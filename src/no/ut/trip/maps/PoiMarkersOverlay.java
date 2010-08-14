package no.ut.trip.maps;

import java.util.ArrayList;
import java.util.List;

import no.ut.trip.MapMarkersActivity;
import no.ut.trip.maps.content.JsonMarkerAdapter;
import no.ut.trip.maps.content.JsonMarkerItem;
import no.ut.trip.maps.util.GeoPosSpecification;
import no.ut.trip.ws.MapMarkersClient;
import no.ut.trip.ws.MapMarkersRequest;
import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

/**
 * TODO this sometimes crashes with ArrayIndexOutOfBoundsException maybe it
 * happens when I click on a POI before the new list is populated.
 * 
 * Maybe I should divide the HTTP calls into tiles with x,y,zoom-parameters.
 * 
 * Or maybe I can substitute the entire PoiMarkersOverlay object on change.
 * 
 * @author Knut-Olav Hoven <knutolav@gmail.com>
 */
public class PoiMarkersOverlay extends ItemizedOverlay<PoiMarker> implements
	MapEventListener {
    private final Object markersSync = new Object();
    private ArrayList<PoiMarker> markers = new ArrayList<PoiMarker>();
    private MapMarkersActivity activity;
    private GeoPoint oldCenter;
    private int oldZoom = 0;
    private boolean dontUpdateMarkers = false;

    public PoiMarkersOverlay(MapMarkersActivity context, Drawable defaultMarker) {
	super(boundCenterBottom(defaultMarker));
	this.activity = context;
    }

    @Override
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
	    long when) {
	maybe_reload_markers();

	return super.draw(canvas, mapView, shadow, when);
    }

    @Override
    protected boolean onTap(int index) {
	PoiMarker item;
	synchronized (markersSync) {
	    item = markers.get(index);
	}

	AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
	dialog.setTitle(item.getTitle());
	dialog.setMessage(item.getSnippet());
	dialog.show();

	return true;
    }

    @Override
    public int size() {
	synchronized (markersSync) {
	    return markers.size();
	}
    }

    @Override
    protected PoiMarker createItem(int i) {
	synchronized (markersSync) {
	    return markers.get(i);
	}
    }

    private static void addMarkerToList(List<PoiMarker> toList,
	    PoiMarker overlay) {
	Drawable drawable = overlay.getDrawableByType();
	if (drawable != null) {
	    overlay.setMarker(boundCenterBottom(drawable));
	}

	toList.add(overlay);
    }

    public void addOverlay(PoiMarker overlay) {
	synchronized (markersSync) {
	    addMarkerToList(markers, overlay);
	    populate();
	}
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    dontUpdateMarkers = true;
	} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
	    dontUpdateMarkers = true;
	} else {
	    dontUpdateMarkers = false;
	}

	return super.onTouchEvent(event, mapView);
    }

    private boolean maybe_reload_markers() {
	if (dontUpdateMarkers) {
	    // dont update while touching or moving
	    return false;
	}

	boolean refresh = false;
	MapView mapView = activity.getMapView();
	GeoPoint newCenter = mapView.getMapCenter();
	if (oldCenter == null) {
	    refresh = true;
	    oldCenter = newCenter;
	}

	GeoPosSpecification geoSpec = new GeoPosSpecification(oldCenter);
	if (!geoSpec.isSatisfiedBy(newCenter)) {
	    refresh = true;
	    oldCenter = newCenter;
	}

	int newZoom = mapView.getZoomLevel();
	if (oldZoom != newZoom) {
	    refresh = true;
	    oldZoom = newZoom;
	}

	if (refresh) {
	    refreshMarkers();
	    return true;
	}

	return false;
    }

    private void refreshMarkers() {
	ArrayList<PoiMarker> newMarkers = new ArrayList<PoiMarker>();

	MapView mapView = activity.getMapView();
	Projection proj = mapView.getProjection();
	GeoPoint topLeft = proj.fromPixels(0, 0);
	GeoPoint bottomRight = proj.fromPixels(mapView.getWidth(), mapView
		.getHeight());

	GeoBoundingBox geoBox = new GeoBoundingBox(topLeft, bottomRight);
	MapMarkersRequest markersRequest = new MapMarkersRequest(geoBox);

	JsonMarkerAdapter markers = MapMarkersClient.retrieve(markersRequest);
	for (JsonMarkerItem item : markers.getItems()) {
	    PoiMarker marker = new PoiMarker(activity, item, "Turmål",
		    "Uten beskrivelse");

	    addMarkerToList(newMarkers, marker);
	}

	synchronized (markersSync) {
	    // Just replacing the list
	    this.markers = newMarkers;
	    populate();
	}
    }

    public void onChange(MapEvent event) {
	if (event.getAction() == MapEvent.ACTION_ANIMATE) {
	    this.dontUpdateMarkers = true;
	} else if (event.getAction() == MapEvent.ACTION_ANIMATE_STOP) {
	    dontUpdateMarkers = false;
	}
    }
}
