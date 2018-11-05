package de.fxnn.brainfuck.program;

public interface InstructionPointer {

  InstructionPointer forward();

  char getInstruction();

  boolean isEndOfProgram();

}
