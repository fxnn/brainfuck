package de.fxnn.brainfuck.interpreter;

import java.util.Optional;

import de.fxnn.brainfuck.program.InstructionPointer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BrainfuckInstruction {

  MOVE_FORWARD('>') {
    @Override
    public Instruction onInstructionSet(BrainfuckInstructionSet instructionSet) {
      return instructionSet::moveForward;
    }

  },
  MOVE_BACKWARD('<') {
    @Override
    public Instruction onInstructionSet(BrainfuckInstructionSet instructionSet) {
      return instructionSet::moveBackward;
    }

  },
  INCREMENT('+') {
    @Override
    public Instruction onInstructionSet(BrainfuckInstructionSet instructionSet) {
      return instructionSet::increment;
    }

  },
  DECREMENT('-') {
    @Override
    public Instruction onInstructionSet(BrainfuckInstructionSet instructionSet) {
      return instructionSet::decrement;
    }

  },
  OUTPUT('.') {
    @Override
    public Instruction onInstructionSet(BrainfuckInstructionSet instructionSet) {
      return instructionSet::output;
    }

  },
  INPUT(',') {
    @Override
    public Instruction onInstructionSet(BrainfuckInstructionSet instructionSet) {
      return instructionSet::input;
    }

  },
  START_OF_LOOP('[') {
    @Override
    public Instruction onInstructionSet(BrainfuckInstructionSet instructionSet) {
      return instructionSet::startOfLoop;
    }

  },
  END_OF_LOOP(']') {
    @Override
    public Instruction onInstructionSet(BrainfuckInstructionSet instructionSet) {
      return instructionSet::endOfLoop;
    }
  };

  @Getter
  private final char instructionCharacter;

  public InstructionPointer step(InstructionPointer instructionPointer, BrainfuckInstructionSet instructionSet)
      throws InterpreterException {
    return onInstructionSet(instructionSet).step(instructionPointer);
  }

  public abstract Instruction onInstructionSet(BrainfuckInstructionSet instructionSet);

  public static Optional<BrainfuckInstruction> fromCharacter(char instructionCharacter) {
    for (BrainfuckInstruction instruction : values()) {
      if (instruction.getInstructionCharacter() == instructionCharacter) {
        return Optional.of(instruction);
      }
    }

    return Optional.empty();
  }

}
