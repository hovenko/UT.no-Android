package no.ut.trip.xml;

import no.nrk.listings.facet.FacetField;

public class FacetList extends AbstractTypedList<FacetGroup> {

    public FacetGroup facetByType(FacetField type) {
	if (null == type) {
	    throw new NullPointerException("Requires a string: type");
	}

	for (FacetGroup facet : list) {
	    if (type.equals(facet.getType())) {
		return facet;
	    }
	}

	return null;
    }
}
