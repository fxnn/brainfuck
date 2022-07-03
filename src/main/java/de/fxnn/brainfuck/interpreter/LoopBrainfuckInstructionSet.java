package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.program.InstructionPointer;
import de.fxnn.brainfuck.tape.Tape;

import java.util.Deque;

record LoopBrainfuckInstructionSet(Deque<InstructionPointer> instructionPointerStack,
                                          Deque<LoopMode> loopModeStack, Tape<?> tape) implements
    InstructionSet {

  public boolean isLoopInstruction(InstructionPointer instructionPointer) {
    var instruction = instructionPointer.getInstruction();
    return instruction == '[' || instruction == ']';
  }

  @Override
  public InstructionPointer step(InstructionPointer instructionPointer)
      throws InterpreterException {
    return switch (instructionPointer.getInstruction()) {
      case '[' -> startOfLoop(instructionPointer);
      case ']' -> endOfLoop(instructionPointer);
      // HINT: just forward the IP in case of an unknown instruction, because Brainfuck is tolerant
      //   and also because this InstructionSet is used when a loop shall be skipped
      default -> instructionPointer.forward();
    };
  }

  private InstructionPointer startOfLoop(InstructionPointer instructionPointer)
      throws InterpreterException {

    instructionPointerStack.addLast(instructionPointer);

    if (tape.isZero()) {
      loopModeStack.addLast(LoopMode.SKIPPED);
    } else {
      loopModeStack.addLast(LoopMode.EXECUTED);
    }

    return instructionPointer.forward();

  }

  private InstructionPointer endOfLoop(InstructionPointer instructionPointer)
      throws InterpreterException {

    InstructionPointer startOfLoop = instructionPointerStack.removeLast();

    if (LoopMode.EXECUTED.equals(loopModeStack.removeLast())) {
      return startOfLoop;
    }

    return instructionPointer.forward();

  }
}
