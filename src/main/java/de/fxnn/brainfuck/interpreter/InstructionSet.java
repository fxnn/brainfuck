package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.program.InstructionPointer;

public interface InstructionSet {

  InstructionPointer step(InstructionPointer instructionPointer) throws InterpreterException;

}
