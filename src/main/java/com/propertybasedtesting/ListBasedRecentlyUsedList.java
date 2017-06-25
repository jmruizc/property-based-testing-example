package com.propertybasedtesting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class ListBasedRecentlyUsedList<T> implements RecentlyUsedList<T> {

    private final int capacity;
    private final LinkedList<T> recentlyUsedList;

    private ListBasedRecentlyUsedList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException();
        } else {
            this.capacity = capacity;
            recentlyUsedList = new LinkedList<>();
        }
    }

    public static <T> ListBasedRecentlyUsedList<T> newInstance(int capacity) {
        return new ListBasedRecentlyUsedList<>(capacity);
    }

    @Override
    public boolean isEmpty() {
        return recentlyUsedList.isEmpty();
    }

    @Override
    public int size() {
        return recentlyUsedList.size();
    }

    @Override
    public int capacity() {
        return recentlyUsedList.size();
    }

    @Override
    public void clear() {
        recentlyUsedList.clear();
    }

    @Override
    public void push(T newHead) {
        if (newHead == null) {
            throw new NullPointerException();
        }

        if (recentlyUsedList.contains(newHead)) {
            recentlyUsedList.remove(newHead);
        }
        recentlyUsedList.push(newHead);
    }

    @Override
    public T elementAt(int index) {
        if (index < 0 || (recentlyUsedList.size() <= index)) {
            throw new IndexOutOfBoundsException();
        }

        return recentlyUsedList.get(index);
    }

    @Override
    public List<T> toList() {
        return new ArrayList<T>(recentlyUsedList);
    }

    public int getCapacity() {
        return capacity;
    }


    @Override
    public String toString() {
        return String.format("RecentlyUsedList%s", this.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListBasedRecentlyUsedList<?> that = (ListBasedRecentlyUsedList<?>) o;

        if (capacity != that.capacity) return false;
        return recentlyUsedList != null ? recentlyUsedList.equals(that.recentlyUsedList) : that.recentlyUsedList == null;
    }

    @Override
    public int hashCode() {
        int result = capacity;
        result = 31 * result + (recentlyUsedList != null ? recentlyUsedList.hashCode() : 0);
        return result;
    }
}
