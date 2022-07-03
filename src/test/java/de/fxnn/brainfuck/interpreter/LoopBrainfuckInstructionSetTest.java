package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.program.InstructionPointer;
import de.fxnn.brainfuck.program.StringInstructionPointer;
import de.fxnn.brainfuck.tape.Tape;
import java.util.ArrayDeque;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Deque;

public class LoopBrainfuckInstructionSetTest {

  LoopBrainfuckInstructionSet sut;

  Deque<InstructionPointer> instructionPointerStack;

  Deque<LoopMode> loopModeStack;

  Tape<Object> tape;

  @Before
  public void setUp() {
    instructionPointerStack = new ArrayDeque<>();
    loopModeStack = new ArrayDeque<>();
    tape = Mockito.mock(Tape.class);
    sut = new LoopBrainfuckInstructionSet(instructionPointerStack, loopModeStack, tape);
  }

  @Test
  public void testIncreasesInstructionPointerOnStartOfLoop() throws Exception {
    var before = new StringInstructionPointer("[", 0);
    var after = sut.step(before);
    Assert.assertTrue(after.isEndOfProgram());
  }

  @Test
  public void testIncreasesInstructionPointerStackAfterEnteringLoop() throws Exception {
    var before = new StringInstructionPointer("[", 0);
    sut.step(before);
    Assert.assertEquals(before, instructionPointerStack.peekLast());
  }

  @Test
  public void testIncreasesInstructionSetStackAfterEnteringLoop_withZeroValue() throws Exception {
    givenZeroValueOnTape();
    sut.step(new StringInstructionPointer("[", 0));
    Assert.assertEquals(LoopMode.SKIPPED, loopModeStack.peekLast());
  }

  @Test
  public void testIncreasesInstructionSetStackAfterEnteringLoop_withNonZeroValue() throws Exception {
    givenNonZeroValueOnTape();
    sut.step(new StringInstructionPointer("[", 0));
    Assert.assertEquals(LoopMode.EXECUTED, loopModeStack.peekLast());
  }

  @Test
  public void testDecreasesLoopModeStackAfterLeavingLoop() throws Exception {
    loopModeStack.addLast(LoopMode.SKIPPED);
    instructionPointerStack.addLast(new StringInstructionPointer("[", 0));
    sut.step(new StringInstructionPointer("]", 0));
    Assert.assertTrue(loopModeStack.isEmpty());
  }

  @Test
  public void testDecreasesInstructionPointerStackAfterLeavingLoop() throws Exception {
    loopModeStack.addLast(LoopMode.SKIPPED);
    instructionPointerStack.addLast(new StringInstructionPointer("[", 0));
    sut.step(new StringInstructionPointer("]", 0));
    Assert.assertTrue(instructionPointerStack.isEmpty());
  }

  protected void givenZeroValueOnTape() {
    Mockito.when(tape.isZero()).thenReturn(true);
  }

  protected void givenNonZeroValueOnTape() {
    Mockito.when(tape.isZero()).thenReturn(false);
  }

}