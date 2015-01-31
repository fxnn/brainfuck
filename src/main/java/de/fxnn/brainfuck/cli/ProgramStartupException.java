package de.fxnn.brainfuck.cli;

public class ProgramStartupException extends Exception {

  public ProgramStartupException() {
  }

  public ProgramStartupException(String message) {
    super(message);
  }

  public ProgramStartupException(String message, Throwable cause) {
    super(message, cause);
  }

  public ProgramStartupException(Throwable cause) {
    super(cause);
  }

  public ProgramStartupException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
