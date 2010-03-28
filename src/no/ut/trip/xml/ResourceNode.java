package no.ut.trip.xml;

import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ResourceNode implements Resource {
    protected Node node;

    protected String value;
    protected String type;
    protected URL url;
    protected String label;

    public ResourceNode(Node node) {
	this.node = node;
	setupNode(node);
    }

    protected void setupNode(Node node) {
	NamedNodeMap attrs = node.getAttributes();

	String strUrl = attrs.getNamedItem("url").getNodeValue();
	type = attrs.getNamedItem("type").getNodeValue();
	value = attrs.getNamedItem("value").getNodeValue();
	label = node.getNodeValue();

	try {
	    url = new URL(strUrl);
	} catch (MalformedURLException e) {
	    throw new RuntimeException("Bad URL of facet resource", e);
	}
    }

    public Node getNode() {
	return node;
    }

    public String getValue() {
	return value;
    }

    public String getType() {
	return type;
    }

    public String getLabel() {
	return label;
    }

    public URL getURL() {
	return url;
    }
}
