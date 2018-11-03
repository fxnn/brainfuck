package de.fxnn.brainfuck.program;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static de.fxnn.brainfuck.program.InvalidInstructionPointer.invalidInstructionPointer;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TreeInstructionPointer extends AbstractInstructionPointer {

  private final List<Program> childPrograms;

  private final int childProgramIndex;

  private final InstructionPointer childInstructionPointer;

  @Override
  public char getInstruction() {
    return childInstructionPointer.getInstruction();
  }

  @Override
  public InstructionPointer forward() {
    InstructionPointer nextChildInstructionPointer = childInstructionPointer.forward();

    if (invalidInstructionPointer().equals(nextChildInstructionPointer)) {
      return createInstructionPointerAt(childPrograms, childProgramIndex + 1);
    }

    return new TreeInstructionPointer(childPrograms, childProgramIndex, nextChildInstructionPointer);
  }

  public static InstructionPointer createInstructionPointer(List<Program> childPrograms) {
    return createInstructionPointerAt(childPrograms, 0);
  }

  protected static InstructionPointer createInstructionPointerAt(List<Program> childPrograms, int childProgramIndex) {
    for (int correctedChildProgramIndex = childProgramIndex; correctedChildProgramIndex < childPrograms.size(); correctedChildProgramIndex++) {
      InstructionPointer childInstructionPointer = firstInstructionAt(childPrograms, correctedChildProgramIndex);

      if (!invalidInstructionPointer().equals(childInstructionPointer)) {
        return new TreeInstructionPointer(childPrograms, correctedChildProgramIndex, childInstructionPointer);
      }
    }

    return invalidInstructionPointer();
  }

  protected static InstructionPointer firstInstructionAt(List<Program> childPrograms, int childProgramIndex) {
    try {
      return childPrograms.get(childProgramIndex).getStartOfProgram();
    } catch (IndexOutOfBoundsException ex) {
      return invalidInstructionPointer();
    }
  }
}
