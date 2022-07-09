package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.program.InstructionPointer;

/**
 * Covers the behavior of a given set of instructions. Generally stateful.
 */
public interface InstructionSet {

  InstructionPointer step(InstructionPointer instructionPointer) throws InterpreterException;

}
