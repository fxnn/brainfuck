package de.fxnn.brainfuck.tape;

import java.io.DataInput;

public enum TapeEofBehaviour {

  THROWS {
    @Override
    public Integer getEofValue(Tape tape, DataInput input) throws TapeIOException {
      throw new TapeIOException("Could not read from input [" + input + "] to tape [" + tape + "], as no more input data is available.");
    }
  },
  READS_ZERO {
    @Override
    public Integer getEofValue(Tape tape, DataInput input) throws TapeIOException {
      return 0;
    }
  },
  READS_MINUS_ONE {
    @Override
    public Integer getEofValue(Tape tape, DataInput input) throws TapeIOException {
      return -1;
    }
  };

  public abstract Integer getEofValue(Tape tape, DataInput input) throws TapeIOException;

}
