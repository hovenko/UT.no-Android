package no.ut.trip.ws;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import no.ut.trip.maps.content.JsonMarkerAdapter;

import org.json.JSONObject;

import android.net.Uri;

public class MapMarkersClient {
    static final String TAG = "MapMarkersClient";

    public static JsonMarkerAdapter retrieve(MapMarkersRequest req) {
	Uri uri = req.getUri();
	return retrieve(uri.toString());
    }

    public static JsonMarkerAdapter retrieve(String strUri) {
	try {
	    InputStream is = UtHttpClient.retrieve(strUri);

	    JSONObject json = new JSONObject(convertStreamToString(is));
	    JsonMarkerAdapter markers = new JsonMarkerAdapter(json);
	    return markers;
	} catch (Exception e) {
	    throw new RuntimeException("Failing to retrieve JSON", e);
	}
    }

    public static String convertStreamToString(InputStream is) throws Exception {
	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	StringBuilder sb = new StringBuilder();
	String line = null;
	while ((line = reader.readLine()) != null) {
	    sb.append(line + "\n");
	}
	is.close();
	return sb.toString();
    }
}
