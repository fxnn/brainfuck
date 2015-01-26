package de.fxnn.brainfuck;

import de.fxnn.brainfuck.program.InstructionPointer;

public interface Interpreter {

  InstructionPointer step(InstructionPointer instruction) throws InterpreterException;

}
