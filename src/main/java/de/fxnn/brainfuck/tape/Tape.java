package de.fxnn.brainfuck.tape;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public interface Tape<T> {

  void moveForward() throws OutOfTapeBoundsException;

  void moveBackward() throws OutOfTapeBoundsException;

  void increment();

  void decrement();

  void readTo(BufferedWriter writer) throws IOException;

  void writeFrom(BufferedReader reader) throws IOException;

  boolean isZero();

}
