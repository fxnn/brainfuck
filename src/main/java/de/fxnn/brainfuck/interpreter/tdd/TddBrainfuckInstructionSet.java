package de.fxnn.brainfuck.interpreter.tdd;

import de.fxnn.brainfuck.interpreter.InstructionSet;
import de.fxnn.brainfuck.interpreter.InterpreterException;
import de.fxnn.brainfuck.interpreter.tdd.State.NoLabelSeen;
import de.fxnn.brainfuck.program.InstructionPointer;
import java.io.PrintWriter;

class TddBrainfuckInstructionSet implements InstructionSet {

  private final PrintWriter output;
  private State state = new NoLabelSeen();

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
    switch (instructionPointer.getInstruction()) {
      case '}' -> state = state.leavingTest(instructionPointer,
          labelName -> output.println("PASSED " + labelName));
    }
  }

  private void stepOutsideTest(InstructionPointer instructionPointer) throws InterpreterException {
    switch (instructionPointer.getInstruction()) {
      case '#' -> state = state.findingLabel(instructionPointer);
      case '{' -> state = state.enteringTest(instructionPointer);
    }
  }

}
