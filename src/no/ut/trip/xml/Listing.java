package no.ut.trip.xml;

import java.io.InputStream;

import no.nrk.listings.facet.FacetField;

public class Listing {
    protected ListingXmlRepository repository;

    public Listing(InputStream is) {
	repository = new ListingXmlRepository(is);
    }

    public FacetList facets() {
	return repository.getFacets();
    }

    public FacetGroup facetsByType(FacetField type) {
	return repository.getFacetsByType(type);
    }

    public ResultList result() {
	return repository.getResult();
    }

    public PartialList partials() {
	return repository.getPartials();
    }
}
