package impl;

import lombok.Getter;
import sql.SRow;

import java.util.*;

@Getter
public class SRowImpl extends ListAdapter<Object> implements SRow {
    private final List<String> columns;
    private final Map<String, Integer> columnMap;

    public <R> SRowImpl(Map<String, Integer> columnMap, List<Object> list) {
        super(list);
        this.columnMap = columnMap;
        this.columns = columnMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).toList();
    }

    @Override
    public List<String> columns() {
        return columns;
    }

    @Override
    public Object get(String column) {
        return get(columnMap.get(column.toLowerCase()));
    }

    @Override
    public <T> T get(String column, Class<T> type) {
        //noinspection unchecked
        return (T) get(column);
    }
}

class ListAdapter<L> implements List<L> {

    private final List<L> list;

    public ListAdapter(List<L> list) {
        this.list = list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<L> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public L get(int index) {
        return list.get(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<L> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<L> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<L> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(list).containsAll(c);
    }


    @Override
    public boolean add(L l) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends L> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends L> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public L set(int index, L element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, L element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public L remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return list.toString();
    }
}