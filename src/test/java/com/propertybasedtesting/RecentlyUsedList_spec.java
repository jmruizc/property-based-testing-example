package com.propertybasedtesting;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.*;

import static com.propertybasedtesting.ListBasedRecentlyUsedList.newInstance;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

@RunWith(Enclosed.class)
public final class RecentlyUsedList_spec {

    @RunWith(JUnitQuickcheck.class)
    public static final class A_new_list {

        @Rule
        public final ExpectedException thrown = ExpectedException.none();

        @Property
        public void cannot_be_instantiated_with_a_nonpositive_capacity(
                @InRange(maxInt = 0) int capacity) {
            thrown.expect(IllegalArgumentException.class);
            newInstance(capacity);
        }

        @Property
        public void can_be_instantiated_with_a_positive_capacity(
                @InRange(minInt = 1) int capacity) {
            assertEquals(capacity, newInstance(capacity).getCapacity());
        }

        @Property
        public void is_empty(
                @InRange(minInt = 1) int capacity) {
            assertEquals(true, newInstance(capacity).isEmpty());
        }

    }

    @RunWith(JUnitQuickcheck.class)
    public static final class An_empty_list {

        @Property
        public void retains_a_single_addition(
                @InRange(minInt = 1) int capacity,
                String element) {
            RecentlyUsedList<String> rul = newInstance(capacity);

            rul.push(element);

            assertThat(rul.size()).isEqualTo(1);
            assertThat(rul.elementAt(0)).isEqualTo(element);
        }

        @Property
        public void retains_unique_additions_in_stack_order_up_to_its_capacity(
                @InRange(minInt = 1) int capacity,
                Set<String> uniqueItems) {
            RecentlyUsedList<String> rul = recentlyUsedListBuiltFrom(capacity, uniqueItems);

            assertThat(rul.size()).isEqualTo(uniqueItems.size());

            List<String> copy = new ArrayList<>(uniqueItems);
            Collections.reverse(copy);
            int toIndex = Math.min(uniqueItems.size(), rul.capacity());
            List<String> expected = copy.subList(0, toIndex);
            assertThat(rul.toList()).isEqualTo(expected);

        }

    }

    @RunWith(JUnitQuickcheck.class)
    public static final class An_nonempty_list {

        @Property
        public void is_unchanged_when_its_head_item_is_re_added(
                @InRange(minInt = 1) int capacity) {
            String item = "Item A";
            String item2 = "Item B";
            ListBasedRecentlyUsedList<String> listBasedRecentlyUsedList = newInstance(capacity);
            listBasedRecentlyUsedList.push(item2);
            listBasedRecentlyUsedList.push(item);
            assertEquals(2, listBasedRecentlyUsedList.capacity());
            listBasedRecentlyUsedList.push(item);
            assertEquals(2, listBasedRecentlyUsedList.capacity());
            assertEquals(item, listBasedRecentlyUsedList.elementAt(0));
        }

    }

    @RunWith(JUnitQuickcheck.class)
    public static final class Any_list {

        @Rule
        public final ExpectedException thrown = ExpectedException.none();

        @Property
        public void rejects_the_addition_of_a_null_item(
                @InRange(minInt = 1) int capacity) {
            thrown.expect(NullPointerException.class);
            ListBasedRecentlyUsedList<String> listBasedRecentlyUsedList = newInstance(capacity);
            listBasedRecentlyUsedList.push(null);
        }

        @Property
        public void of_at_least_two_items_moves_a_non_head_item_to_head_when_that_item_is_re_added(
                @InRange(minInt = 1) int capacity,
                Set<String> items) {
            assumeThat(items.size() , greaterThanOrEqualTo(2));

            RecentlyUsedList<String> rul = recentlyUsedListBuiltFrom(capacity, items);
            int index = new Random().nextInt(rul.size()); // not ideal :'(
            String nonHeadItem = rul.elementAt(index);

            rul.push(nonHeadItem);

            assertThat(rul.elementAt(0)).isEqualTo(nonHeadItem);

            RecentlyUsedList<String> initialRul = recentlyUsedListBuiltFrom(capacity, items);
            // All elements up to the nonhead item have been shifted to the right
            for (int i = 1; i <= index; i++) {
                assertThat(rul.elementAt(i)).isEqualTo(initialRul.elementAt(i - 1));
            }

            // All elements after the nonhead item have not moved
            for (int i = index + 1; i < rul.size(); i++) {
                assertThat(rul.elementAt(i)).isEqualTo(initialRul.elementAt(i));
            }

        }

        @Property
        public void that_gets_cleared_yields_an_empty_list_of_the_same_capacity(
                @InRange(minInt = 1) int capacity,
                Set<String> items) {
            RecentlyUsedList<String> rul = recentlyUsedListBuiltFrom(capacity, items);

            rul.clear();

            assertThat(rul.isEmpty()).isTrue();
            assertThat(rul.size()).isEqualTo(0);
        }

        @Property
        public void allows_indexing_within_its_bounds(
                @InRange(minInt = 1) int capacity,
                Set<String> items) {
            RecentlyUsedList<String> recentlyUsedList = recentlyUsedListBuiltFrom(capacity, items);

            List<String> stringList = recentlyUsedList.toList();
            for (int i = 0, hi = recentlyUsedList.size(); i < hi; i++) {
                assertThat(recentlyUsedList.elementAt(i)).isEqualTo(stringList.get(i));
            }
        }

        @Property
        public void rejects_negative_indexing(
                @InRange(minInt = 1) int capacity,
                Set<String> items,
                @InRange(maxInt = -1) int index) {
            RecentlyUsedList<String> recentlyUsedList = recentlyUsedListBuiltFrom(capacity, items);

            thrown.expect(IndexOutOfBoundsException.class);
            recentlyUsedList.elementAt(index);
        }

        @Property
        public void rejects_indexing_past_its_end(
                @InRange(minInt = 1, maxInt = 10) int capacity,
                Set<String> items,
                int index) {
            assumeThat(index, greaterThanOrEqualTo(items.size()));
            thrown.expect(IndexOutOfBoundsException.class);
            recentlyUsedListBuiltFrom(capacity, items).elementAt(index);
        }

        @Test
        public void satisfies_the_equals_hashCode_contract() {
            EqualsVerifier.forClass(ListBasedRecentlyUsedList.class).verify();
        }

        @Property
        public void has_a_sensible_toString_implementation(
                @InRange(minInt = 1) int capacity,
                Set<String> items) {
            RecentlyUsedList<String> rul = recentlyUsedListBuiltFrom(capacity, items);

            String expected = String.format("RecentlyUsedList%s", rul.toList());
            assertThat(rul.toString()).isEqualTo(expected);
        }

    }

    private static <T> RecentlyUsedList<T> recentlyUsedListBuiltFrom(
            int capacity,
            Collection<T> items) {
        RecentlyUsedList<T> rul = newInstance(capacity);
        items.forEach(rul::push);
        return rul;
    }

}
