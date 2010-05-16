package no.ut.trip.xml;

import java.net.MalformedURLException;
import java.net.URL;

import no.nrk.listings.ListingResource;
import no.nrk.listings.QueryResource;
import no.nrk.listings.Resource;
import no.nrk.listings.DefaultResource;
import no.nrk.listings.DefaultResourceLabel;
import no.nrk.listings.ResourceLabel;
import no.nrk.listings.facet.FacetResource;
import no.nrk.listings.query.DefaultQueryField;
import no.nrk.listings.query.DefaultQueryToken;
import no.nrk.listings.query.DefaultQueryValue;
import no.nrk.listings.query.QueryField;
import no.nrk.listings.query.QueryToken;
import no.nrk.listings.query.QueryValue;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class FacetXmlFactory {

    static public FacetResource createFacetResource(Node node) {
	Resource res = createResource(node);
	QueryValue value = createQueryValue(node);

	int found = 0;
	Node attrFound = node.getAttributes().getNamedItem("found");
	if (null != attrFound) {
	    String strFound = attrFound.getNodeValue();
	    found = new Integer(strFound);
	}

	QueryResource query = new ListingResource(res, value);
	FacetResource facet = new FacetResource(query, found);

	return facet;
    }

    static protected QueryValue createQueryValue(Node node) {
	String attrType = node.getAttributes().getNamedItem("type")
		.getNodeValue();

	String attrValue = node.getAttributes().getNamedItem("value")
		.getNodeValue();

	QueryField field = new DefaultQueryField(attrType);
	QueryToken token = new DefaultQueryToken(attrValue);
	QueryValue value = new DefaultQueryValue(field, token);
	return value;
    }

    static protected Resource createResource(Node node) {
	NamedNodeMap attrs = node.getAttributes();

	String strUrl = attrs.getNamedItem("url").getNodeValue();
	StringBuilder b = TextualContentXmlFactory.createStringBuilder(node);
	ResourceLabel label = new DefaultResourceLabel(b.toString());

	URL url = null;

	try {
	    url = new URL(strUrl);
	} catch (MalformedURLException e) {
	    throw new RuntimeException("Bad URL of facet resource", e);
	}

	Resource res = new DefaultResource(url, label);

	return res;
    }
}
