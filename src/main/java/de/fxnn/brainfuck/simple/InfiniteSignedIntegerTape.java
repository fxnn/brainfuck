package de.fxnn.brainfuck.simple;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import javax.annotation.Nonnull;

public class InfiniteSignedIntegerTape extends AbstractInfiniteTape<Integer> {

  static final int DEFAULT_TAPE_SEGMENT_SIZE = 1024;

  static final int DEFAULT_VALUE = 0;

  @Override
  protected InfiniteTapeSegment<Integer> createSegment() {
    return new InfiniteTapeSegment<>(DEFAULT_TAPE_SEGMENT_SIZE, DEFAULT_VALUE);
  }

  @Override
  public void increment() {
    write(read()+1);
  }

  @Override
  public void decrement() {
    write(read()-1);
  }

  @Override
  public void readTo(BufferedWriter writer) throws IOException {
    writer.write(read());
  }

  @Override
  public void writeFrom(BufferedReader reader) throws IOException {
    write(reader.read());
  }

  @Override
  public boolean isZero() {
    return read() == 0;
  }
}
