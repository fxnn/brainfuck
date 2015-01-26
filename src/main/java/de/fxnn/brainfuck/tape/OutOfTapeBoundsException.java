package de.fxnn.brainfuck.tape;

public class OutOfTapeBoundsException extends Exception {

  public OutOfTapeBoundsException() {
  }

  public OutOfTapeBoundsException(String message) {
    super(message);
  }
}
