package no.ut.trip.xml;

import no.ut.trip.xml.util.NodeListAdapter;

import org.w3c.dom.Node;

public class ResourceGroup {
    protected final ResourceList resources = new ResourceList();

    public ResourceGroup(Node node) {
	setupNode(node);
    }

    protected void setupNode(Node node) {
	setupResources(node);
    }

    protected void setupResources(Node node) {
	for (Node child : new NodeListAdapter<Node>(node.getChildNodes())) {
	    if (child.getNodeName().equals("resource")) {
		ResourceNode res = new ResourceNode(child);
		resources.add(res);
	    }
	}
    }

    public ResourceList resources() {
	return resources;
    }

}
