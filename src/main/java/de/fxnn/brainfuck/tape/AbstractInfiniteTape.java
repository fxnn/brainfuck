package de.fxnn.brainfuck.tape;

import javax.annotation.Nonnull;

public abstract class AbstractInfiniteTape<T> implements Tape<T> {

  private TapeSegment<T> currentSegment;

  public AbstractInfiniteTape() {
    currentSegment = createSegment();
  }

  @Override
  public void moveForward() throws OutOfTapeBoundsException {
    try {
      currentSegment.moveForward();
    } catch (OutOfTapeBoundsException ex) {
      currentSegment = getOrCreateNextSegment().atBeginning();
    }
  }

  protected TapeSegment<T> getOrCreateNextSegment() {
    TapeSegment<T> nextSegment = currentSegment.getNextSegment();
    if (nextSegment != null) {
      return nextSegment;
    }

    return createNextSegment();
  }

  protected TapeSegment<T> createNextSegment() {
    TapeSegment<T> nextSegment = createSegment();
    nextSegment.setPreviousSegment(currentSegment);
    currentSegment.setNextSegment(nextSegment);

    return nextSegment;
  }

  @Override
  public void moveBackward() throws OutOfTapeBoundsException {
    try {
      currentSegment.moveBackward();
    } catch (OutOfTapeBoundsException ex) {
      currentSegment = getOrCreatePreviousSegment().atEnd();
    }
  }

  public TapeSegment<T> getOrCreatePreviousSegment() {
    TapeSegment<T> previousSegment = createSegment().getPreviousSegment();
    if (previousSegment != null) {
      return previousSegment;
    }

    return createPreviousSegment();
  }

  private TapeSegment<T> createPreviousSegment() {
    TapeSegment<T> previousSegment = createSegment();
    previousSegment.setNextSegment(currentSegment);
    currentSegment.setPreviousSegment(previousSegment);

    return previousSegment;
  }

  protected abstract TapeSegment<T> createSegment();

  protected void write(@Nonnull T value) {
    currentSegment.write(value);
  }

  protected T read() {
    return currentSegment.read();
  }

}
