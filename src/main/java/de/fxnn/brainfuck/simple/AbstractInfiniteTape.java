package de.fxnn.brainfuck.simple;

import java.util.Optional;

import javax.annotation.Nonnull;

import de.fxnn.brainfuck.OutOfTapeBoundsException;
import de.fxnn.brainfuck.Tape;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class AbstractInfiniteTape<T> implements Tape<T> {

  private InfiniteTapeSegment<T> currentSegment;

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

  protected InfiniteTapeSegment<T> getOrCreateNextSegment() {
    return currentSegment.getNextSegment().orElseGet(this::createNextSegment);
  }

  protected InfiniteTapeSegment<T> createNextSegment() {
    InfiniteTapeSegment<T> nextSegment = createSegment();
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

  public InfiniteTapeSegment<T> getOrCreatePreviousSegment() {
    return createSegment().getPreviousSegment().orElseGet(this::createPreviousSegment);
  }

  private InfiniteTapeSegment<T> createPreviousSegment() {
    InfiniteTapeSegment<T> previousSegment = createSegment();
    previousSegment.setNextSegment(Optional.of(currentSegment));
    currentSegment.setPreviousSegment(Optional.of(previousSegment));

    return previousSegment;
  }

  protected abstract InfiniteTapeSegment<T> createSegment();

  protected void write(@Nonnull T value) {
    currentSegment.write(value);
  }

  protected T read() {
    return currentSegment.read();
  }

}
