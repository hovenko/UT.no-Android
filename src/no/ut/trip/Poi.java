package no.ut.trip;

import no.nrk.elements.PoiElement;
import no.ut.trip.error.ExceptionHandler;
import no.ut.trip.ws.PoiElementClient;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Poi extends Activity {

    static final int DIALOG_PROGRESS = 0;

    static final String TAG = "Poi";

    ProgressDialog progressDialog = null;

    PoiElement element = null;

    protected Dialog onCreateDialog(int id) {
	switch (id) {
	case DIALOG_PROGRESS:
	    progressDialog = new ProgressDialog(Poi.this);
	    progressDialog.setMessage(getText(R.string.process_loading_data));
	    progressDialog.setIndeterminate(true);
	    return progressDialog;

	default:
	    return null;
	}
    }

    protected synchronized void setElement(final PoiElement element) {
	this.element = element;
    }

    // Define the Handler that receives messages from the thread and update the
    // progress
    final Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    boolean complete = msg.getData().getBoolean("complete");
	    if (complete) {
		dismissDialog(DIALOG_PROGRESS);
		setupElement(element);
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
		PoiElement element = PoiElementClient.retrieve(strUri);
		Poi.this.setElement(element);
	    } catch (Exception e) {
		Log.e(TAG, "Failed to get trip listings", e);
		new ExceptionHandler(Poi.this, "Failed to get trip listings", e)
			.setRethrow(true).show();
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

    private void setupElement(final PoiElement element) {
	Log.v(TAG, "setupElement()");

	// setupElementExpandable(element);
	// setupElementResult(element);
    }

    // private void setupListingsExpandable(final ListingDocument listings) {
    // ExpandableListView list = (ExpandableListView)
    // findViewById(R.id.list_facet);
    //
    // ExpandableListAdapter adapter = new PoiAdapter(this, listings);
    //
    // list.setAdapter(adapter);
    // list.setTextFilterEnabled(true);
    // }
    //
    // private void setupListingsResult(final ListingDocument listings) {
    // ListView list = (ListView) findViewById(R.id.list_result);
    // ResultListAdapter adapter = new ResultListAdapter(this, listings);
    //
    // list.setAdapter(adapter);
    // list.setTextFilterEnabled(true);
    // }

}