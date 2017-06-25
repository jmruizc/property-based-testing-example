# Drive the design of a Recently Used List using properties!

This is an example of _property-based testing_ in Java.
My objective was to implement an abstraction known as a
_[Recently Used List]_, using test-driven development.
However, my test suite will consist mostly of _properties_,
as opposed to example-based tests.
To write those properties, I used [junit-quickcheck][junit-quickcheck],
which is an [JUnit][Junit] extension dedicated to property-based testing.

## Set up

First, make sure the following are installed on your machine:

* [Git][git]
* [Java 8][Java8]
* [Gradle][Gradle] (version 4 or greater)

Then, run the following commands in a shell:

```shell
git clone https://github.com/jmruizc/property-based-testing-example.git
cd property-based-testing-example
gradle
```

## What is a Recently Used List?

A Recently Used List is an abstraction that keeps track of items
(e.g. files, webpages) that have recently been used or viewed.
It exhibits some interesting behaviours:

* it's a bit like a set, insofar as it contains no duplicate items;
* it's a bit like a list, insofar as items are ordered and you can index into it;
* it's a bit like a stack, insofar as the most recently used items
  appear at the top of the list.

You most likely have come across Recently Used Lists in the wild:
IntelliJ IDEA's _File > Open Recent_ menu is one,
and so is macOS Safari's _Reading List_.

##  Summary

This project contains a Java interface named `RecentlyUsedList`
and a test class named `RecentlyUsedList_spec`;
the latter contains property-based tests that describe behaviours
that any implementor of `RecentlyUsedList` should exhibit.
Unit test of an implementor of `RecentlyUsedList`
satisfies all those properties.

Ideally, when writing TDD-style we should proceed as follows:

1. write one of the properties listed in `RecentlyUsedList_spec`;
2. implement just enough in your `RecentlyUsedList` implementor
   to satisfy that property.

Note that [`RecentlyUsedList`] is a generic interface:
it has a type parameter, `T`, which corresponds to the elements' type.
In order to test [`RecentlyUsedList`] implementor,
we'll need to use some concrete value type in your tests,
with a sensible `equals`/`hashcode` implementation (e.g. `String` or `Integer`).

### Getting started: example of writing your first property

Let's write a test that check that `ListBasedRecentlyUsedList`
satisfies the following property:

> A new list cannot be instantiated with a nonpositive capacity.

You might express that behaviour using an example-based test:

```java
@Test
public void cannot_be_instantiated_with_a_nonpositive_capacity() {
    thrown.expect(IllegalArgumentException.class);
    newInstance(-1);
}
```

That's a good start, but it's somewhat unsatisfactory.
The main problem with such a test is that using a particular value (`-1`, here)
for the capacity leaves room for ambiguity.
Readers of this test might wonder:

> What should happen if I pass `-42` to `newInstance`?
> Who knows? The test says nothing about it...

Granted, the ambiguity is dispelled by the name of the test method,
but that name isn't much better than a comment, because it's not executable.

We can improve the situation somewhat by introducing a clarifying local variable.
However, the test still describes a particular case of a more general behaviour
that applies, not just for `-1`, but for any nonpositive integer.

Therefore, let's make our test more precise by converting it to a property:

```java
@Property
public void cannot_be_instantiated_with_a_nonpositive_capacity(
        @InRange(maxInt = 0) int capacity) {
    thrown.expect(IllegalArgumentException.class);
    newInstance(capacity);
}
```

Our test now no longer uses any particular nonpositive value.
Instead, it specifies some behaviour that the system under test should
exhibit for _any_ nonpositive integer:

> Passing any integer less than or equal to `0`
to `ListBasedRecentlyUsedList#newInstance`
should throw an `IllegalArgumentException`.

By converting the original example-based test to a property,
we're raised the level of abstraction of our burgeoning test suite!

# Contributing

If

* you find this README unclear, or
* if you've identified missing properties, or
* if you've found a bug in my solutions,

please submit a pull request.

[git]: https://git-scm.com/
[Gradle]: https://gradle.org/
[Java8]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[JUnit]: http://junit.org/junit4/
[junit-quickcheck]: https://github.com/pholser/junit-quickcheck
