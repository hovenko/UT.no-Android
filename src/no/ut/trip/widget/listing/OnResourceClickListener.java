package no.ut.trip.widget.listing;

import no.ut.trip.xml.ResourceNode;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

public class OnResourceClickListener implements OnClickListener {
    private final ResourceNode resource;
    private final Activity activity;

    public OnResourceClickListener(final Activity activity,
	    final ResourceNode resource) {
	this.resource = resource;
	this.activity = activity;
    }

    public void onClick(View v) {
	Intent intent = new Intent(Intent.ACTION_SEARCH);
	Uri uri = Uri.parse(resource.getURL().toString());
	intent.setDataAndType(uri, "application/xml");
	activity.startActivity(intent);
    }
}