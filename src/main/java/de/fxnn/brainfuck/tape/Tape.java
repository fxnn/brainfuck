package de.fxnn.brainfuck.tape;

import java.io.DataInput;
import java.io.DataOutput;

public interface Tape<T> {

  void moveForward() throws OutOfTapeBoundsException;

  void moveBackward() throws OutOfTapeBoundsException;

  void increment();

  void decrement();

  void readTo(DataOutput output) throws TapeIOException;

  void writeFrom(DataInput input) throws TapeIOException;

  boolean isZero();

}
