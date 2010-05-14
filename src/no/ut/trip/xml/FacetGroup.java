package no.ut.trip.xml;

import no.nrk.listings.result.Subject;

public class FacetGroup extends ResourceGroup implements Typed {

    public Subject type;

    public FacetGroup() {

    }

    public FacetGroup(ResourceGroup group) {
	super(group);
    }

    public Subject getType() {
	return type;
    }

}
