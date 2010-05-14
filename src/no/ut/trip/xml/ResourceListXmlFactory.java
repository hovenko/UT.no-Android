package no.ut.trip.xml;

import no.ut.trip.xml.util.NodeListAdapter;

import org.w3c.dom.Node;

public class ResourceListXmlFactory {

    static public ResourceList createResourceList(Node node) {
	ResourceList list = new ResourceList();
	for (Node child : new NodeListAdapter<Node>(node.getChildNodes())) {
	    if (child.getNodeName().equals("resource")) {
		ResourceImpl res = ResourceNodeXmlFactory
			.createResourceNode(child);
		list.add(res);
	    }
	}

	return list;
    }

}
