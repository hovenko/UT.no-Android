package no.ut.trip.xml;

import org.w3c.dom.Node;

public class FacetGroup extends ResourceGroup<Facet> implements Typed {

    protected String type;

    public FacetGroup(Node node) {
        super(node);
    }

    @Override
    protected void setupNode(Node node) {
        super.setupNode(node);
        type = node.getAttributes().getNamedItem("type").getNodeValue();
    }

    public String getType() {
        return type;
    }

}
