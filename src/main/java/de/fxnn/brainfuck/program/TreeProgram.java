package de.fxnn.brainfuck.program;

import java.util.Iterator;
import java.util.List;

import lombok.Value;

@Value
public class TreeProgram implements Program, Iterable<Program> {

  private final List<Program> childPrograms;

  @Override
  public InstructionPointer getStartOfProgram() {
    if (childPrograms.isEmpty()) {
      return InvalidInstructionPointer.invalidInstructionPointer();
    }

    return TreeInstructionPointer.createInstructionPointer(childPrograms);
  }

  @Override
  public Iterator<Program> iterator() {
    return new DepthFirstTreeProgramIterator(this);
  }
}
