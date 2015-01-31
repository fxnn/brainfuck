package de.fxnn.brainfuck.cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.io.CharStreams;
import de.fxnn.brainfuck.ProgramBuilder;
import de.fxnn.brainfuck.program.EmptyProgram;
import de.fxnn.brainfuck.program.StringProgram;
import de.fxnn.brainfuck.tape.InfiniteSignedIntegerTape;

public class BrainfuckProgramStartup implements Closeable {

  final Charset programCharset;

  final BufferedReader stdinReader;

  boolean stdoutAtItsEnd = false;

  final BufferedWriter stdoutWriter;

  final List<Closeable> closeables;

  final BrainfuckApplicationConfiguration configuration;

  public BrainfuckProgramStartup(InputStream stdinStream, OutputStream stdoutStream,
      BrainfuckApplicationConfiguration configuration) {
    this.programCharset = configuration.getProgramCharset();
    this.stdinReader = new BufferedReader(new InputStreamReader(stdinStream, configuration.getInputCharset()));
    this.stdoutWriter = new BufferedWriter(new OutputStreamWriter(stdoutStream, configuration.getOutputCharset()));
    this.configuration = configuration;
    this.closeables = new LinkedList<>();
  }

  @Override
  public void close() throws IOException {
    stdinReader.close();
    stdoutWriter.close();

    for (Closeable closeable : closeables) {
      closeable.close();
    }
  }

  public void startProgramFromCommandlineArgument(String argument) throws ProgramStartupException {

    ProgramBuilder programBuilder = createProgramBuilder() //
        .withInputReader(getStdinReader()) //
        .withOutputWriter(stdoutWriter);

    if (configuration.isProgramGivenAsArgument()) {
      programBuilder.withProgram(new StringProgram(argument));

    } else if (argument.equals("-")) {
      programBuilder //
          .withInputReader(emptyProgramInput()) // NOTE, that we can't use the input stream twice
          .withProgram(new StringProgram(readProgramFromInputStream()));

    } else {
      programBuilder //
          .withProgram(new StringProgram(readProgramFromPath(argument, programCharset)));
    }

    if (configuration.getInputFile() != null) {
      programBuilder.withInputReader(getReaderFromFile(configuration.getInputFile()));
    }
    if (configuration.getOutputFile() != null) {
      programBuilder.withOutputWriter(getWriterFromFile(configuration.getOutputFile()));
    }

    execute(programBuilder);

  }

  protected BufferedWriter getWriterFromFile(@Nonnull File outputFile) throws ProgramStartupException {
    try {
      BufferedWriter writer = Files
          .newBufferedWriter(outputFile.toPath(), configuration.getOutputCharset(),
              StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
      closeables.add(writer);
      return writer;

    } catch (IOException e) {
      throw new ProgramStartupException("Could not open file \"" + configuration.getInputFile() + "\" for output.", e);
    }
  }

  protected BufferedReader getReaderFromFile(@Nonnull File inputFile) throws ProgramStartupException {
    try {
      BufferedReader reader = Files
          .newBufferedReader(inputFile.toPath(), configuration.getInputCharset());
      closeables.add(reader);
      return reader;

    } catch (IOException e) {
      throw new ProgramStartupException("Could not open file \"" + configuration.getInputFile() + "\" for input.", e);
    }
  }

  protected void execute(ProgramBuilder programBuilder) {
    programBuilder.buildProgramExecutor().run();
  }

  protected ProgramBuilder createProgramBuilder() {
    return new ProgramBuilder() //
        .withProgram(new EmptyProgram()) //
        .withTape(new InfiniteSignedIntegerTape()) //
        .withInputReader(emptyProgramInput()) //
        .withOutputWriter(noProgramOutput());
  }

  protected String readProgramFromPath(String programPath, Charset programCharset) throws ProgramStartupException {

    try (BufferedReader fileReader = Files.newBufferedReader(Paths.get(programPath), programCharset)) {

      return CharStreams.toString(fileReader);

    } catch (IOException ex) {
      throw new ProgramStartupException("Could not read program from path \"" + programPath + "\": " + ex.getMessage(),
          ex);
    }

  }

  protected String readProgramFromInputStream() throws ProgramStartupException {

    try {

      String result = CharStreams.toString(getStdinReader());
      stdoutAtItsEnd = true;

      return result;

    } catch (IOException ex) {
      throw new ProgramStartupException("Could not read program from input stream: " + ex.getMessage(), ex);
    }

  }

  protected BufferedReader getStdinReader() throws ProgramStartupException {
    if (stdoutAtItsEnd) {
      throw new ProgramStartupException("The input was already read to its end!");
    }

    return stdinReader;
  }

  protected static BufferedWriter noProgramOutput() {
    return new BufferedWriter(CharStreams.nullWriter());
  }

  protected static BufferedReader emptyProgramInput() {
    return new BufferedReader(new StringReader(""));
  }

}
