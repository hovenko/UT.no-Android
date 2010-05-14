package no.ut.trip.xml;

import no.nrk.listings.result.Subject;

public class PartialList extends AbstractTypedList<Partial> {

    public Partial partialByType(String type) {
	if (null == type) {
	    throw new NullPointerException("Requires a string: type");
	}
	for (Partial partial : list) {
	    if (type.equals(partial.getType())) {
		return partial;
	    }
	}

	return null;
    }

    public PartialList filterIgnores(Subject[] ignore_fields) {
	PartialList filtered = new PartialList();

	for (Partial partial : this) {
	    boolean toAdd = true;

	    for (Subject ignore : ignore_fields) {
		if (ignore.equals(partial.getType())) {
		    toAdd = false;
		}
	    }

	    if (toAdd) {
		filtered.add(partial);
	    }
	}

	return filtered;
    }
}
