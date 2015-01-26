package de.fxnn.brainfuck.simple;

import de.fxnn.brainfuck.InstructionPointer;
import de.fxnn.brainfuck.Program;
import lombok.Value;

import static de.fxnn.brainfuck.InvalidInstructionPointer.invalidInstructionPointer;

@Value
public class StringSourcedProgram implements Program {

  String program;

  @Override
  public InstructionPointer getStartOfProgram() {
    if (program.isEmpty()) {
      return invalidInstructionPointer();
    }

    return new StringSourcedInstructionPointer(program, 0);
  }

}
