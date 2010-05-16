package no.ut.trip.xml;

import no.nrk.listings.query.DefaultQueryField;
import no.nrk.listings.query.DefaultQueryToken;
import no.nrk.listings.query.DefaultQueryValue;
import no.nrk.listings.query.QueryField;
import no.nrk.listings.query.QueryToken;
import no.nrk.listings.query.QueryValue;

import org.w3c.dom.Node;

public class QueryXmlFactory {

    static public QueryValue createQueryValue(Node node) {
	String attrType = node.getAttributes().getNamedItem("type")
		.getNodeValue();

	String attrValue = node.getAttributes().getNamedItem("value")
		.getNodeValue();

	QueryField field = new DefaultQueryField(attrType);
	QueryToken token = new DefaultQueryToken(attrValue);
	QueryValue value = new DefaultQueryValue(field, token);
	return value;
    }

}
