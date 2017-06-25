package com.propertybasedtesting;

import java.util.List;

public interface RecentlyUsedList<T> {

    /**
     * Returns {@code true} if, and only if, {@link #size()} is {@code 0}.
     *
     * @return {@code true} if {@link #size()} is {@code 0}, and
     * {@code false} otherwise.
     */
    boolean isEmpty();

    /**
     * Returns the size of this recently used list,
     * i.e. the number of items it currently contains.
     *
     * @return the number of items that this recently used list contains.
     */
    int size();

    /**
     * Returns the capacity of this recently used list,
     * i.e. the maximum number of items that it can contain.
     * The capacity is set at instantiation and remains unchanged
     * throughout the lifetime of a recently used list.
     *
     * @return the capacity of this recently used list
     */
    int capacity();

    /**
     * Clear this recently used list of all the items it contains.
     */
    void clear();

    /**
     * Adds the specified item as new most recently used item to
     * this recently used list.
     * If this recently used list does not already contains the item,
     * the former is simply added to the top (as it would in a stack).
     * Otherwise, the item is bumped to the top of this recently used list, and
     * any item preceding it in this recently used list gets shifted to the right.
     *
     * @param newHead the new most recently used item
     * @throws NullPointerException If {@code newHead} is {@code null}
     */
    void push(T newHead);

    /**
     * Returns the {@code T} item at the specified index.
     * An index ranges from {@code 0} to {@code size() - 1}.
     * The most recently used element is at index {@code 0};
     * the second most recently used element is at index {@code 1};
     * and so on.
     *
     * @param index the index of the {@code T} item.
     * @return the {@code T} item at the specified index
     *         of this recently used list.
     * @throws IndexOutOfBoundsException if {@code index} is negative or
     *         is greater than or equal to {@code size()}.
     */
    T elementAt(int index);

    /**
     * Returns a {@code List<T>} view of this recently used list.
     *
     * @return a {@code List<T>} view of this recently used list.
     */
    List<T> toList();

}
