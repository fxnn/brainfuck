package de.fxnn.brainfuck.tape;

import java.util.Optional;

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
    return currentSegment.getNextSegment().orElseGet(this::createNextSegment);
  }

  protected TapeSegment<T> createNextSegment() {
    TapeSegment<T> nextSegment = createSegment();
    nextSegment.setPreviousSegment(Optional.of(currentSegment));
    currentSegment.setNextSegment(Optional.of(nextSegment));

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
    return createSegment().getPreviousSegment().orElseGet(this::createPreviousSegment);
  }

  private TapeSegment<T> createPreviousSegment() {
    TapeSegment<T> previousSegment = createSegment();
    previousSegment.setNextSegment(Optional.of(currentSegment));
    currentSegment.setPreviousSegment(Optional.of(previousSegment));

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
