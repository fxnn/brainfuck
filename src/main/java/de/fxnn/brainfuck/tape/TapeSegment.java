package de.fxnn.brainfuck.tape;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

import static java.util.Collections.nCopies;

public class TapeSegment<T> {

  List<T> list;

  int index;

  @Getter
  @Setter
  Optional<TapeSegment<T>> previousSegment = Optional.empty();

  @Getter
  @Setter
  Optional<TapeSegment<T>> nextSegment = Optional.empty();

  public TapeSegment(int size, T initialValue) {
    this.list = new ArrayList<>(nCopies(size, initialValue));
    moveToIndex(0);
  }

  public TapeSegment<T> atBeginning() {
    moveToIndex(0);
    return this;
  }

  public TapeSegment<T> atEnd() {
    moveToIndex(list.size() - 1);
    return this;
  }

  public void moveForward() throws OutOfTapeBoundsException {
    int newIndex = index + 1;

    if (newIndex >= list.size()) {
      throw new OutOfTapeBoundsException("Would move beyond end of tape segment [newIndex=" + newIndex + "]!");
    }

    moveToIndex(newIndex);
  }

  public void moveBackward() throws OutOfTapeBoundsException {
    int newIndex = index - 1;

    if (newIndex < 0) {
      throw new OutOfTapeBoundsException("Would move before start of tape segment [newIndex=" + newIndex + "]");
    }

    moveToIndex(newIndex);
  }

  protected void moveToIndex(int index) {
    this.index = index;
  }

  public T read() {
    return list.get(index);
  }

  public void write(T value) {
    list.set(index, value);
  }

}
