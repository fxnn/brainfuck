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

  final FullBrainfuckInstructionSet fullBrainfuckInstructionSet;

  final LoopHandlingBrainfuckInstructionSet loopHandlingBrainfuckInstructionSet;

  public BrainfuckInterpreter(Tape<?> tape, DataInput input, DataOutput output) {
    this.instructionPointerStack = new ArrayDeque<>();
    this.loopModeStack = new ArrayDeque<>();
    fullBrainfuckInstructionSet = new FullBrainfuckInstructionSet(instructionPointerStack, loopModeStack, tape, input,
        output);
    loopHandlingBrainfuckInstructionSet = new LoopHandlingBrainfuckInstructionSet(instructionPointerStack,
        loopModeStack, tape);
  }

  @Override
  public InstructionPointer step(InstructionPointer instructionPointer) throws InterpreterException {
    BrainfuckInstruction instruction = BrainfuckInstruction.fromCharacter(instructionPointer.getInstruction());

    if (instruction != null) {
      if (isExecuteCurrentLoopOrNoLoopPresent()) {
        return instruction.step(instructionPointer, fullBrainfuckInstructionSet);
      }

      return instruction.step(instructionPointer, loopHandlingBrainfuckInstructionSet);
    }

    return instructionPointer.forward();
  }

  protected boolean isExecuteCurrentLoopOrNoLoopPresent() {
    return loopModeStack.isEmpty() || LoopMode.EXECUTED.equals(loopModeStack.getLast());
  }

}
