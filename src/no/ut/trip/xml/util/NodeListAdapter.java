package no.ut.trip.xml.util;

import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeListAdapter<T extends Node> implements Iterable<T> {
    protected NodeList list;
    protected int index = 0;

    public NodeListAdapter(NodeList list) {
	if (null == list) {
	    throw new NullPointerException("Cannot be a null reference: list");
	}
	this.list = list;
    }

    public Iterator<T> iterator() {
	Iterator<T> i = new Iterator<T>() {

	    public boolean hasNext() {
		if (index < list.getLength()) {
		    return true;
		} else {
		    return false;
		}
	    }

	    @SuppressWarnings("unchecked")
	    public T next() {
		T node = (T) list.item(index);
		index++;
		return node;
	    }

	    public void remove() {
		return;

	    }
	};

	return i;
    }
}
