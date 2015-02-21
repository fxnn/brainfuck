package de.fxnn.brainfuck.tape;

public class TapeIOException extends Exception {

  public TapeIOException(String message) {
    super(message);
  }

  public TapeIOException(String message, Throwable cause) {
    super(message, cause);
  }

}
