package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.program.InstructionPointer;
import de.fxnn.brainfuck.tape.Tape;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Deque;

public class LoopHandlingBrainfuckInstructionSetTest {

  LoopHandlingBrainfuckInstructionSet sut;

  Deque<InstructionPointer> instructionPointerStack;

  Deque<LoopMode> loopModeStack;

  Tape<Object> tape;

  InstructionPointer instructionPointer;

  @Before
  public void setUp() {
    // NOTE, that Mock injection doesn't work properly here

    instructionPointerStack = Mockito.mock(Deque.class);
    loopModeStack = Mockito.mock(Deque.class);
    tape = Mockito.mock(Tape.class);
    sut = new LoopHandlingBrainfuckInstructionSet(instructionPointerStack, loopModeStack, tape);

    instructionPointer = Mockito.mock(InstructionPointer.class);
  }

  @Test
  public void testIncreasesInstructionPointerOnMoveForward() throws Exception {

    sut.moveForward(instructionPointer);

    Mockito.verify(instructionPointer).forward();

  }

  @Test
  public void testIncreasesInstructionPointerOnMoveBackward() throws Exception {

    sut.moveBackward(instructionPointer);

    Mockito.verify(instructionPointer).forward();

  }

  @Test
  public void testIncreasesInstructionPointerOnIncrement() throws Exception {

    sut.increment(instructionPointer);

    Mockito.verify(instructionPointer).forward();

  }

  @Test
  public void testIncreasesInstructionPointerOnDecrement() throws Exception {

    sut.decrement(instructionPointer);

    Mockito.verify(instructionPointer).forward();

  }

  @Test
  public void testIncreasesInstructionPointerOnInput() throws Exception {

    sut.input(instructionPointer);

    Mockito.verify(instructionPointer).forward();

  }

  @Test
  public void testIncreasesInstructionPointerOnOutput() throws Exception {

    sut.output(instructionPointer);

    Mockito.verify(instructionPointer).forward();

  }

  @Test
  public void testIncreasesInstructionPointerOnStartOfLoop() throws Exception {

    sut.startOfLoop(instructionPointer);

    Mockito.verify(instructionPointer).forward();

  }

  @Test
  public void testIncreasesInstructionPointerStackAfterEnteringLoop() throws Exception {

    sut.startOfLoop(instructionPointer);

    Mockito.verify(instructionPointerStack).addLast(instructionPointer);

  }

  @Test
  public void testIncreasesInstructionSetStackAfterEnteringLoop_withZeroValue() throws Exception {

    givenZeroValueOnTape();

    sut.startOfLoop(instructionPointer);

    // NOTE, that this class has subclasses and may NOT use them in a zero-value-situation
    Mockito.verify(loopModeStack).addLast(LoopMode.SKIPPED);

  }

  @Test
  public void testIncreasesInstructionSetStackAfterEnteringLoop_withNonZeroValue() throws Exception {

    givenNonZeroValueOnTape();

    sut.startOfLoop(instructionPointer);

    Mockito.verify(loopModeStack).addLast(LoopMode.EXECUTED);

  }

  @Test
  public void testDecreasesStacksAfterLeavingLoop() throws Exception {

    sut.endOfLoop(instructionPointer);

    Mockito.verify(loopModeStack).removeLast();
    Mockito.verify(instructionPointerStack).removeLast();

  }

  protected void givenZeroValueOnTape() {
    Mockito.when(tape.isZero()).thenReturn(true);
  }

  protected void givenNonZeroValueOnTape() {
    Mockito.when(tape.isZero()).thenReturn(false);
  }

}