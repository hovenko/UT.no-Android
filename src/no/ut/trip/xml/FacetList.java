package no.ut.trip.xml;

public class FacetList extends AbstractList<FacetGroup> {

    public FacetGroup facetByType(String type) {
        if (null == type) {
            throw new NullPointerException("Requires a string: type");
        }

        for (FacetGroup facet : list) {
            if (type.equals(facet.getType())) {
                return facet;
            }
        }

        return null;
    }
}
