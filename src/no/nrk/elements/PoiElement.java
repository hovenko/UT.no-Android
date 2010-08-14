package no.nrk.elements;

import java.util.Date;
import java.util.UUID;

public class PoiElement implements DublinCore {
    UUID uuid;
    Date created;
    Date modified;
    String title;
    String subject;
    String description;

    public PoiElement() {

    }

    public UUID getUuid() {
	return uuid;
    }

    public void setUuid(UUID uuid) {
	this.uuid = uuid;
    }

    public void setCreated(Date created) {
	this.created = created;
    }

    public void setModified(Date modified) {
	this.modified = modified;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Date getCreated() {
	return created;
    }

    public Date getModified() {
	return modified;
    }

    public String getSubject() {
	return subject;
    }

    public String getTitle() {
	return title;
    }

    public UUID getUUID() {
	return uuid;
    }

    public String getDescription() {
	return description;
    }

}
