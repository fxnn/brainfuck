package de.fxnn.brainfuck.program;

import com.google.common.base.Objects;

import static de.fxnn.brainfuck.program.InvalidInstructionPointer.invalidInstructionPointer;

public class StringProgram implements Program {

  private final String program;

  public StringProgram(String program) {
    this.program = program;
  }

  @Override
  public InstructionPointer getStartOfProgram() {
    if (program.isEmpty()) {
      return invalidInstructionPointer();
    }

    return new StringInstructionPointer(program, 0);
  }

  public String getProgram() {
    return this.program;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StringProgram that = (StringProgram) o;
    return Objects.equal(program, that.program);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(program);
  }

  public String toString() {
    return "StringProgram(program=" + this.getProgram() + ")";
  }
}
