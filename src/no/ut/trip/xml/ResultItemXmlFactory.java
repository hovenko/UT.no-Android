package no.ut.trip.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import no.nrk.listings.ResourceType;
import no.nrk.listings.result.Content;
import no.nrk.listings.result.Item;
import no.nrk.listings.result.ItemResource;
import no.nrk.listings.result.Subject;
import no.ut.trip.xml.util.NodeListAdapter;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ResultItemXmlFactory {

    static public Item createResultItem(Node node) {
	Set<ItemResource> resources = ResultItemResourceXmlFactory
		.createResourceList(node);

	NamedNodeMap attrs = node.getAttributes();

	Content content = null;

	for (Node child : new NodeListAdapter<Node>(node.getChildNodes())) {
	    String nodename = child.getNodeName();
	    if ("content".equals(nodename)) {
		content = TextualContentXmlFactory.createTextualContent(child);
	    }
	}

	String strSubject = attrs.getNamedItem("subject").getNodeValue();
	Subject subject = new Subject(strSubject);

	Map<ResourceType, ItemResource> resourceMap = new HashMap<ResourceType, ItemResource>();

	for (ItemResource res : resources) {
	    ResourceType key = res.getType();
	    resourceMap.put(key, res);
	}

	Item item = new Item(subject, resourceMap, content);

	return item;
    }
}
