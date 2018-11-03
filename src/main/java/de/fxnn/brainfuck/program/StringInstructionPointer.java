package de.fxnn.brainfuck.program;

import lombok.AllArgsConstructor;

import static de.fxnn.brainfuck.program.InvalidInstructionPointer.invalidInstructionPointer;

@AllArgsConstructor
public class StringInstructionPointer extends AbstractInstructionPointer {

  String program;

  int programIndex;

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
