package de.fxnn.brainfuck.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import de.fxnn.brainfuck.OutOfTapeBoundsException;
import lombok.Getter;
import lombok.Setter;

import static java.util.Collections.nCopies;

public class InfiniteTapeSegment<T> {

  List<T> list;

  int position;

  @Getter
  @Setter
  Optional<InfiniteTapeSegment<T>> previousSegment = Optional.empty();

  @Getter
  @Setter
  Optional<InfiniteTapeSegment<T>> nextSegment = Optional.empty();

  public InfiniteTapeSegment(int size, T initialValue) {
    this.list = new ArrayList<>(nCopies(size, initialValue));
    moveToIndex(0);
  }

  public InfiniteTapeSegment<T> atBeginning() {
    moveToIndex(0);
    return this;
  }

  public InfiniteTapeSegment<T> atEnd() {
    moveToIndex(list.size() - 1);
    return this;
  }

  protected void moveToIndex(int index) {
    position = index;
  }

  public void moveForward() throws OutOfTapeBoundsException {
    if (position + 1 >= list.size()) {
      throw new OutOfTapeBoundsException(
          "Would move beyond end of tape segment [newIndex=" + (position + 1) + "]!");
    }

    position++;
  }

  public void moveBackward() throws OutOfTapeBoundsException {
    if (position - 1 < 0) {
      throw new OutOfTapeBoundsException(
          "Would move before start of tape segment [newIndex=" + (position - 1) + "]");
    }

    position--;
  }

  public T read() {
    return list.get(position);
  }

  public void write(T value) {
    list.set(position, value);
  }

}
