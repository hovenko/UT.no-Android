package no.ut.trip;

import no.nrk.listings.ListingDocument;
import no.ut.trip.error.ExceptionHandler;
import no.ut.trip.widget.listing.ListingListAdapter;
import no.ut.trip.ws.ListingsClient;
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

    ListingDocument listing = null;

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

    protected synchronized void setListing(ListingDocument listing) {
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
		ListingDocument listing = ListingsClient.retrieve(strUri);
		Listings.this.setListing(listing);
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

    private void setupListings(final ListingDocument listings) {
	Log.v(TAG, "setupListings()");

	setupListingsExpandable(listings);
    }

    private void setupListingsExpandable(final ListingDocument listings) {
	ExpandableListView list = (ExpandableListView) findViewById(R.id.list_facet);

	ExpandableListAdapter adapter = new ListingListAdapter(this, listings);

	list.setAdapter(adapter);
	list.setTextFilterEnabled(true);
    }

}