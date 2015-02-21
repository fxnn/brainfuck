package de.fxnn.brainfuck.cli;

public class ProgramStartupException extends Exception {

  public ProgramStartupException(String message) {
    super(message);
  }

  public ProgramStartupException(String message, Throwable cause) {
    super(message, cause);
  }

}
