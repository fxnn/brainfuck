package de.fxnn.brainfuck.interpreter.tdd;

import de.fxnn.brainfuck.interpreter.InstructionSet;
import de.fxnn.brainfuck.interpreter.InterpreterException;
import de.fxnn.brainfuck.interpreter.tdd.State.NoLabelSeen;
import de.fxnn.brainfuck.interpreter.tdd.State.StartOfLabelKnown;
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
    switch (instructionPointer.getInstruction()) {
      case '#' -> state = state.findingLabel(instructionPointer);
      case '{' -> state = state.enteringTest(instructionPointer);
      case '}' -> state = state.leavingTest(instructionPointer,
          labelName -> output.println("PASSED " + labelName));
    }
    return instructionPointer.forward();
  }

}
