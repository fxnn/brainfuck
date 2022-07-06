package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.program.InstructionPointer;

public class InterpreterException extends Exception {

  public static InterpreterException unsupportedInstruction(InstructionPointer instructionPointer) {
    return new InterpreterException(
        "unsupported instruction " + instructionPointer.getInstruction());
  }

  public InterpreterException(String message) {
    super(message);
  }

  public InterpreterException(String message, Throwable cause) {
    super(message, cause);
  }

}
