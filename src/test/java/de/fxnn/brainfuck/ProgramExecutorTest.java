package de.fxnn.brainfuck;

import com.google.common.base.Charsets;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.fxnn.brainfuck.interpreter.BrainfuckInterpreter;
import de.fxnn.brainfuck.interpreter.Interpreter;
import de.fxnn.brainfuck.interpreter.InterpreterException;
import de.fxnn.brainfuck.program.InstructionPointer;
import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuck.program.StringProgram;
import de.fxnn.brainfuck.tape.InfiniteCharacterTape;
import de.fxnn.brainfuck.tape.Tape;
import de.fxnn.brainfuck.tape.TapeEofBehaviour;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProgramExecutorTest {

  ProgramExecutor sut;

  Program program;

  Tape<?> tape;

  ByteArrayDataInput input;

  ByteArrayDataOutput output;

  Thread thread;

  AtomicReference<Throwable> throwable = new AtomicReference<>();

  @Before
  public void setUp() {
    tape = new InfiniteCharacterTape(Charsets.UTF_8, TapeEofBehaviour.THROWS);
    output = ByteStreams.newDataOutput();
    givenInput();
  }

  @Test
  public void testOutput() throws Exception {

    givenProgram("+.");

    whenBrainfuckProgramIsExecuted();

    assertOutputEquals((byte) 1);

  }

  @Test
  public void testLoopNotEntered() throws Exception {

    givenProgram("[.]");

    whenBrainfuckProgramIsExecuted();

    assertOutputEquals();

  }

  @Test
  public void testLoopEntered() throws Exception {

    givenProgram("+[.-]");

    whenBrainfuckProgramIsExecuted();

    assertOutputEquals((byte) 1);

  }

  @Test
  public void testMovement() throws Exception {

    givenProgram("+++>++>+>.<.<.<.");

    whenBrainfuckProgramIsExecuted();

    assertOutputEquals((byte) 0, (byte) 1, (byte) 2, (byte) 3);

  }

  @Test
  public void testThreadInterruptThrowsProgramExecutionException() throws Exception {

    givenProgramWithInfiniteLoop();

    whenBrainfuckProgramIsExecutedConcurrently();

    thread.interrupt();
    thread.join();

    assertNotNull(throwable.get());
    assertEquals(ProgramExecutionException.class, throwable.get().getClass());

  }

  @Test(expected = ProgramExecutionException.class)
  public void testInterpreterExceptionCausesProgramExecutionException() throws Exception {

    sut = new ProgramExecutor(new StringProgram("+"), new Interpreter() {

      @Override
      public InstructionPointer step(InstructionPointer instruction) throws InterpreterException {
        throw new InterpreterException("Thrown by test");
      }
    });

    sut.run();

  }

  protected void assertOutputEquals(byte... expectedOutput) {
    Assert.assertArrayEquals(expectedOutput, output.toByteArray());
  }

  protected void whenBrainfuckProgramIsExecuted() throws IOException {
    sut = createSutWithBrainfuckProgram();
    sut.run();
  }

  protected void whenBrainfuckProgramIsExecutedConcurrently() throws IOException {
    sut = createSutWithBrainfuckProgram();
    thread = new Thread(sut);
    thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

      @Override
      public void uncaughtException(Thread t, Throwable e) {
        throwable.set(e);
      }
    });
    thread.start();
  }

  protected ProgramExecutor createSutWithBrainfuckProgram() {
    return new ProgramExecutor(program, new BrainfuckInterpreter(tape, input, output));
  }

  protected void givenInput(byte... input) {
    this.input = ByteStreams.newDataInput(input);
  }

  protected void givenProgramWithInfiniteLoop() {
    givenProgram("+[]");
  }

  protected void givenProgram(String brainfuckProgram) {
    program = new StringProgram(brainfuckProgram);
  }

}