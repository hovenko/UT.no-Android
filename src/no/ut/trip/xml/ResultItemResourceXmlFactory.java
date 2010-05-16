package no.ut.trip.xml;

import java.util.HashSet;
import java.util.Set;

import no.nrk.listings.Resource;
import no.nrk.listings.ResourceType;
import no.nrk.listings.result.ItemResource;
import no.ut.trip.xml.util.NodeListAdapter;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ResultItemResourceXmlFactory {

    static public Set<ItemResource> createResourceList(Node node) {
	Set<ItemResource> list = new HashSet<ItemResource>();

	for (Node child : new NodeListAdapter<Node>(node.getChildNodes())) {
	    if (child.getNodeName().equals("resource")) {
		Resource res = ResourceNodeXmlFactory.createResourceNode(child);
		NamedNodeMap attrs = child.getAttributes();

		String strType = attrs.getNamedItem("type").getNodeValue();
		ResourceType type = new ResourceType(strType);
		ItemResource value = new ItemResource(res, type);

		list.add(value);
	    }
	}

	return list;
    }
}
