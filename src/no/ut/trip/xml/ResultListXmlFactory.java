package no.ut.trip.xml;

import java.util.ArrayList;
import java.util.List;

import no.nrk.listings.result.Item;
import no.ut.trip.xml.util.NodeListAdapter;

import org.w3c.dom.Node;

public class ResultListXmlFactory {

    static public List<Item> createResultList(Node node) {
	List<Item> list = new ArrayList<Item>();

	for (Node subnode : new NodeListAdapter<Node>(node.getChildNodes())) {
	    if (subnode.getNodeName().equals("item")) {
		Item item = ResultItemXmlFactory.createResultItem(subnode);
		list.add(item);
	    }
	}

	return list;
    }

}
