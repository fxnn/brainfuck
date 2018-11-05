package de.fxnn.brainfuck.program;

import com.google.common.base.Objects;

import java.util.Iterator;
import java.util.List;

public class TreeProgram implements Program, Iterable<Program> {

  private final List<Program> childPrograms;

  public TreeProgram(List<Program> childPrograms) {
    this.childPrograms = childPrograms;
  }

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

  public List<Program> getChildPrograms() {
    return this.childPrograms;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TreeProgram programs = (TreeProgram) o;
    return Objects.equal(childPrograms, programs.childPrograms);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(childPrograms);
  }

  public String toString() {
    return "TreeProgram(childPrograms=" + this.getChildPrograms() + ")";
  }
}
