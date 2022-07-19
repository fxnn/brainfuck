package de.fxnn.brainfuck.tape;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import javax.annotation.Nonnull;

/**
 * Input and output are done in terms of four byte (aka 32 bit) signed integer values.
 */
public class InfiniteSignedIntegerTape extends AbstractInfiniteTape<Integer> {

  private static final int DEFAULT_TAPE_SEGMENT_SIZE = 1024;

  private static final int DEFAULT_VALUE = 0;

  private final TapeEofBehaviour eofBehaviour;

  public InfiniteSignedIntegerTape(TapeEofBehaviour eofBehaviour) {
    this.eofBehaviour = eofBehaviour;
  }

  @Override
  protected TapeSegment<Integer> createSegment() {
    return new TapeSegment<>(DEFAULT_TAPE_SEGMENT_SIZE, DEFAULT_VALUE);
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
  public void readTo(DataOutput output) throws TapeIOException {
    try {
      output.writeInt(read());
    } catch (IOException ex) {
      throw new TapeIOException("I/O error while writing from tape to output [" + output + "]: " + ex.getMessage(), ex);
    }
  }

  @Override
  public void writeFrom(DataInput input) throws TapeIOException {
    try {
      write(input.readInt());

    } catch (EOFException ex) {
      write(eofBehaviour.getEofValue(this, input));

    } catch (IOException ex) {
      throw new TapeIOException("I/O error while reading from input [" + input + "] to tape: " + ex.getMessage(), ex);
    }
  }

  @Override
  public void writeInteger(@Nonnull Integer value) {
    write(value);
  }

  @Override
  public Integer readInteger() {
    return read();
  }

  @Override
  public boolean isZero() {
    return read() == 0;
  }
}
