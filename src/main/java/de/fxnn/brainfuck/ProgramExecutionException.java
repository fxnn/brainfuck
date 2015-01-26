package de.fxnn.brainfuck;

public class ProgramExecutionException extends RuntimeException {

  public ProgramExecutionException() {
  }

  public ProgramExecutionException(String message) {
    super(message);
  }

  public ProgramExecutionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ProgramExecutionException(Throwable cause) {
    super(cause);
  }

  public ProgramExecutionException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
