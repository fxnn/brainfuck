package de.fxnn.brainfuck.program;

import static de.fxnn.brainfuck.program.InvalidInstructionPointer.invalidInstructionPointer;

public class StringInstructionPointer extends AbstractInstructionPointer {

  String program;

  int programIndex;

  public StringInstructionPointer(String program, int programIndex) {
    this.program = program;
    this.programIndex = programIndex;
  }

  @Override
  public InstructionPointer forward() {
    if (program.length() > programIndex + 1) {
      return new StringInstructionPointer(program, programIndex + 1);
    }

    return invalidInstructionPointer();
  }

  @Override
  public char getInstruction() {
    return program.charAt(programIndex);
  }
}
