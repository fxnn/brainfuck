package de.fxnn.brainfuck.simple;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Deque;

import de.fxnn.brainfuck.InstructionPointer;
import de.fxnn.brainfuck.InterpreterException;
import de.fxnn.brainfuck.OutOfTapeBoundsException;
import de.fxnn.brainfuck.Tape;

public class FullBrainfuckInstructionSet extends LoopHandlingBrainfuckInstructionSet {

  protected final BufferedReader input;

  protected final BufferedWriter output;

  public FullBrainfuckInstructionSet(Deque<InstructionPointer> instructionPointerStack,
      Deque<BrainfuckLoopMode> loopModeStack, Tape<?> tape, BufferedReader input, BufferedWriter output) {
    super(instructionPointerStack, loopModeStack, tape);
    this.input = input;
    this.output = output;
  }

  @Override
  public InstructionPointer moveForward(InstructionPointer instructionPointer) throws InterpreterException {
    try {
      tape.moveForward();
      return super.moveForward(instructionPointer);
    } catch (OutOfTapeBoundsException e) {
      throw new InterpreterException("End of tape reached while moving forward", e);
    }
  }

  @Override
  public InstructionPointer moveBackward(InstructionPointer instructionPointer) throws InterpreterException {
    try {
      tape.moveBackward();
      return super.moveBackward(instructionPointer);
    } catch (OutOfTapeBoundsException e) {
      throw new InterpreterException("End of tape reached while moving backward", e);
    }
  }

  @Override
  public InstructionPointer increment(InstructionPointer instructionPointer) throws InterpreterException {
    tape.increment();
    return super.increment(instructionPointer);
  }

  @Override
  public InstructionPointer decrement(InstructionPointer instructionPointer) throws InterpreterException {
    tape.decrement();
    return super.decrement(instructionPointer);
  }

  @Override
  public InstructionPointer output(InstructionPointer instructionPointer) throws InterpreterException {
    try {
      tape.readTo(output);
      return super.output(instructionPointer);
    } catch (IOException e) {
      throw new InterpreterException("Sending tape contents to output failed", e);
    }
  }

  @Override
  public InstructionPointer input(InstructionPointer instructionPointer) throws InterpreterException {
    try {
      tape.writeFrom(input);
      return super.input(instructionPointer);
    } catch (IOException e) {
      throw new InterpreterException("Reading input to tape failed", e);
    }
  }
}
