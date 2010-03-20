package no.ut.trip.widget.listing;

import no.ut.trip.Listings;
import no.ut.trip.xml.Resource;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

public class OnResourceClickListener implements OnClickListener {
    private final Resource resource;
    private final Listings adapter;

    public OnResourceClickListener(final Listings adapter,
	    final Resource resource) {
	this.resource = resource;
	this.adapter = adapter;
    }

    public void onClick(View v) {
	Intent intent = new Intent(Intent.ACTION_SEARCH);
	Uri uri = Uri.parse(resource.getURL().toString());
	intent.setDataAndType(uri, "application/xml");
	adapter.startActivity(intent);
    }
}
