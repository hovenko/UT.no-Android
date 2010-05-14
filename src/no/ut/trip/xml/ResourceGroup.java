package no.ut.trip.xml;

public class ResourceGroup {
    protected final ResourceList resources = new ResourceList();

    /**
     * Default constructor.
     */
    public ResourceGroup() {

    }

    public ResourceGroup(ResourceList list) {
	this.resources.addAll(list);
    }

    public ResourceGroup(ResourceGroup origin) {
	this.resources.addAll(origin.resources);
    }

    public ResourceList resources() {
	return resources;
    }

}
