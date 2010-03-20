package no.ut.trip;

import java.io.InputStream;
import java.net.URI;

import no.ut.trip.error.ExceptionHandler;
import no.ut.trip.widget.listing.ListingListAdapter;
import no.ut.trip.xml.Listing;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

public class Listings extends Activity {

    static final int DIALOG_PROGRESS = 0;

    static final String TAG = "Listings";

    ProgressDialog progressDialog = null;

    Listing listing = null;

    protected Dialog onCreateDialog(int id) {
	switch (id) {
	case DIALOG_PROGRESS:
	    progressDialog = new ProgressDialog(Listings.this);
	    progressDialog.setMessage(getText(R.string.process_loading_data));
	    progressDialog.setIndeterminate(true);
	    return progressDialog;

	default:
	    return null;
	}
    }

    protected synchronized void setListing(Listing listing) {
	this.listing = listing;
    }

    // Define the Handler that receives messages from the thread and update the
    // progress
    final Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    boolean complete = msg.getData().getBoolean("complete");
	    if (complete) {
		dismissDialog(DIALOG_PROGRESS);
		setupListings(listing);
	    }
	}
    };

    /**
     * Nested class that performs progress calculations (counting)
     */
    private class ProgressThread extends Thread {
	Handler mHandler;
	final Uri intentUri;

	ProgressThread(Handler h, Uri intentUri) {
	    mHandler = h;
	    this.intentUri = intentUri;
	}

	public void run() {
	    try {
		String strUri = intentUri.toString();
		Log.d("http", "Retrieving URL: " + strUri);

		URI uri = new URI(strUri);
		HttpClient client = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet get = new HttpGet(uri);
		HttpResponse response = client.execute(get, localContext);
		InputStream is = response.getEntity().getContent();
		Listings.this.setListing(new Listing(is));
	    } catch (Exception e) {
		Log.e(TAG, "Failed to get trip listings", e);
		new ExceptionHandler(Listings.this,
			"Failed to get trip listings", e).setRethrow(true)
			.show();
		return;
	    }

	    Log.v(TAG, "Closing progress dialog");
	    Message msg = mHandler.obtainMessage();
	    Bundle b = new Bundle();
	    b.putBoolean("complete", true);
	    msg.setData(b);
	    mHandler.sendMessage(msg);
	}
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.listings);

	showDialog(DIALOG_PROGRESS);

	Intent intent = getIntent();
	Uri intentUri = intent.getData();

	ProgressThread progressThread = new ProgressThread(handler, intentUri);
	progressThread.start();
    }

    private void setupListings(final Listing listings) {
	Log.v(TAG, "setupListings()");

	setupListingsExpandable(listings);
    }

    private void setupListingsExpandable(final Listing listings) {
	ExpandableListView list = (ExpandableListView) findViewById(R.id.list_facet);

	ExpandableListAdapter adapter = new ListingListAdapter(this, listings);

	list.setAdapter(adapter);
	list.setTextFilterEnabled(true);
    }

    // final class OnResourceItemClickListener implements OnItemClickListener {
    // private final ResourceList resources;
    //
    // public OnResourceItemClickListener(final ResourceList resources) {
    // this.resources = resources;
    // }
    //
    // public void onItemClick(AdapterView<? extends Adapter> parent, View v,
    // int position, long id) {
    // Resource res = resources.get(position);
    // Intent intent = new Intent(Intent.ACTION_SEARCH);
    // Uri uri = Uri.parse(res.getURL().toString());
    // intent.setDataAndType(uri, "application/xml");
    // startActivity(intent);
    // }
    // }

    // final class OnPartialClickListener implements OnItemClickListener {
    // private final PartialList partials;
    //
    // public OnPartialClickListener(final PartialList partials) {
    // this.partials = partials;
    // }
    //
    // public void onItemClick(AdapterView<? extends Adapter> parent, View v,
    // int position, long id) {
    // Partial partial = partials.get(position);
    // Intent intent = new Intent(Intent.ACTION_SEARCH);
    // Uri uri = Uri.parse(partial.getURL().toString());
    // intent.setDataAndType(uri, "application/xml");
    // startActivity(intent);
    // }
    // }

}