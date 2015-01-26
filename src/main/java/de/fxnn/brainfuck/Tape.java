package de.fxnn.brainfuck;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ListIterator;

import javax.annotation.Nonnull;

public interface Tape<T> {

  void moveForward() throws OutOfTapeBoundsException;

  void moveBackward() throws OutOfTapeBoundsException;

  void increment();

  void decrement();

  void readTo(BufferedWriter writer) throws IOException;

  void writeFrom(BufferedReader reader) throws IOException;

  boolean isZero();

}
