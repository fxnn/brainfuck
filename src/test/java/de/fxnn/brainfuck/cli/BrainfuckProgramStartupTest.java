package de.fxnn.brainfuck.cli;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.io.CharStreams;
import de.fxnn.brainfuck.ProgramBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class BrainfuckProgramStartupTest {

  static final int AT_EOF = -1;

  ByteArrayInputStream inputStream;

  ByteArrayOutputStream outputStream;

  BrainfuckApplicationConfiguration configuration;

  BrainfuckProgramStartup sut;

  ProgramBuilder programBuilder;

  @Before
  public void setUp() throws Exception {
    configuration = new BrainfuckApplicationConfiguration();

    inputStream = new ByteArrayInputStream(new byte[0]);
    outputStream = new ByteArrayOutputStream();
  }

  @Test
  public void testCantReadFromInputWhenProgramIsReadFromInput() throws Exception{

    givenDefaultCharsets();
    givenInputString("123");

    whenSutIsInvoked("-");

    Assert.assertEquals(AT_EOF, programBuilder.getInputReader().read());

  }

  @Test
  public void testCanReadFromInputWhenProgramIsReadFromArguments() throws Exception{

    givenDefaultCharsets();
    givenInputString("123");
    configuration.setProgramGivenAsArgument(true);

    whenSutIsInvoked("-");

    Assert.assertThat(programBuilder.getInputReader().read(), greaterThan(AT_EOF));

  }

  @Test
  public void testReadsProgramFromFile() throws Exception {

    File file = testFile("program");

    givenDefaultCharsets();
    givenFileContents(file, configuration.getProgramCharset(), "+++ + +++ [ - > +++ + +++ < ] > .");

    whenSutIsInvoked(file.getAbsolutePath());
    whenProgramIsExecuted();
    whenSutIsClosed();

    Assert.assertThat(outputStreamContents(), equalTo("1"));

  }

  @Test
  public void testWritesToFileOutput() throws Exception {

    File file = testFile("out");

    givenDefaultCharsets();
    givenInputString("87");
    configuration.setProgramGivenAsArgument(true);
    configuration.setOutputFile(file);

    whenSutIsInvoked(",.,.");
    whenProgramIsExecuted();
    whenSutIsClosed();

    Assert.assertThat(fileContents(file, configuration.getOutputCharset()), equalTo("87"));

  }

  @Test
  public void testReadsFromFileInput() throws Exception {

    File file = testFile("in");

    givenDefaultCharsets();
    givenFileContents(file, configuration.getInputCharset(), "42");
    configuration.setProgramGivenAsArgument(true);
    configuration.setInputFile(file);

    whenSutIsInvoked(",.,.");
    whenProgramIsExecuted();
    whenSutIsClosed();

    Assert.assertThat(outputStreamContents(), equalTo("42"));

  }

  protected String outputStreamContents() {
    return new String(outputStream.toByteArray());
  }

  protected String fileContents(File file, Charset charset) throws IOException {
    return CharStreams.toString(Files.newBufferedReader(file.toPath(), charset));
  }

  protected void givenFileContents(File file, Charset charset, String contents) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), charset)) {
      writer.write(contents);
    }
  }

  protected void whenSutIsInvoked(String argument) throws ProgramStartupException {
    sut = new BrainfuckProgramStartup(inputStream, outputStream, configuration) {

      @Override
      protected void execute(ProgramBuilder programBuilder) {
        BrainfuckProgramStartupTest.this.programBuilder = programBuilder;
      }

    };
    sut.startProgramFromCommandlineArgument(argument);
  }

  protected void whenProgramIsExecuted() {
    programBuilder.buildProgramExecutor().run();
  }

  protected void whenSutIsClosed() throws IOException {
    sut.close();
  }

  protected void givenInputString(String input) {
    inputStream = new ByteArrayInputStream(input.getBytes(configuration.getInputCharset()));
  }

  protected void givenDefaultCharsets() {
    configuration.setProgramCharset(Charset.defaultCharset());
    configuration.setInputCharset(Charset.defaultCharset());
    configuration.setOutputCharset(Charset.defaultCharset());
  }

  protected File testFile(String fileName) throws IOException {
    Path testDirectory = Paths.get("target", "test-output");
    if (!Files.isDirectory(testDirectory)) {
      Files.createDirectories(testDirectory);
    }

    return new File(testDirectory.toFile(), fileName);
  }
}