package de.fxnn.brainfuck;

public class OutOfTapeBoundsException extends Exception {

  public OutOfTapeBoundsException() {
  }

  public OutOfTapeBoundsException(String message) {
    super(message);
  }
}
