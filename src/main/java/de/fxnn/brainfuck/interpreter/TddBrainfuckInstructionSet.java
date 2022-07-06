package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.interpreter.MostRecentLabel.Known;
import de.fxnn.brainfuck.interpreter.MostRecentLabel.NoneYet;
import de.fxnn.brainfuck.interpreter.MostRecentLabel.StartOfLabelKnown;
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
    var nextInstructionPointer = switch (instructionPointer.getInstruction()) {
      case '#' -> {
        if (lastInstructionPointer == null || lastInstructionPointer.getInstruction() != '#') {
          mostRecentLabel = new StartOfLabelKnown(instructionPointer);
        }
        yield instructionPointer.forward();
      }
      case '{' -> {
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
        yield instructionPointer.forward();
      }
      case '}' -> {
        if (withinTest && mostRecentLabel instanceof Known knownLabel) {
          withinTest = false;
          output.println("PASSED " + knownLabel.name());
          yield instructionPointer.forward();
        }
        throw new InterpreterException("'}' without preceding '{' instruction");
      }
      default -> instructionPointer.forward();
    };
    lastInstructionPointer = instructionPointer;
    return nextInstructionPointer;
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
