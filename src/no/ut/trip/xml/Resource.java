package no.ut.trip.xml;

import java.net.URL;

public interface Resource extends Typed {
    public String getValue();

    public String getLabel();

    public URL getURL();
}
