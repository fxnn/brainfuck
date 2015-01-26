package de.fxnn.brainfuck.program;

import lombok.Value;

import static de.fxnn.brainfuck.program.InvalidInstructionPointer.invalidInstructionPointer;

@Value
public class StringProgram implements Program {

  String program;

  @Override
  public InstructionPointer getStartOfProgram() {
    if (program.isEmpty()) {
      return invalidInstructionPointer();
    }

    return new StringInstructionPointer(program, 0);
  }

}
