package de.fxnn.brainfuck;

import de.fxnn.brainfuck.interpreter.Interpreter;
import de.fxnn.brainfuck.interpreter.InterpreterException;
import de.fxnn.brainfuck.program.InstructionPointer;
import de.fxnn.brainfuck.program.Program;

public class ProgramExecutor implements Runnable {

  Program program;

  Interpreter interpreter;

  public ProgramExecutor(Program program, Interpreter interpreter) {
    this.program = program;
    this.interpreter = interpreter;
  }

  public void run() {

    try {
      InstructionPointer instructionPointer = program.getStartOfProgram();

      while (!instructionPointer.isEndOfProgram()) {
        instructionPointer = interpreter.step(instructionPointer);

        if (Thread.currentThread().isInterrupted()) {
          throw new ProgramExecutionException("Program was interrputed: " + program);
        }
      }

    } catch (InterpreterException ex) {
      throw new ProgramExecutionException(
          "Error [" + ex.getMessage() + "] while interpreting the Brainfuck program [" + program + "]", ex);
    }

  }

}
