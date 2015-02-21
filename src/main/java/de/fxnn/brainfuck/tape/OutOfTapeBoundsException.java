package de.fxnn.brainfuck.tape;

public class OutOfTapeBoundsException extends Exception {

  public OutOfTapeBoundsException(String message) {
    super(message);
  }
}
