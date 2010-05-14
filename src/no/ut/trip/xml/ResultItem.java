package no.ut.trip.xml;

import no.nrk.listings.result.Subject;

public class ResultItem extends ResourceGroup implements Typed {

    public Subject subject;
    public TextualContent content;

    public ResultItem() {

    }

    public ResultItem(ResourceGroup group) {
	super(group);
    }

    public Subject getType() {
	return subject;
    }

    public TextualContent getContent() {
	return content;
    }

}
