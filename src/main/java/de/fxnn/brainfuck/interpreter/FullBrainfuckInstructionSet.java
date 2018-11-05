package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.program.InstructionPointer;
import de.fxnn.brainfuck.tape.OutOfTapeBoundsException;
import de.fxnn.brainfuck.tape.Tape;
import de.fxnn.brainfuck.tape.TapeIOException;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.Deque;

public class FullBrainfuckInstructionSet extends LoopHandlingBrainfuckInstructionSet {

  protected final DataInput input;

  protected final DataOutput output;

  public FullBrainfuckInstructionSet(Deque<InstructionPointer> instructionPointerStack,
      Deque<LoopMode> loopModeStack, Tape<?> tape, DataInput input, DataOutput output) {
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
    } catch (TapeIOException e) {
      throw new InterpreterException("Sending tape contents to output failed", e);
    }
  }

  @Override
  public InstructionPointer input(InstructionPointer instructionPointer) throws InterpreterException {
    try {
      tape.writeFrom(input);
      return super.input(instructionPointer);
    } catch (TapeIOException e) {
      throw new InterpreterException("Reading input to tape failed", e);
    }
  }
}
