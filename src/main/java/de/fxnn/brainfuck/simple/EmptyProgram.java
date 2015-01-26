package de.fxnn.brainfuck.simple;

import de.fxnn.brainfuck.InstructionPointer;
import de.fxnn.brainfuck.InvalidInstructionPointer;
import de.fxnn.brainfuck.Program;

public class EmptyProgram implements Program {

  private static final EmptyProgram EMPTY_PROGRAM = new EmptyProgram();

  public static EmptyProgram emptyProgram() {
    return EMPTY_PROGRAM;
  }

  @Override
  public InstructionPointer getStartOfProgram() {
    return InvalidInstructionPointer.invalidInstructionPointer();
  }
}
