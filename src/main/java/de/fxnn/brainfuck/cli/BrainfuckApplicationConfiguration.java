package de.fxnn.brainfuck.cli;

import com.google.common.base.Objects;
import de.fxnn.brainfuck.tape.TapeEofBehaviour;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;

public class BrainfuckApplicationConfiguration {

  private Charset tapeCharset;

  private Charset programCharset;

  private boolean programGivenAsArgument;

  @Nullable
  private File inputFile;

  @Nullable
  private File outputFile;

  private TapeEofBehaviour eofBehaviour;

  public Charset getTapeCharset() {
    return this.tapeCharset;
  }

  public void setTapeCharset(Charset tapeCharset) {
    this.tapeCharset = tapeCharset;
  }

  public Charset getProgramCharset() {
    return this.programCharset;
  }

  public void setProgramCharset(Charset programCharset) {
    this.programCharset = programCharset;
  }

  public boolean isProgramGivenAsArgument() {
    return this.programGivenAsArgument;
  }

  public void setProgramGivenAsArgument(boolean programGivenAsArgument) {
    this.programGivenAsArgument = programGivenAsArgument;
  }

  @Nullable
  public File getInputFile() {
    return this.inputFile;
  }

  public void setInputFile(@Nullable File inputFile) {
    this.inputFile = inputFile;
  }

  @Nullable
  public File getOutputFile() {
    return this.outputFile;
  }

  public void setOutputFile(@Nullable File outputFile) {
    this.outputFile = outputFile;
  }

  public TapeEofBehaviour getEofBehaviour() {
    return this.eofBehaviour;
  }

  public void setEofBehaviour(TapeEofBehaviour eofBehaviour) {
    this.eofBehaviour = eofBehaviour;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BrainfuckApplicationConfiguration that = (BrainfuckApplicationConfiguration) o;
    return programGivenAsArgument == that.programGivenAsArgument && Objects.equal(tapeCharset, that.tapeCharset)
        && Objects.equal(programCharset, that.programCharset) && Objects.equal(inputFile, that.inputFile) && Objects
        .equal(outputFile, that.outputFile) && eofBehaviour == that.eofBehaviour;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(tapeCharset, programCharset, programGivenAsArgument, inputFile, outputFile, eofBehaviour);
  }

  public String toString() {
    return "BrainfuckApplicationConfiguration(tapeCharset=" + this.getTapeCharset() + ", programCharset=" + this
        .getProgramCharset() + ", programGivenAsArgument=" + this.isProgramGivenAsArgument() + ", inputFile=" + this
        .getInputFile() + ", outputFile=" + this.getOutputFile() + ", eofBehaviour=" + this.getEofBehaviour() + ")";
  }
}
