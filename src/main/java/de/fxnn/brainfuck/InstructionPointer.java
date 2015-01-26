package de.fxnn.brainfuck;

import static de.fxnn.brainfuck.InvalidInstructionPointer.invalidInstructionPointer;

public interface InstructionPointer {

  InstructionPointer forward();

  char getInstruction();

  default boolean isEndOfProgram() {
    return equals(invalidInstructionPointer());
  }

}
