package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.program.InstructionPointer;
import de.fxnn.brainfuck.tape.OutOfTapeBoundsException;
import de.fxnn.brainfuck.tape.Tape;
import de.fxnn.brainfuck.tape.TapeIOException;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.Deque;

record RegularBrainfuckInstructionSet(Tape<?> tape, DataInput input, DataOutput output) implements
    InstructionSet {

  @Override
  public InstructionPointer step(InstructionPointer instructionPointer)
      throws InterpreterException {
    return switch (instructionPointer.getInstruction()) {
      case '>' -> moveForward(instructionPointer);
      case '<' -> moveBackward(instructionPointer);
      case '+' -> increment(instructionPointer);
      case '-' -> decrement(instructionPointer);
      case '.' -> output(instructionPointer);
      case ',' -> input(instructionPointer);
      // HINT: we just forward the IP, because Brainfuck ignores any unknown characters
      default -> instructionPointer.forward();
    };
  }

  private InstructionPointer moveForward(InstructionPointer instructionPointer)
      throws InterpreterException {
    try {
      tape.moveForward();
      return instructionPointer.forward();
    } catch (OutOfTapeBoundsException e) {
      throw new InterpreterException("End of tape reached while moving forward", e);
    }
  }

  private InstructionPointer moveBackward(InstructionPointer instructionPointer)
      throws InterpreterException {
    try {
      tape.moveBackward();
      return instructionPointer.forward();
    } catch (OutOfTapeBoundsException e) {
      throw new InterpreterException("End of tape reached while moving backward", e);
    }
  }

  private InstructionPointer increment(InstructionPointer instructionPointer)
      throws InterpreterException {
    tape.increment();
    return instructionPointer.forward();
  }

  private InstructionPointer decrement(InstructionPointer instructionPointer)
      throws InterpreterException {
    tape.decrement();
    return instructionPointer.forward();
  }

  private InstructionPointer output(InstructionPointer instructionPointer)
      throws InterpreterException {
    try {
      tape.readTo(output);
      return instructionPointer.forward();
    } catch (TapeIOException e) {
      throw new InterpreterException("Sending tape contents to output failed", e);
    }
  }

  private InstructionPointer input(InstructionPointer instructionPointer)
      throws InterpreterException {
    try {
      tape.writeFrom(input);
      return instructionPointer.forward();
    } catch (TapeIOException e) {
      throw new InterpreterException("Reading input to tape failed", e);
    }
  }
}
