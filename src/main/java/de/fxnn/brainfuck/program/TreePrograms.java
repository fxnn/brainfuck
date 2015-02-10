package de.fxnn.brainfuck.program;

import java.util.Iterator;

import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

public class TreePrograms {

  private TreePrograms() {
    // static utility class
  }

  /** including the treeProgram itself */
  public static Iterator<Program> searchProgramsDepthFirst(Program program) {
    UnmodifiableIterator<Program> singletonIterator = Iterators.singletonIterator(program);
    if (program instanceof TreeProgram) {
      return Iterators.concat(singletonIterator, ((TreeProgram) program).iterator());
    }

    return singletonIterator;
  }

  public static String toString(Program program) {
    StringBuilder resultBuilder = new StringBuilder();

    Iterators.filter(searchProgramsDepthFirst(program), StringProgram.class).forEachRemaining(p -> resultBuilder.append(p.getProgram()));

    return resultBuilder.toString();
  }

  public static int getTotalStringProgramLength(Program program) {
    if (program instanceof StringProgram) {
      return ((StringProgram) program).getProgram().length();
    }

    if (program instanceof TreeProgram) {
      return ((TreeProgram) program).getChildPrograms().stream().mapToInt(TreePrograms::getTotalStringProgramLength).sum();
    }

    throw new IllegalArgumentException("Program not supported: " + program);
  }

}
