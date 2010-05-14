package no.ut.trip.xml;

import java.net.URL;

import no.nrk.listings.result.Subject;

public class ResourceImpl implements Resource {
    public String value;
    public Subject type;
    public URL url;
    public String label;

    /**
     * Default constructor.
     */
    public ResourceImpl() {

    }

    public ResourceImpl(ResourceImpl self) {
	value = self.value;
	type = self.type;
	url = self.url;
	label = self.label;
    }

    public String getValue() {
	return value;
    }

    public Subject getType() {
	return type;
    }

    public String getLabel() {
	return label;
    }

    public URL getURL() {
	return url;
    }
}
