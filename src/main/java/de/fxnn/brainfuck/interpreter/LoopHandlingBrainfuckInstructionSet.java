package de.fxnn.brainfuck.interpreter;

import java.util.Deque;

import de.fxnn.brainfuck.program.InstructionPointer;
import de.fxnn.brainfuck.tape.Tape;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoopHandlingBrainfuckInstructionSet implements BrainfuckInstructionSet {

  protected final Deque<InstructionPointer> instructionPointerStack;

  protected final Deque<LoopMode> loopModeStack;

  protected final Tape<?> tape;

  @Override
  public InstructionPointer startOfLoop(InstructionPointer instructionPointer) throws InterpreterException {

    instructionPointerStack.addLast(instructionPointer);

    if (tape.isZero()) {
      loopModeStack.addLast(LoopMode.SKIPPED);
    } else {
      loopModeStack.addLast(LoopMode.EXECUTED);
    }

    return instructionPointer.forward();

  }

  @Override
  public InstructionPointer endOfLoop(InstructionPointer instructionPointer) throws InterpreterException {

    InstructionPointer startOfLoop = instructionPointerStack.removeLast();

    if (LoopMode.EXECUTED.equals(loopModeStack.removeLast())) {
      return startOfLoop;
    }

    return instructionPointer.forward();

  }

  @Override
  public InstructionPointer moveForward(InstructionPointer instructionPointer) throws InterpreterException {
    return instructionPointer.forward();
  }

  @Override
  public InstructionPointer moveBackward(InstructionPointer instructionPointer) throws InterpreterException {
    return instructionPointer.forward();
  }

  @Override
  public InstructionPointer increment(InstructionPointer instructionPointer) throws InterpreterException {
    return instructionPointer.forward();
  }

  @Override
  public InstructionPointer decrement(InstructionPointer instructionPointer) throws InterpreterException {
    return instructionPointer.forward();
  }

  @Override
  public InstructionPointer output(InstructionPointer instructionPointer) throws InterpreterException {
    return instructionPointer.forward();
  }

  @Override
  public InstructionPointer input(InstructionPointer instructionPointer) throws InterpreterException {
    return instructionPointer.forward();
  }
}
