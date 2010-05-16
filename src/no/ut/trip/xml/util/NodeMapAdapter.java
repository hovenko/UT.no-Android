package no.ut.trip.xml.util;

import java.util.Iterator;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NodeMapAdapter<T extends Node> implements Iterable<T> {
    protected NamedNodeMap list;
    protected int index = 0;

    public NodeMapAdapter(NamedNodeMap list) {
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
