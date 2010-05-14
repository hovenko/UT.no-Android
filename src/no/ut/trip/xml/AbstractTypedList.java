package no.ut.trip.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import no.nrk.listings.result.Subject;

public abstract class AbstractTypedList<T extends Typed> implements List<T> {
    protected List<T> list;

    public AbstractTypedList() {
	list = new ArrayList<T>();
    }

    public T findByType(Subject type) {
	if (null == type) {
	    throw new NullPointerException("Requires a string: type");
	}
	for (T item : list) {
	    if (type.equals(item.getType())) {
		return item;
	    }
	}

	return null;
    }

    public boolean add(T object) {
	return list.add(object);
    }

    public void add(int location, T object) {
	list.add(location, object);
    }

    public boolean addAll(Collection<? extends T> arg0) {
	return list.addAll(arg0);
    }

    public boolean addAll(int arg0, Collection<? extends T> arg1) {
	return list.addAll(arg0, arg1);
    }

    public void clear() {
	list.clear();
    }

    public boolean contains(Object object) {
	return list.contains(object);
    }

    public boolean containsAll(Collection<?> arg0) {
	return list.containsAll(arg0);
    }

    public T get(int location) {
	return list.get(location);
    }

    public int indexOf(Object object) {
	return list.indexOf(object);
    }

    public boolean isEmpty() {
	return list.isEmpty();
    }

    public Iterator<T> iterator() {
	return list.iterator();
    }

    public int lastIndexOf(Object object) {
	return list.lastIndexOf(object);
    }

    public ListIterator<T> listIterator() {
	return list.listIterator();
    }

    public ListIterator<T> listIterator(int location) {
	return list.listIterator(location);
    }

    public T remove(int location) {
	return list.remove(location);
    }

    public boolean remove(Object object) {
	return list.remove(object);
    }

    public boolean removeAll(Collection<?> arg0) {
	return list.removeAll(arg0);
    }

    public boolean retainAll(Collection<?> arg0) {
	return list.retainAll(arg0);
    }

    public T set(int location, T object) {
	return list.set(location, object);
    }

    public int size() {
	return list.size();
    }

    public List<T> subList(int start, int end) {
	return list.subList(start, end);
    }

    public Object[] toArray() {
	return list.toArray();
    }

    @SuppressWarnings("hiding")
    public <T> T[] toArray(T[] array) {
	return list.toArray(array);
    }
}
