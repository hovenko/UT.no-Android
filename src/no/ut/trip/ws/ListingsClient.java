package no.ut.trip.ws;

import java.io.InputStream;
import java.net.URI;

import no.ut.trip.xml.Listing;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

public class ListingsClient {
    static final String TAG = "ListingsClient";

    public static Listing retrieve(String strUri) throws Exception {
	Log.d(TAG, "Retrieving URL: " + strUri);

	URI uri = new URI(strUri);
	HttpClient client = new DefaultHttpClient();
	HttpContext localContext = new BasicHttpContext();
	HttpGet get = new HttpGet(uri);
	HttpResponse response = client.execute(get, localContext);
	InputStream is = response.getEntity().getContent();
	return new Listing(is);
    }
}
