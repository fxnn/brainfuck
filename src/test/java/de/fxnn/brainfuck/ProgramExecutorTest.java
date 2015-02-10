package de.fxnn.brainfuck;

import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.fxnn.brainfuck.interpreter.BrainfuckInterpreter;
import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuck.program.StringProgram;
import de.fxnn.brainfuck.tape.InfiniteCharacterTape;
import de.fxnn.brainfuck.tape.Tape;
import de.fxnn.brainfuck.tape.TapeEofBehaviour;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProgramExecutorTest {

  ProgramExecutor sut;

  Program program;

  Tape<?> tape;

  ByteArrayDataInput input;

  ByteArrayDataOutput output;

  @Before
  public void setUp() {
    tape = new InfiniteCharacterTape(Charsets.UTF_8, TapeEofBehaviour.THROWS);
    output = ByteStreams.newDataOutput();
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
      sut = new ProgramExecutor(program, new BrainfuckInterpreter(tape, input, output));
      sut.run();
  }

  protected void givenInput(byte... input) {
    this.input = ByteStreams.newDataInput(input);
  }

  protected void givenProgram(String brainfuckProgram) {
    program = new StringProgram(brainfuckProgram);
  }

}