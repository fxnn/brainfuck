package de.fxnn.brainfuck.cli;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import de.fxnn.brainfuck.ProgramBuilder;
import de.fxnn.brainfuck.program.EmptyProgram;
import de.fxnn.brainfuck.program.StringProgram;
import de.fxnn.brainfuck.tape.InfiniteCharacterTape;

public class BrainfuckProgramStartup implements Closeable {

  final Charset programCharset;

  final Charset tapeCharset;

  final InputStream stdinStream;

  boolean stdinAtItsEnd = false;

  final OutputStream stdoutStream;

  final List<Closeable> closeables;

  final BrainfuckApplicationConfiguration configuration;

  public BrainfuckProgramStartup(InputStream stdinStream, OutputStream stdoutStream,
      BrainfuckApplicationConfiguration configuration) {
    this.programCharset = configuration.getProgramCharset();
    this.tapeCharset = configuration.getTapeCharset();
    this.stdinStream = stdinStream;
    this.stdoutStream = stdoutStream;
    this.configuration = configuration;
    this.closeables = new LinkedList<>();
  }

  @Override
  public void close() throws IOException {
    stdinStream.close();
    stdoutStream.close();

    for (Closeable closeable : closeables) {
      closeable.close();
    }
  }

  public void startProgramFromCommandlineArgument(String argument) throws ProgramStartupException {

    ProgramBuilder programBuilder = createProgramBuilder(tapeCharset) //
        .withInput(getDataInputFromStdin()) //
        .withOutput(getDataOutputFromStdout());

    if (configuration.isProgramGivenAsArgument()) {
      programBuilder.withProgram(new StringProgram(argument));

    } else if (argument.equals("-")) {
      programBuilder //
          .withInput(emptyProgramInput()) // NOTE, that we can't use the input stream twice
          .withProgram(new StringProgram(readProgramFromInputStream(programCharset)));

    } else {
      programBuilder //
          .withProgram(new StringProgram(readProgramFromPath(argument, programCharset)));
    }

    if (configuration.getInputFile() != null) {
      programBuilder.withInput(getDataInputFromFile(configuration.getInputFile()));
    }
    if (configuration.getOutputFile() != null) {
      programBuilder.withOutput(getDataOutputFromFile(configuration.getOutputFile()));
    }

    execute(programBuilder);

  }

  protected DataOutput getDataOutputFromFile(@Nonnull File outputFile) throws ProgramStartupException {
    try {
      OutputStream outputStream = Files
          .newOutputStream(outputFile.toPath(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING,
              StandardOpenOption.CREATE);

      DataOutputStream result = new DataOutputStream(outputStream);
      closeables.add(result);

      return result;

    } catch (IOException e) {
      throw new ProgramStartupException("Could not open file \"" + configuration.getInputFile() + "\" for output.", e);
    }
  }

  protected DataInput getDataInputFromFile(@Nonnull File inputFile) throws ProgramStartupException {
    try {
      InputStream inputStream = Files.newInputStream(inputFile.toPath());

      DataInputStream result = new DataInputStream(inputStream);
      closeables.add(result);

      return result;

    } catch (IOException e) {
      throw new ProgramStartupException("Could not open file \"" + configuration.getInputFile() + "\" for input.", e);
    }
  }

  protected void execute(ProgramBuilder programBuilder) {
    programBuilder.buildProgramExecutor().run();
  }

  protected ProgramBuilder createProgramBuilder(Charset tapeCharset) {
    return new ProgramBuilder() //
        .withProgram(new EmptyProgram()) //
        .withTape(new InfiniteCharacterTape(tapeCharset, configuration.getEofBehaviour())) //
        .withInput(emptyProgramInput()) //
        .withOutput(noProgramOutput());
  }

  protected String readProgramFromPath(String programPath, Charset programCharset) throws ProgramStartupException {

    try (BufferedReader fileReader = Files.newBufferedReader(Paths.get(programPath), programCharset)) {

      return CharStreams.toString(fileReader);

    } catch (IOException ex) {
      throw new ProgramStartupException("Could not read program from path \"" + programPath + "\": " + ex.getMessage(),
          ex);
    }

  }

  protected String readProgramFromInputStream(Charset programCharset) throws ProgramStartupException {

    try {

      String result = CharStreams.toString(new BufferedReader(new InputStreamReader(stdinStream, programCharset)));
      stdinAtItsEnd = true;

      return result;

    } catch (IOException ex) {
      throw new ProgramStartupException("Could not read program from input stream: " + ex.getMessage(), ex);
    }

  }

  private DataOutput getDataOutputFromStdout() {
    return new DataOutputStream(stdoutStream);
  }

  protected DataInput getDataInputFromStdin() throws ProgramStartupException {
    if (stdinAtItsEnd) {
      throw new ProgramStartupException("The input was already read to its end!");
    }

    return new DataInputStream(stdinStream);
  }

  protected static DataOutput noProgramOutput() {
    return new DataOutputStream(ByteStreams.nullOutputStream());
  }

  protected static DataInput emptyProgramInput() {
    return ByteStreams.newDataInput(new byte[0]);
  }

}
