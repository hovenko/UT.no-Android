package no.ut.trip;

import android.net.Uri;

import com.google.android.maps.GeoPoint;

public class GeoPointVO {
    public Double latitude;
    public Double longitude;

    public GeoPointVO() {

    }

    public Double getLatitude() {
	return latitude;
    }

    public Double getLongitude() {
	return longitude;
    }

    public Uri getUri() {
	String str = "geo:" + latitude + "," + longitude;
	return Uri.parse(str);
    }

    public GeoPoint getGeoPoint() {
	int intLat = (int) (latitude * 1E6);
	int intLng = (int) (longitude * 1E6);

	GeoPoint p = new GeoPoint(intLat, intLng);
	return p;
    }

    public static GeoPointVO fromGeoPoint(GeoPoint point) {
	GeoPointVO p = new GeoPointVO();
	p.latitude = point.getLatitudeE6() / 1E6;
	p.longitude = point.getLongitudeE6() / 1E6;
	return p;
    }

    public static GeoPointVO fromUri(Uri uri) {
	if (null == uri) {
	    throw new NullPointerException("uri cannot be null");
	}

	String scheme = uri.getScheme();
	if (!"geo".equals(scheme)) {
	    throw new IllegalArgumentException("bad uri scheme: " + scheme);
	}

	String strUri = uri.toString();

	int idxQuery = strUri.indexOf("?");

	String coordinates = strUri.substring(4);
	if (idxQuery != -1) {
	    coordinates = strUri.substring(4, idxQuery);
	}

	if ("".equals(coordinates)) {
	    throw new IllegalArgumentException("missing coordinates: " + uri);
	}

	int idx = coordinates.indexOf(",");
	if (idx == -1) {
	    throw new IllegalArgumentException(
		    "bad geo coordinates, missing comma");
	}

	String latitudeString = coordinates.substring(0, idx).trim();
	String longitudeString = coordinates.substring(idx + 1).trim();

	double latitude = Double.parseDouble(latitudeString);
	double longitude = Double.parseDouble(longitudeString);

	GeoPointVO p = new GeoPointVO();
	p.latitude = latitude;
	p.longitude = longitude;

	return p;
    }
}
