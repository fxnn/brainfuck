package de.fxnn.brainfuck.interpreter.tdd;

import de.fxnn.brainfuck.interpreter.InterpreterException;

public class UnexpectedInstructionException extends InterpreterException {

  public UnexpectedInstructionException(char instruction, State state) {
    super("Unexpected instruction '" + instruction + "' (in state '" + state
        + "')");
  }
}
