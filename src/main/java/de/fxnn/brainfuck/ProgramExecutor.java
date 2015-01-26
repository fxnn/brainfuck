package de.fxnn.brainfuck;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProgramExecutor implements Runnable {

  Program program;

  Interpreter interpreter;

  public void run() {

    try {
      InstructionPointer instructionPointer = program.getStartOfProgram();

      while (!instructionPointer.isEndOfProgram()) {
        instructionPointer = interpreter.step(instructionPointer);
      }

    } catch (InterpreterException ex) {
      throw new RuntimeException(ex.getMessage(), ex.getCause());
    }

  }

}
