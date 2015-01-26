package de.fxnn.brainfuck.simple;

import de.fxnn.brainfuck.InstructionPointer;
import lombok.AllArgsConstructor;

import static de.fxnn.brainfuck.InvalidInstructionPointer.invalidInstructionPointer;

@AllArgsConstructor
public class StringSourcedInstructionPointer implements InstructionPointer {

  String program;

  int programIndex;

  @Override
  public InstructionPointer forward() {
    if (program.length() > programIndex + 1) {
      return new StringSourcedInstructionPointer(program, programIndex + 1);
    }

    return invalidInstructionPointer();
  }

  @Override
  public char getInstruction() {
    return program.charAt(programIndex);
  }
}
