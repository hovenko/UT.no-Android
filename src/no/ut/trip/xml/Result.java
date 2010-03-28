package no.ut.trip.xml;

public class Result extends AbstractList<ResultItem> {

    public Result itemsByType(String type) {
        Result sub = new Result();

        if (null == type) {
            throw new NullPointerException("Requires a string: type");
        }
        for (ResultItem item : list) {
            if (type.equals(item.getType())) {
                sub.add(item);
            }
        }

        return sub;
    }
}
