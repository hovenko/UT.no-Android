package no.ut.trip.xml;

public class ResultItemList extends AbstractList<ResultItem> {

    public ResultItemList itemsByType(String type) {
        ResultItemList sub = new ResultItemList();

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
