package no.ut.trip.content;

import android.net.Uri;
import android.provider.BaseColumns;

public class Pois implements BaseColumns {
    public static final Uri CONTENT_URI = Uri.withAppendedPath(
	    PoiProvider.CONTENT_URI, "pois");

    public static final String CREATED_DATE = "created";
    public static final String MODIFIED_DATE = "modified";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";
    public static final String ARRIVAL = "arrival";
    public static final String GEO = "geopoint";

    private Pois() {
    }

    static class Images implements BaseColumns {
	public static final Uri CONTENT_URI = Uri.withAppendedPath(
		Pois.CONTENT_URI, "images");

	public static final String POI_ID = "poi_id";
	public static final String MEDIA_URI = "media_uri";
    }
}
