package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.InterpreterException;
import de.fxnn.brainfuck.program.InstructionPointer;

public interface BrainfuckInstructionSet {

  InstructionPointer moveForward(InstructionPointer instructionPointer) throws InterpreterException;

  InstructionPointer moveBackward(InstructionPointer instructionPointer) throws InterpreterException;

  InstructionPointer increment(InstructionPointer instructionPointer) throws InterpreterException;

  InstructionPointer decrement(InstructionPointer instructionPointer) throws InterpreterException;

  InstructionPointer output(InstructionPointer instructionPointer) throws InterpreterException;

  InstructionPointer input(InstructionPointer instructionPointer) throws InterpreterException;

  InstructionPointer startOfLoop(InstructionPointer instructionPointer) throws InterpreterException;

  InstructionPointer endOfLoop(InstructionPointer instructionPointer) throws InterpreterException;

}
