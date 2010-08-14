package no.ut.trip.maps;

import no.ut.trip.MapsActivity;
import no.ut.trip.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/**
 * @author Knut-Olav Hoven <knutolav@gmail.com>
 */
public class MapPickOverlay extends Overlay {
    private MapsActivity activity;
    private GeoPoint origPoint;
    private GeoPoint currentPoint;
    private boolean grip = false;
    Bitmap icon;

    public MapPickOverlay(MapsActivity context, GeoPoint origPoint) {
	super();
	this.origPoint = origPoint;
	this.currentPoint = origPoint;
	this.activity = context;

	icon = BitmapFactory.decodeResource(activity.getResources(),
		R.drawable.custom_general);
    }

    @Override
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
	    long when) {
	Paint paint = new Paint();
	// paint.setColor(Color.RED);

	Projection proj = mapView.getProjection();
	Point p = proj.toPixels(currentPoint, null);
	// canvas.drawCircle(p.x, p.y, 8f, paint);

	canvas.drawBitmap(icon, p.x - 12, p.y - 32, paint);

	return super.draw(canvas, mapView, shadow, when);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
	int x = (int) event.getX();
	int y = (int) event.getY();
	Projection proj = mapView.getProjection();

	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    Point point = proj.toPixels(currentPoint, null);
	    int centerX = point.x;
	    int centerY = point.y - 16;

	    double radCircle = Math
		    .sqrt((double) (((centerX - x) * (centerX - x)) + (centerY - y)
			    * (centerY - y)));

	    if (radCircle < 16) {
		grip = true;
	    } else {
		grip = false;
	    }
	} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
	    if (grip) {
		GeoPoint newPoint = proj.fromPixels(x, y + 16);
		currentPoint = newPoint;
		return true;
	    }
	} else {
	    grip = false;
	}

	return super.onTouchEvent(event, mapView);
    }

    public void resetLocation() {
	currentPoint = origPoint;
    }

    public boolean hasChanged() {
	if (currentPoint != origPoint) {
	    return true;
	}

	return false;
    }

    public GeoPoint getLocation() {
	return currentPoint;
    }

    public void setLocation(GeoPoint newPoint) {
	currentPoint = newPoint;
    }
}
