package no.ut.trip.xml;

import java.net.MalformedURLException;
import java.net.URL;

import no.ut.trip.WS;
import no.ut.trip.xml.util.NodeMapAdapter;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import android.util.Log;

public class SingleResourceNode implements Resource {
    static final String TAG = "SingleResourceNode";

    protected Node node;

    protected String value;
    protected String type;
    protected URL url;
    protected String label;

    public SingleResourceNode(Node node) {
	this.node = node;
	setupNode(node);
    }

    protected void setupNode(Node node) {
	Log.d(TAG, "setupNode: " + node.getNodeName());
	NamedNodeMap attrs = node.getAttributes();
	setupNodeAttributes(attrs);
    }

    protected void setupNodeAttributes(NamedNodeMap attrs) {
	if (null == attrs) {
	    throw new NullPointerException("Param attrs cannot be null");
	}

	for (Node attr : new NodeMapAdapter<Node>(attrs)) {
	    Log.d(TAG, attr.toString());
	}

	String strUrl = attrs.getNamedItemNS(WS.NS, "url").getNodeValue();
	type = attrs.getNamedItemNS(WS.NS, "type").getNodeValue();
	value = attrs.getNamedItemNS(WS.NS, "value").getNodeValue();
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
