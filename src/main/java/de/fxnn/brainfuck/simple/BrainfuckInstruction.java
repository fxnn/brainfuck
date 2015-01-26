package de.fxnn.brainfuck.simple;

import java.util.Optional;

import de.fxnn.brainfuck.InstructionPointer;
import de.fxnn.brainfuck.InterpreterException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BrainfuckInstruction {

  MOVE_FORWARD('>') {
    @Override
    public InstructionPointer run(InstructionPointer instructionPointer, BrainfuckInstructionSet instructionSet)
        throws InterpreterException {
      return instructionSet.moveForward(instructionPointer);
    }
  },
  MOVE_BACKWARD('<') {
    @Override
    public InstructionPointer run(InstructionPointer instructionPointer, BrainfuckInstructionSet instructionSet)
        throws InterpreterException {
      return instructionSet.moveBackward(instructionPointer);
    }
  },
  INCREMENT('+') {
    @Override
    public InstructionPointer run(InstructionPointer instructionPointer, BrainfuckInstructionSet instructionSet)
        throws InterpreterException {
      return instructionSet.increment(instructionPointer);
    }
  },
  DECREMENT('-') {
    @Override
    public InstructionPointer run(InstructionPointer instructionPointer, BrainfuckInstructionSet instructionSet)
        throws InterpreterException {
      return instructionSet.decrement(instructionPointer);
    }
  },
  OUTPUT('.') {
    @Override
    public InstructionPointer run(InstructionPointer instructionPointer, BrainfuckInstructionSet instructionSet)
        throws InterpreterException {
      return instructionSet.output(instructionPointer);
    }
  },
  INPUT(',') {
    @Override
    public InstructionPointer run(InstructionPointer instructionPointer, BrainfuckInstructionSet instructionSet)
        throws InterpreterException {
      return instructionSet.input(instructionPointer);
    }
  },
  START_OF_LOOP('[') {
    @Override
    public InstructionPointer run(InstructionPointer instructionPointer, BrainfuckInstructionSet instructionSet)
        throws InterpreterException {
      return instructionSet.startOfLoop(instructionPointer);
    }
  },
  END_OF_LOOP(']') {
    @Override
    public InstructionPointer run(InstructionPointer instructionPointer, BrainfuckInstructionSet instructionSet)
        throws InterpreterException {
      return instructionSet.endOfLoop(instructionPointer);
    }
  };

  @Getter
  private final char instructionCharacter;

  public abstract InstructionPointer run(InstructionPointer instructionPointer, BrainfuckInstructionSet instructionSet)
      throws InterpreterException;

  public static Optional<BrainfuckInstruction> fromCharacter(char instructionCharacter) {
    for (BrainfuckInstruction instruction : values()) {
      if (instruction.getInstructionCharacter() == instructionCharacter) {
        return Optional.of(instruction);
      }
    }

    return Optional.empty();
  }

}
