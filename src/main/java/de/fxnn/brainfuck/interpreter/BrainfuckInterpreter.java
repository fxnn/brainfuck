package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.program.InstructionPointer;
import de.fxnn.brainfuck.tape.Tape;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.ArrayDeque;
import java.util.Deque;

public class BrainfuckInterpreter implements Interpreter {

  final Deque<InstructionPointer> instructionPointerStack;

  final Deque<LoopMode> loopModeStack;

  final RegularBrainfuckInstructionSet regularBrainfuckInstructionSet;

  final LoopBrainfuckInstructionSet loopBrainfuckInstructionSet;

  public BrainfuckInterpreter(Tape<?> tape, DataInput input, DataOutput output) {
    this.instructionPointerStack = new ArrayDeque<>();
    this.loopModeStack = new ArrayDeque<>();
    regularBrainfuckInstructionSet = new RegularBrainfuckInstructionSet(tape, input,
        output);
    loopBrainfuckInstructionSet = new LoopBrainfuckInstructionSet(instructionPointerStack,
        loopModeStack, tape);
  }

  @Override
  public InstructionPointer step(InstructionPointer instructionPointer)
      throws InterpreterException {
    if (isSkipLoop() || loopBrainfuckInstructionSet.isLoopInstruction(
        instructionPointer)) {
      return loopBrainfuckInstructionSet.step(instructionPointer);
    }

    return regularBrainfuckInstructionSet.step(instructionPointer);

  }

  protected boolean isSkipLoop() {
    return LoopMode.SKIPPED.equals(loopModeStack.peekLast());
  }

}
