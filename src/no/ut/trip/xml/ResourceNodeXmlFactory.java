package no.ut.trip.xml;

import java.net.MalformedURLException;
import java.net.URL;

import no.nrk.listings.result.Subject;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ResourceNodeXmlFactory {

    static public ResourceImpl createResourceNode(Node node) {
	NamedNodeMap attrs = node.getAttributes();

	ResourceImpl res = new ResourceImpl();

	String strUrl = attrs.getNamedItem("url").getNodeValue();
	String strType = attrs.getNamedItem("type").getNodeValue();
	res.type = new Subject(strType);
	res.value = attrs.getNamedItem("value").getNodeValue();
	res.label = node.getNodeValue();

	try {
	    res.url = new URL(strUrl);
	} catch (MalformedURLException e) {
	    throw new RuntimeException("Bad URL of facet resource", e);
	}

	return res;
    }

}
