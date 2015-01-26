package de.fxnn.brainfuck.program;

public class InvalidInstructionPointer implements InstructionPointer {

  private static final InvalidInstructionPointer INVALID_INSTRUCTION_POINTER = new InvalidInstructionPointer();

  public static InvalidInstructionPointer invalidInstructionPointer() {
    return INVALID_INSTRUCTION_POINTER;
  }

  @Override
  public InstructionPointer forward() {
    return this;
  }

  @Override
  public char getInstruction() {
    throw new IllegalStateException("Invalid instruction pointer");
  }
}
