package de.fxnn.brainfuck.program;

import static de.fxnn.brainfuck.program.InvalidInstructionPointer.invalidInstructionPointer;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StringInstructionPointer that = (StringInstructionPointer) o;
    return programIndex == that.programIndex && program.equals(that.program);
  }

  @Override
  public int hashCode() {
    return Objects.hash(program, programIndex);
  }
}
