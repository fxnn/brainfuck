package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.program.InstructionPointer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

class TddBrainfuckInstructionSet {

  private final PrintWriter output;
  private boolean label = false;
  private boolean beginOfTest = false;

  public TddBrainfuckInstructionSet(PrintWriter output) {
    this.output = output;
  }

  public InstructionPointer step(InstructionPointer instructionPointer)
      throws InterpreterException {
    return switch (instructionPointer.getInstruction()) {
      case '#' -> {
        label = true;
        yield instructionPointer.forward();
      }
      case '{' -> {
        if (label) {
          beginOfTest = true;
          yield instructionPointer.forward();
        }
        throw new InterpreterException("'{' without preceding '#' instruction");
      }
      case '}' -> {
        if (beginOfTest) {
          beginOfTest = false;
          output.println("PASSED");
          yield instructionPointer.forward();
        }
        throw new InterpreterException("'}' without preceding '{' instruction");
      }
      default -> instructionPointer.forward();
    };
  }
}
