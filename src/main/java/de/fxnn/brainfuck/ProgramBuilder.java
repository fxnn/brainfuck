package de.fxnn.brainfuck;

import java.io.DataInput;
import java.io.DataOutput;

import de.fxnn.brainfuck.interpreter.BrainfuckInterpreter;
import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuck.tape.Tape;
import lombok.Getter;

@Getter
public class ProgramBuilder {

  private Program program;

  private Tape<?> tape;

  private DataInput input;

  private DataOutput output;

  public ProgramBuilder withProgram(Program program) {
    this.program = program;
    return this;
  }

  public ProgramBuilder withTape(Tape<?> tape) {
    this.tape = tape;
    return this;
  }

  public ProgramBuilder withInput(DataInput input) {
    this.input = input;
    return this;
  }

  public ProgramBuilder withOutput(DataOutput output) {
    this.output = output;
    return this;
  }

  public ProgramExecutor buildProgramExecutor() {
    return new ProgramExecutor(program, new BrainfuckInterpreter(tape, input, output));
  }

}
