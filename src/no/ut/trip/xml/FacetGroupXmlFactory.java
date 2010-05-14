package no.ut.trip.xml;

import no.nrk.listings.result.Subject;

import org.w3c.dom.Node;

public class FacetGroupXmlFactory {

    static public FacetGroup createFacetGroup(Node node) {
	ResourceGroup group = ResourceGroupXmlFactory.createResourceGroup(node);
	FacetGroup facetgroup = new FacetGroup(group);

	String type = node.getAttributes().getNamedItem("type").getNodeValue();
	facetgroup.type = new Subject(type);

	return facetgroup;
    }

}
