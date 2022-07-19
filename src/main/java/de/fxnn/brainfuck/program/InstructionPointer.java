package de.fxnn.brainfuck.program;

public interface InstructionPointer {

  InstructionPointer forward();

  char getInstruction();

  boolean isEndOfProgram();

  default boolean isInstructionWithin(char low, char high) {
    return low <= getInstruction() && getInstruction() <= high;
  }

}
