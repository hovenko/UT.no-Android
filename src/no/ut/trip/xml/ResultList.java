package no.ut.trip.xml;

import no.nrk.listings.result.Subject;

public class ResultList extends AbstractTypedList<ResultItem> {

    public ResultList itemsByType(Subject type) {
	if (null == type) {
	    throw new NullPointerException("Requires a string: type");
	}

	ResultList sub = new ResultList();

	for (ResultItem item : list) {
	    if (type.equals(item.getType())) {
		sub.add(item);
	    }
	}

	return sub;
    }
}
