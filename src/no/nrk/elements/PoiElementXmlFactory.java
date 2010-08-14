package no.nrk.elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import no.nrk.listings.result.Content;
import no.nrk.listings.xml.TextualContentXmlFactory;
import no.nrk.listings.xml.collection.NodeListAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PoiElementXmlFactory implements DublinCore {

    protected Document doc;
    protected Element node;

    public PoiElementXmlFactory(InputStream is) {
	Document doc = null;
	try {
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    doc = db.parse(is);
	    this.doc = doc;
	    this.node = doc.getDocumentElement();
	} catch (IOException ioe) {
	    System.err.println("Invalid XML format!!");
	    throw new RuntimeException("Failed to create element", ioe);
	} catch (ParserConfigurationException pce) {
	    System.err.println("Could not parse XML!");
	    throw new RuntimeException("Failed to create element", pce);
	} catch (SAXException se) {
	    System.err.println("Could not parse XML!");
	    throw new RuntimeException("Failed to create element", se);
	}
    }

    public PoiElement create() {
	PoiElement e = new PoiElement();
	e.setDescription(getDescription());
	return e;
    }

    public Date getCreated() {
	// Date created = null;
	//
	// NodeList nodes = this.node.getChildNodes();
	// for (Node node : new NodeListAdapter<Node>(nodes)) {
	// if (node.getNodeName().equals("created")) {
	// created = new Date(node.getNodeValue());
	// return created;
	// }
	// }

	return null;
    }

    public Date getModified() {
	// TODO Auto-generated method stub
	return null;
    }

    public String getSubject() {
	// TODO Auto-generated method stub
	return null;
    }

    public String getTitle() {
	String title = null;

	NodeList nodes = node.getChildNodes();
	for (Node node : new NodeListAdapter<Node>(nodes)) {
	    if (node.getNodeName().equals("title")) {
		Content content = TextualContentXmlFactory
			.createTextualContent(node);
		title = content.getValue();
		return title;
	    }
	}

	return null;
    }

    public UUID getUUID() {
	// TODO Auto-generated method stub
	return null;
    }

    public String getDescription() {
	String desc = null;

	NodeList nodes = node.getChildNodes();
	for (Node node : new NodeListAdapter<Node>(nodes)) {
	    if (node.getNodeName().equals("description")) {
		Content content = TextualContentXmlFactory
			.createTextualContent(node);
		desc = content.getValue();
		return desc;
	    }
	}

	return null;
    }

}
