package no.ut.trip;

import no.ut.trip.error.ExceptionHandler;
import no.ut.trip.widget.listing.SubjectsListAdapter;
import no.ut.trip.ws.ListingsClient;
import no.ut.trip.xml.Listing;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

public class EntrySubjects extends Activity {

    static final int DIALOG_PROGRESS = 0;

    static final String TAG = "EntrySubjects";

    ProgressDialog progressDialog = null;

    Listing listing = null;

    protected Dialog onCreateDialog(int id) {
	switch (id) {
	case DIALOG_PROGRESS:
	    progressDialog = new ProgressDialog(EntrySubjects.this);
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
		Listing listing = ListingsClient.retrieve(strUri);
		EntrySubjects.this.setListing(listing);
	    } catch (Exception e) {
		Log.e(TAG, "Failed to get listings", e);
		new ExceptionHandler(EntrySubjects.this,
			"Failed to get listings", e).setRethrow(true).show();
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
	setContentView(R.layout.entry_subjects);

	showDialog(DIALOG_PROGRESS);

	Intent intent = getIntent();
	Uri intentUri = intent.getData();

	ProgressThread progressThread = new ProgressThread(handler, intentUri);
	progressThread.start();
    }

    private void setupListings(final Listing listings) {
	Log.v(TAG, "setupListings()");

	setupListingSubjects(listings);
    }

    private void setupListingSubjects(final Listing listings) {
	ListView list = (ListView) findViewById(R.id.list_facet);

	SubjectsListAdapter adapter = new SubjectsListAdapter(this, listings);

	list.setAdapter(adapter);
	list.setTextFilterEnabled(true);
    }

}