package de.fxnn.brainfuck.program;

import static de.fxnn.brainfuck.program.InvalidInstructionPointer.invalidInstructionPointer;

public interface InstructionPointer {

  InstructionPointer forward();

  char getInstruction();

  default boolean isEndOfProgram() {
    return equals(invalidInstructionPointer());
  }

}
