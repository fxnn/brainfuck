package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.program.InstructionPointer;

public interface Instruction {

  InstructionPointer step(InstructionPointer instructionPointer) throws InterpreterException;

}
