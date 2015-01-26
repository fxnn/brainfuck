package de.fxnn.brainfuck.program;

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
