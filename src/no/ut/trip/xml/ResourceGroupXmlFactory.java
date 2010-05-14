package no.ut.trip.xml;

import org.w3c.dom.Node;

public class ResourceGroupXmlFactory {

    static public ResourceGroup createResourceGroup(Node node) {
	ResourceList list = ResourceListXmlFactory.createResourceList(node);
	ResourceGroup group = new ResourceGroup(list);
	return group;
    }

}
