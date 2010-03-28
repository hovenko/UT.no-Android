package no.ut.trip.xml;

import java.net.URL;

import org.w3c.dom.Node;

public interface Resource extends Typed {

    public Node getNode();

    public String getValue();

    public String getLabel();

    public URL getURL();
}
