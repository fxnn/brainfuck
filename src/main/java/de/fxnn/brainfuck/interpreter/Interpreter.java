package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.program.InstructionPointer;

/**
 * Reads instructions from an {@link InstructionPointer} and thereby modifies its state.
 */
public interface Interpreter {

  InstructionPointer step(InstructionPointer instruction) throws InterpreterException;

}
