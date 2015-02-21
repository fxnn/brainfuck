package de.fxnn.brainfuck.interpreter;

public class InterpreterException extends Exception {

  public InterpreterException(String message) {
    super(message);
  }

  public InterpreterException(String message, Throwable cause) {
    super(message, cause);
  }

}
