package no.ut.trip.widget.listing;

import no.ut.trip.Listings;
import no.ut.trip.xml.ResourceNode;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

public class OnResultItemClickListener implements OnClickListener {
    private final ResourceNode resource;
    private final Listings listings;

    public OnResultItemClickListener(final Listings listings,
	    final ResourceNode resource) {
	this.resource = resource;
	this.listings = listings;
    }

    public void onClick(View v) {
	AlertDialog.Builder builder = new AlertDialog.Builder(listings);
	builder.setMessage(resource.getValue()).setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
			// nothing
		    }
		});
	AlertDialog alert = builder.create();
	alert.show();

	// Intent intent = new Intent(Intent.ACTION_SEARCH);
	// Uri uri = Uri.parse(resource.getURL().toString());
	// intent.setDataAndType(uri, "application/xml");
	// startActivity(intent);
    }
}
