package no.ut.trip.xml;

public class PartialList extends AbstractList<Partial> {

    public Partial partialByType(String type) {
        if (null == type) {
            throw new NullPointerException("Requires a string: type");
        }
        for (Partial partial : list) {
            if (type.equals(partial.getType())) {
                return partial;
            }
        }

        return null;
    }
}
