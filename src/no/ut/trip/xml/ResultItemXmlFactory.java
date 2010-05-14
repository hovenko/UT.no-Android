package no.ut.trip.xml;

import no.nrk.listings.result.Subject;
import no.ut.trip.xml.util.NodeListAdapter;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ResultItemXmlFactory {

    static public ResultItem createResultItem(Node node) {
	ResourceList list = ResourceListXmlFactory.createResourceList(node);
	ResourceGroup group = new ResourceGroup(list);
	ResultItem item = new ResultItem(group);

	NamedNodeMap attrs = node.getAttributes();

	String strSubject = attrs.getNamedItem("subject").getNodeValue();
	Subject subject = new Subject(strSubject);
	item.subject = subject;

	for (Node child : new NodeListAdapter<Node>(node.getChildNodes())) {
	    String nodename = child.getNodeName();
	    if ("content".equals(nodename)) {
		item.content = TextualContentXmlFactory
			.createTextualContent(child);
	    }
	}

	return item;
    }

}
