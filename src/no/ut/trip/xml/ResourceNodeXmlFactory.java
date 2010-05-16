package no.ut.trip.xml;

import java.net.MalformedURLException;
import java.net.URL;

import no.nrk.listings.DefaultResourceLabel;
import no.nrk.listings.EmptyResourceLabel;
import no.nrk.listings.Resource;
import no.nrk.listings.DefaultResource;
import no.nrk.listings.ResourceLabel;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ResourceNodeXmlFactory {

    static public Resource createResourceNode(Node node) {
	NamedNodeMap attrs = node.getAttributes();

	String strUrl = attrs.getNamedItem("url").getNodeValue();

	StringBuilder b = TextualContentXmlFactory.createStringBuilder(node);
	ResourceLabel label;

	try {
	    label = new DefaultResourceLabel(b.toString());
	} catch (IllegalArgumentException e) {
	    label = new EmptyResourceLabel();
	}

	URL url;

	try {
	    url = new URL(strUrl);
	} catch (MalformedURLException e) {
	    throw new RuntimeException("Bad URL of facet resource", e);
	}

	Resource res = new DefaultResource(url, label);

	return res;
    }
}
