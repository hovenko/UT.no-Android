package no.ut.trip.xml;

import no.nrk.listings.Search;
import no.nrk.listings.query.QueryValue;
import no.ut.trip.xml.util.NodeListAdapter;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SearchXmlFactory {

    static public Search createSearch(Node node) {
	Search search = new Search();

	NodeList nodes = node.getChildNodes();
	for (Node subnode : new NodeListAdapter<Node>(nodes)) {
	    if (node.getNodeName().equals("search")) {
		QueryValue value = QueryXmlFactory.createQueryValue(subnode);
		search.add(value);
	    }
	}

	return search;
    }
}
