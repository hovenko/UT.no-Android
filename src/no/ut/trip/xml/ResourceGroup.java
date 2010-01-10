package no.ut.trip.xml;

import no.ut.trip.xml.util.NodeListAdapter;

import org.w3c.dom.Node;

public class ResourceGroup<T extends Resource> {
    protected final Node node;
    protected final ResourceList resources = new ResourceList();

    public ResourceGroup(Node node) {
        this.node = node;
        setupNode(node);
    }

    protected void setupNode(Node node) {
        setupResources(node);
    }

    protected void setupResources(Node node) {
        for (Node child : new NodeListAdapter<Node>(node.getChildNodes())) {
            if (child.getNodeName().equals("resource")) {
                Resource res = new Resource(child);
                resources.add(res);
            }
        }
    }

    public ResourceList resources() {
        return resources;
    }
}
