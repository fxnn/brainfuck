package de.fxnn.brainfuck.tape;

import java.io.DataInput;
import java.io.DataOutput;
import javax.annotation.Nonnull;

public interface Tape<T> {

  void rewind();

  void moveForward() throws OutOfTapeBoundsException;

  void moveBackward() throws OutOfTapeBoundsException;

  void increment();

  void decrement();

  void readTo(DataOutput output) throws TapeIOException;

  void writeFrom(DataInput input) throws TapeIOException;

  boolean isZero();

  void writeInteger(@Nonnull Integer value);

  Integer readInteger();
}
