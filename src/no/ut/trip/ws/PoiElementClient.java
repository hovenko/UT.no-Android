package no.ut.trip.ws;

import java.io.InputStream;

import no.nrk.elements.PoiElement;
import no.nrk.elements.PoiElementXmlFactory;
import android.util.Log;

public class PoiElementClient {
    static final String TAG = "ElementClient";

    public static PoiElement retrieve(String strUri) throws Exception {
	Log.d(TAG, "Retrieving URL: " + strUri);

	InputStream is = UtHttpClient.retrieve(strUri);
	PoiElement element = new PoiElementXmlFactory(is).create();

	return element;
    }
}
