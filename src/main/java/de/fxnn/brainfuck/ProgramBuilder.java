package de.fxnn.brainfuck;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import de.fxnn.brainfuck.interpreter.BrainfuckInterpreter;
import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuck.tape.Tape;
import lombok.Getter;

@Getter
public class ProgramBuilder {

  private Program program;

  private Tape<?> tape;

  private BufferedReader inputReader;

  private BufferedWriter outputWriter;

  public ProgramBuilder withProgram(Program program) {
    this.program = program;
    return this;
  }

  public ProgramBuilder withTape(Tape<?> tape) {
    this.tape = tape;
    return this;
  }

  public ProgramBuilder withInputReader(BufferedReader inputReader) {
    this.inputReader = inputReader;
    return this;
  }

  public ProgramBuilder withOutputWriter(BufferedWriter outputWriter) {
    this.outputWriter = outputWriter;
    return this;
  }

  public ProgramExecutor buildProgramExecutor() {
    return new ProgramExecutor(program, new BrainfuckInterpreter(tape, inputReader, outputWriter));
  }

}
