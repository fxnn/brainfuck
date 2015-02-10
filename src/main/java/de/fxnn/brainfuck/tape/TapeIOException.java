package de.fxnn.brainfuck.tape;

public class TapeIOException extends Exception {

  public TapeIOException() {
  }

  public TapeIOException(String message) {
    super(message);
  }

  public TapeIOException(String message, Throwable cause) {
    super(message, cause);
  }

  public TapeIOException(Throwable cause) {
    super(cause);
  }

  public TapeIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
