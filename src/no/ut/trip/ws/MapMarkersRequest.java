package no.ut.trip.ws;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import no.ut.trip.maps.GeoBoundingBox;
import android.net.Uri;

import com.google.android.maps.GeoPoint;

public class MapMarkersRequest {
    static final String TAG = "MapMarkersRequest";

    public static String URL_MAP_POIS = "http://ut.no/map/ws/poi/";

    private GeoBoundingBox geoBox;
    private Set<String> pois;

    public MapMarkersRequest(GeoBoundingBox geoBox) {
	this.geoBox = geoBox;

	pois = new LinkedHashSet<String>();
	pois.add("poi");
	pois.add("article");
	pois.add("video");
    }

    public Uri getUri() {
	GeoPoint nw = geoBox.getNorthWest();
	GeoPoint se = geoBox.getSouthEast();

	String queryPois = join(pois, "|");

	String query = String.format(
		"nwlat=%s&nwlong=%s&selat=%s&selong=%s&pois=%s", nw
			.getLatitudeE6() / 1E6, nw.getLongitudeE6() / 1E6, se
			.getLatitudeE6() / 1E6, se.getLongitudeE6() / 1E6, Uri
			.encode(queryPois));

	String uriString = URL_MAP_POIS + "?" + query;

	return Uri.parse(uriString);
    }

    private static <E> String join(Collection<E> s, String delimiter) {
	StringBuffer buffer = new StringBuffer();
	Iterator<E> iter = s.iterator();
	while (iter.hasNext()) {
	    buffer.append(iter.next());
	    if (iter.hasNext()) {
		buffer.append(delimiter);
	    }
	}
	return buffer.toString();
    }

}
