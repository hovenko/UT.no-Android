package no.ut.trip.xml;

import no.ut.trip.xml.util.NodeListAdapter;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ResultItem extends ResourceGroup<ResultItemResource> implements
        Typed {

    protected String subject;
    protected TextualContent content;

    public ResultItem(Node node) {
        super(node);
    }

    protected void setupNode(Node node) {
        super.setupNode(node);

        NamedNodeMap attrs = node.getAttributes();

        subject = attrs.getNamedItem("subject").getNodeValue();

        for (Node child : new NodeListAdapter<Node>(node.getChildNodes())) {
            if (child.getNodeName().equals("content")) {
                content = new TextualContent(child);
            }
        }
    }

    public ResultItemResource resource() {
        Resource old = resources().get(0);
        ResultItemResource resource = new ResultItemResource(old.getNode());
        return resource;
    }

    public String getType() {
        return subject;
    }

    public TextualContent getContent() {
        return content;
    }

}
