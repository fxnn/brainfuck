package de.fxnn.brainfuck;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import de.fxnn.brainfuck.simple.BrainfuckInterpreter;
import de.fxnn.brainfuck.simple.InfiniteSignedIntegerTape;
import de.fxnn.brainfuck.simple.StringSourcedProgram;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProgramExecutorTest {

  ProgramExecutor sut;

  Program program;

  Tape<?> tape;

  ByteArrayInputStream input;

  ByteArrayOutputStream output;

  @Before
  public void setUp() {
    tape = new InfiniteSignedIntegerTape();
    output = new ByteArrayOutputStream();
    givenInput();
  }

  @Test
  public void testOutput() throws Exception {

    givenProgram("+.");

    whenProgramIsExecuted();

    assertOutputEquals((byte) 1);

  }

  @Test
  public void testLoopNotEntered() throws Exception {

    givenProgram("[.]");

    whenProgramIsExecuted();

    assertOutputEquals();

  }

  @Test
  public void testLoopEntered() throws Exception {

    givenProgram("+[.-]");

    whenProgramIsExecuted();

    assertOutputEquals((byte) 1);

  }

  @Test
  public void testMovement() throws Exception {

    givenProgram("+++>++>+>.<.<.<.");

    whenProgramIsExecuted();

    assertOutputEquals((byte) 0, (byte) 1, (byte) 2, (byte) 3);

  }

  protected void assertOutputEquals(byte... expectedOutput) {
    Assert.assertArrayEquals(expectedOutput, output.toByteArray());
  }

  protected void whenProgramIsExecuted() throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(input));
         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output))) {
      sut = new ProgramExecutor(program, new BrainfuckInterpreter(tape, reader, writer));
      sut.run();
    }
  }

  protected void givenInput(byte... input) {
    this.input = new ByteArrayInputStream(input);
  }

  protected void givenProgram(String brainfuckProgram) {
    program = new StringSourcedProgram(brainfuckProgram);
  }

}