package de.fxnn.brainfuck.cli;

import java.io.File;
import java.nio.charset.Charset;

import javax.annotation.Nullable;

import de.fxnn.brainfuck.tape.TapeEofBehaviour;
import lombok.Data;

@Data
public class BrainfuckApplicationConfiguration {

  private Charset tapeCharset;

  private Charset programCharset;

  private boolean programGivenAsArgument;

  @Nullable
  private File inputFile;

  @Nullable
  private File outputFile;

  private TapeEofBehaviour eofBehaviour;

}
