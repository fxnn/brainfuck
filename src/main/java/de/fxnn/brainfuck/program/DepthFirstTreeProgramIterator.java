package de.fxnn.brainfuck.program;

import java.util.Collections;
import java.util.Iterator;

public class DepthFirstTreeProgramIterator implements Iterator<Program> {

  private Iterator<Program> programsInCurrentLevel;

  private Iterator<Program> programsInChildLevel;

  /**
   * Not including the <code>treeProgram</code> itself.
   */
  public DepthFirstTreeProgramIterator(TreeProgram treeProgram) {
    this.programsInCurrentLevel = treeProgram.getChildPrograms().iterator();
    this.programsInChildLevel = Collections.emptyIterator();
  }

  @Override
  public boolean hasNext() {
    return programsInChildLevel.hasNext() || programsInCurrentLevel.hasNext();
  }

  @Override
  public Program next() {
    if (programsInChildLevel.hasNext()) {
      return programsInChildLevel.next();
    }

    Program next = programsInCurrentLevel.next();
    if (next instanceof TreeProgram) {
      programsInChildLevel = new DepthFirstTreeProgramIterator((TreeProgram) next);
    }

    return next;
  }
}
