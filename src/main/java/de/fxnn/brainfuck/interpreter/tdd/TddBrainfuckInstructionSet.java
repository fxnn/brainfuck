package de.fxnn.brainfuck.interpreter.tdd;

import de.fxnn.brainfuck.interpreter.InstructionSet;
import de.fxnn.brainfuck.interpreter.InterpreterException;
import de.fxnn.brainfuck.interpreter.tdd.State.LabelKnown;
import de.fxnn.brainfuck.interpreter.tdd.State.NoLabelSeen;
import de.fxnn.brainfuck.interpreter.tdd.State.StartOfLabelKnown;
import de.fxnn.brainfuck.interpreter.tdd.State.WithinTest;
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
      case '#' -> findingLabel(instructionPointer);
      case '{' -> enteringTest(instructionPointer);
      case '}' -> leavingTest();
    }
    ;
    return instructionPointer.forward();
  }

  private void leavingTest() throws InterpreterException {
    if (!(state instanceof WithinTest s)) {
      throw new InterpreterException("'}' without preceding '{' instruction");
    }
    output.println("PASSED " + s.labelName());
  }

  private void enteringTest(InstructionPointer instructionPointer) throws InterpreterException {
    switch (state) {
      case StartOfLabelKnown l -> {
        var name = extractName(l.labelPointer(), instructionPointer);
        state = new WithinTest(name, l.labelPointer());
      }
      case LabelKnown l -> state = new WithinTest(l.labelName(), l.labelPointer());
      default -> throw new InterpreterException(
          "Unexpected instruction '" + instructionPointer.getInstruction() + "' (in state '" + state
              + "')");
    }
  }

  private void findingLabel(InstructionPointer instructionPointer) {
    if (!(state instanceof StartOfLabelKnown startOfLabel) || !isSameLabel(
        startOfLabel.labelPointer(), instructionPointer)) {
      state = new StartOfLabelKnown(instructionPointer);
    }
  }

  private boolean isSameLabel(InstructionPointer startPointer, InstructionPointer endPointer) {
    InstructionPointer currentPointer = startPointer;
    while (!currentPointer.isEndOfProgram() && currentPointer.getInstruction() == '#') {
      if (endPointer.equals(currentPointer)) {
        return true;
      }
      currentPointer = currentPointer.forward();
    }
    return false;
  }

  private String extractName(InstructionPointer start, InstructionPointer max) {
    var nameBuilder = new StringBuilder();
    var current = start;
    while (nameBuilder.length() < 20 && !current.equals(max)) {
      nameBuilder.append(current.getInstruction());
      current = current.forward();
    }
    return nameBuilder.toString();
  }
}
