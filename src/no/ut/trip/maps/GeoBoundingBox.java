package no.ut.trip.maps;

import com.google.android.maps.GeoPoint;

public class GeoBoundingBox {
    private GeoPoint nwPoint;
    private GeoPoint sePoint;

    public GeoBoundingBox(GeoPoint nwPoint, GeoPoint sePoint) {
	this.nwPoint = nwPoint;
	this.sePoint = sePoint;
    }

    public GeoPoint getNorthWest() {
	return nwPoint;
    }

    public GeoPoint getSouthEast() {
	return sePoint;
    }
}
