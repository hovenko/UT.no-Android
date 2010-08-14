package no.ut.trip.maps.util;

import com.google.android.maps.GeoPoint;

public class GeoPosSpecification {

    final GeoPoint spec;

    public GeoPosSpecification(GeoPoint spec) {
	this.spec = spec;
    }

    public boolean isSatisfiedBy(GeoPoint candidate) {
	if (candidate == null) {
	    return false;
	}

	if (candidate.getLatitudeE6() == spec.getLatitudeE6()
		&& candidate.getLongitudeE6() == spec.getLongitudeE6()) {
	    return true;
	}

	return false;
    }
}
