package de.fxnn.util;

import java.util.Iterator;

public class IteratorToIterable<E> implements Iterable<E> {

  private final Iterator<E> iterator;

  private IteratorToIterable(Iterator<E> iterator) {
    this.iterator = iterator;
  }

  @Override
  public Iterator<E> iterator() {
    return iterator;
  }

  public static <E> IteratorToIterable<E> iterateOnce(Iterator<E> iterator) {
    return new IteratorToIterable<>(iterator);
  }

}
