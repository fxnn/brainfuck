package de.fxnn.brainfuck.simple;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

import de.fxnn.brainfuck.InstructionPointer;
import de.fxnn.brainfuck.Interpreter;
import de.fxnn.brainfuck.InterpreterException;
import de.fxnn.brainfuck.Tape;

public class BrainfuckInterpreter implements Interpreter {

  final Deque<InstructionPointer> instructionPointerStack;

  final Deque<BrainfuckLoopMode> loopModeStack;

  final FullBrainfuckInstructionSet fullBrainfuckInstructionSet;

  final LoopHandlingBrainfuckInstructionSet loopHandlingBrainfuckInstructionSet;

  public BrainfuckInterpreter(Tape<?> tape, BufferedReader input, BufferedWriter output) {
    this.instructionPointerStack = new ArrayDeque<>();
    this.loopModeStack = new ArrayDeque<>();
    fullBrainfuckInstructionSet = new FullBrainfuckInstructionSet(instructionPointerStack, loopModeStack, tape, input, output);
    loopHandlingBrainfuckInstructionSet = new LoopHandlingBrainfuckInstructionSet(instructionPointerStack, loopModeStack, tape);
  }

  @Override
  public InstructionPointer step(InstructionPointer instructionPointer) throws InterpreterException {
    Optional<BrainfuckInstruction> instruction = BrainfuckInstruction
        .fromCharacter(instructionPointer.getInstruction());

    if (instruction.isPresent()) {
      if (loopModeStack.isEmpty() || BrainfuckLoopMode.EXECUTED.equals(loopModeStack.getLast())) {
        return instruction.get().run(instructionPointer, fullBrainfuckInstructionSet);
      }

      return instruction.get().run(instructionPointer, loopHandlingBrainfuckInstructionSet);
    }

    return instructionPointer.forward();
  }

}
