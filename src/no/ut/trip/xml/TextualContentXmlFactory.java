package no.ut.trip.xml;

import no.ut.trip.xml.util.NodeListAdapter;

import org.w3c.dom.Node;

public class TextualContentXmlFactory {

    static public TextualContent createTextualContent(Node node) {
	StringBuilder b = new StringBuilder();

	for (Node child : new NodeListAdapter<Node>(node.getChildNodes())) {
	    b.append(child.getNodeValue());
	}

	TextualContent content = new TextualContent(b.toString());
	return content;
    }

}
