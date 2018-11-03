package de.fxnn.brainfuck.program;

import static de.fxnn.brainfuck.program.InvalidInstructionPointer.invalidInstructionPointer;

public abstract class AbstractInstructionPointer implements InstructionPointer {

  @Override
  public boolean isEndOfProgram() {
    return equals(invalidInstructionPointer());
  }
}
