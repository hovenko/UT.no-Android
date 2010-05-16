package no.ut.trip.xml;

import no.nrk.listings.result.Content;
import no.ut.trip.xml.util.NodeListAdapter;

import org.w3c.dom.Node;

public class TextualContentXmlFactory {

    static public Content createTextualContent(Node node) {
	StringBuilder b = createStringBuilder(node);

	Content content = new Content(b.toString());
	return content;
    }

    static public StringBuilder createStringBuilder(Node node, StringBuilder b) {
	String nodeValue = node.getNodeValue();
	if (null != nodeValue) {
	    b.append(nodeValue);
	}

	for (Node child : new NodeListAdapter<Node>(node.getChildNodes())) {
	    createStringBuilder(child, b);
	}

	return b;
    }

    static public StringBuilder createStringBuilder(Node node) {
	StringBuilder b = new StringBuilder();
	return createStringBuilder(node, b);
    }
}
