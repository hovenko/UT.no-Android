package no.ut.trip.maps;

import no.ut.trip.PoiDrawableFactory;
import no.ut.trip.maps.content.JsonMarkerItem;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.OverlayItem;

public class PoiMarker extends OverlayItem {

    JsonMarkerItem item;
    Context context;

    public PoiMarker(Context context, JsonMarkerItem item, String title,
	    String snippet) {
	super(item.getPoint().getGeoPoint(), item.getCustomType(), snippet);
	this.item = item;
	this.context = context;
    }

    public JsonMarkerItem getJsonMarkerItem() {
	return item;
    }

    public Drawable getDrawableByType() {
	PoiDrawableFactory factory = new PoiDrawableFactory(context);
	return factory.getDrawableByType(item.getCustomType());
    }
}
