package no.ut.trip.xml;

import no.nrk.listings.facet.FacetResource;
import no.nrk.listings.facet.FacetSet;
import no.ut.trip.xml.util.NodeListAdapter;

import org.w3c.dom.Node;

public class FacetListXmlFactory {

    /**
     * @param node
     * @return
     */
    static public FacetSet createFacetList(Node node) {
	FacetSet list = new FacetSet();

	for (Node child : new NodeListAdapter<Node>(node.getChildNodes())) {
	    if (child.getNodeName().equals("resource")) {
		FacetResource facet = FacetXmlFactory
			.createFacetResource(child);
		list.add(facet);
	    }
	}

	return list;
    }
}
