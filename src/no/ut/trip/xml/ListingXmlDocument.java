package no.ut.trip.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import no.nrk.listings.ListingDocument;
import no.nrk.listings.Search;
import no.nrk.listings.facet.FacetResource;
import no.nrk.listings.facet.FacetSet;
import no.nrk.listings.facet.PartialResource;
import no.nrk.listings.partial.PartialList;
import no.nrk.listings.result.Item;
import no.nrk.listings.result.ResultList;
import no.ut.trip.xml.util.NodeListAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ListingXmlDocument implements ListingDocument {

    protected Document doc;
    protected Element node;

    public ListingXmlDocument(InputStream is) {
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

    public FacetSet getFacets() {
	FacetSet facets = new FacetSet();

	NodeList nodes = node.getChildNodes();
	for (Node node : new NodeListAdapter<Node>(nodes)) {
	    if (node.getNodeName().equals("facet")) {
		Set<FacetResource> list = FacetListXmlFactory
			.createFacetList(node);
		facets.addAll(list);
	    }
	}

	return facets;
    }

    public ResultList getResults() {
	ResultList all = new ResultList();

	NodeList nodes = this.node.getChildNodes();
	for (Node node : new NodeListAdapter<Node>(nodes)) {
	    if (node.getNodeName().equals("result")) {
		List<Item> list = ResultListXmlFactory.createResultList(node);
		all.addAll(list);
	    }
	}

	return all;
    }

    public PartialList getPartials() {
	PartialList all = new PartialList();

	NodeList nodes = node.getChildNodes();
	for (Node node : new NodeListAdapter<Node>(nodes)) {
	    if (node.getNodeName().equals("partials")) {
		FacetSet list = FacetListXmlFactory.createFacetList(node);
		for (FacetResource item : list) {
		    PartialResource partial = new PartialResource(item);
		    all.add(partial);
		}
	    }
	}

	return all;
    }

    public Search getSearch() {
	Search search = SearchXmlFactory.createSearch(node);

	return search;
    }
}
