package de.fxnn.brainfuck.interpreter.tdd;

import de.fxnn.brainfuck.interpreter.InstructionSet;
import de.fxnn.brainfuck.interpreter.InterpreterException;
import de.fxnn.brainfuck.interpreter.tdd.MostRecentLabel.Known;
import de.fxnn.brainfuck.interpreter.tdd.MostRecentLabel.NoneYet;
import de.fxnn.brainfuck.interpreter.tdd.MostRecentLabel.StartOfLabelKnown;
import de.fxnn.brainfuck.program.InstructionPointer;
import java.io.PrintWriter;

class TddBrainfuckInstructionSet implements InstructionSet {

  private final PrintWriter output;
  private MostRecentLabel mostRecentLabel = new MostRecentLabel.NoneYet();
  private boolean withinTest = false;
  private InstructionPointer lastInstructionPointer;

  public TddBrainfuckInstructionSet(PrintWriter output) {
    this.output = output;
  }

  public InstructionPointer step(InstructionPointer instructionPointer)
      throws InterpreterException {
    switch (instructionPointer.getInstruction()) {
      case '#' -> findingLabel(instructionPointer);
      case '{' -> enteringTest(instructionPointer);
      case '}' -> leavingTest();
    };
    lastInstructionPointer = instructionPointer;
    return instructionPointer.forward();
  }

  private void leavingTest() throws InterpreterException {
    if (!withinTest || !(mostRecentLabel instanceof Known knownLabel)) {
      throw new InterpreterException("'}' without preceding '{' instruction");
    }
    withinTest = false;
    output.println("PASSED " + knownLabel.name());
  }

  private void enteringTest(InstructionPointer instructionPointer) throws InterpreterException {
    switch (mostRecentLabel) {
      case NoneYet ignored ->
          throw new InterpreterException("'{' without preceding '#' instruction");
      case StartOfLabelKnown l -> {
        var name = extractName(l.startPointer(), instructionPointer);
        mostRecentLabel = new Known(name, l.startPointer());
      }
      case Known ignored -> {
        // nothing to do
      }
    }
    withinTest = true;
  }

  private void findingLabel(InstructionPointer instructionPointer) {
    if (lastInstructionPointer == null || lastInstructionPointer.getInstruction() != '#') {
      mostRecentLabel = new StartOfLabelKnown(instructionPointer);
    }
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
