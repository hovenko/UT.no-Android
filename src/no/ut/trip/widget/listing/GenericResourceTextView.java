package no.ut.trip.widget.listing;

import no.nrk.listings.Resource;
import android.content.Context;
import android.widget.TextView;

public class GenericResourceTextView extends TextView {

    protected Resource resource;

    public GenericResourceTextView(Context context) {
	super(context);
    }

    public void setResource(Resource resource) {
	this.resource = resource;
    }

    public Resource getResource() {
	return resource;
    }

}
