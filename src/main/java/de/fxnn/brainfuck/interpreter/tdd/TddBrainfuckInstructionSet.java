package de.fxnn.brainfuck.interpreter.tdd;

import de.fxnn.brainfuck.interpreter.InstructionSet;
import de.fxnn.brainfuck.interpreter.InterpreterException;
import de.fxnn.brainfuck.interpreter.tdd.State.HandlesEvent;
import de.fxnn.brainfuck.interpreter.tdd.State.NoLabelSeen;
import de.fxnn.brainfuck.program.InstructionPointer;
import de.fxnn.brainfuck.tape.InfiniteCharacterTape;
import de.fxnn.brainfuck.tape.Tape;
import de.fxnn.brainfuck.tape.TapeEofBehaviour;
import java.io.PrintWriter;
import java.nio.charset.Charset;

class TddBrainfuckInstructionSet implements InstructionSet {

  private final PrintWriter output;
  private State state = new NoLabelSeen(new StateEventHandler());

  public TddBrainfuckInstructionSet(PrintWriter output) {
    this.output = output;
  }

  public InstructionPointer step(InstructionPointer instructionPointer)
      throws InterpreterException {
    if (state.isOutsideTest()) {
      stepOutsideTest(instructionPointer);
    } else if (state.isWithinTest()) {
      stepWithinTest(instructionPointer);
    }
    return instructionPointer.forward();
  }

  private void stepWithinTest(InstructionPointer instructionPointer) throws InterpreterException {
    if (instructionPointer.getInstruction() == '}') {
      state = state.leavingTest(instructionPointer);
      return;
    }
    if (instructionPointer.getInstruction() == '~') {
      state = state.enteringAssertion(instructionPointer);
      return;
    }
    if (instructionPointer.isInstructionWithin('0', '9')) {
      state = state.readDigit(instructionPointer);
    }
  }

  private void stepOutsideTest(InstructionPointer instructionPointer) throws InterpreterException {
    switch (instructionPointer.getInstruction()) {
      case '#' -> state = state.findingLabel(instructionPointer);
      case '{' -> state = state.enteringTest(instructionPointer, createTape());
    }
  }

  private Tape<?> createTape() {
    return new InfiniteCharacterTape(Charset.defaultCharset(), TapeEofBehaviour.READS_ZERO);
  }

  private class StateEventHandler implements State.HandlesEvent {

    @Override
    public void testPassed(String labelName) {
      output.println("PASSED " + labelName);
    }

    @Override
    public void testFailed(String labelName) {
      output.println("FAILED " + labelName);
    }
  }

}
