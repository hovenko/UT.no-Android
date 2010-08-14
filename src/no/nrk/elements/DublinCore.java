package no.nrk.elements;

import java.util.Date;
import java.util.UUID;

public interface DublinCore {

    public String getTitle();

    public String getSubject();

    public Date getCreated();

    public Date getModified();

    public UUID getUUID();

    public String getDescription();
}
