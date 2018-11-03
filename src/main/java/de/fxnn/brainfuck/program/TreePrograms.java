package de.fxnn.brainfuck.program;

import com.google.common.collect.Iterators;
import de.fxnn.util.IteratorToIterable;

import java.util.Iterator;
import java.util.List;

public class TreePrograms {

  private TreePrograms() {
    // static utility class
  }

  /**
   * including the treeProgram itself
   */
  public static Iterator<Program> searchProgramsDepthFirst(Program program) {
    Iterator<Program> singletonIterator = Iterators.singletonIterator(program);
    if (program instanceof TreeProgram) {
      return Iterators.concat(singletonIterator, ((TreeProgram) program).iterator());
    }

    return singletonIterator;
  }

  public static String toString(Program program) {
    final StringBuilder resultBuilder = new StringBuilder();

    Iterator<StringProgram> stringPrograms = Iterators.filter(searchProgramsDepthFirst(program), StringProgram.class);
    for (StringProgram p : IteratorToIterable.iterateOnce(stringPrograms)) {
      resultBuilder.append(p.getProgram());
    }

    return resultBuilder.toString();
  }

  public static int getTotalStringProgramLength(Program program) {
    if (program instanceof StringProgram) {
      return ((StringProgram) program).getProgram().length();
    }

    if (program instanceof TreeProgram) {
      int result = 0;

      List<Program> childPrograms = ((TreeProgram) program).getChildPrograms();
      for (Program childProgram : childPrograms) {
        result += getTotalStringProgramLength(childProgram);
      }

      return result;
    }

    throw new IllegalArgumentException("Program not supported: " + program);
  }

}
