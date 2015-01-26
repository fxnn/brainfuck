package de.fxnn.brainfuck;

public interface Interpreter {

  InstructionPointer step(InstructionPointer instruction) throws InterpreterException;

}
