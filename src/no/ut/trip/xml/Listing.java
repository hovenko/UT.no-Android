package no.ut.trip.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import no.ut.trip.xml.util.NodeListAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Listing {

    protected Document doc;
    protected Element node;

    public Listing(InputStream is) {
	Document doc = null;
	try {
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    doc = db.parse(is);
	    this.doc = doc;
	    this.node = doc.getDocumentElement();
	} catch (IOException ioe) {
	    System.err.println("Invalid XML format!!");
	    throw new RuntimeException("Failed to create listings", ioe);
	} catch (ParserConfigurationException pce) {
	    System.err.println("Could not parse XML!");
	    throw new RuntimeException("Failed to create listings", pce);
	} catch (SAXException se) {
	    System.err.println("Could not parse XML!");
	    throw new RuntimeException("Failed to create listings", se);
	}
    }

    public FacetList facets() {
	FacetList facets = new FacetList();

	NodeList nodes = node.getChildNodes();
	for (Node node : new NodeListAdapter<Node>(nodes)) {
	    if (node.getNodeName().equals("facet")) {
		FacetGroup facet = new FacetGroup(node);
		facets.add(facet);
	    }
	}

	return facets;
    }

    public Result result() {
	Result list = new Result();

	NodeList nodes = this.node.getChildNodes();
	for (Node node : new NodeListAdapter<Node>(nodes)) {
	    if (node.getNodeName().equals("result")) {
		for (Node subnode : new NodeListAdapter<Node>(node
			.getChildNodes())) {
		    if (subnode.getNodeName().equals("item")) {
			ResultItem item = new ResultItem(subnode);
			list.add(item);
		    }
		}
	    }
	}

	return list;
    }

    public PartialList partials() {
	PartialList partials = new PartialList();

	NodeList nodes = this.node.getChildNodes();
	for (Node node : new NodeListAdapter<Node>(nodes)) {
	    if (node.getNodeName().equals("partials")) {
		for (Node subnode : new NodeListAdapter<Node>(node
			.getChildNodes())) {
		    if (subnode.getNodeName().equals("resource")) {
			Partial partial = new Partial(subnode);
			partials.add(partial);
		    }
		}
	    }
	}

	return partials;
    }

}
