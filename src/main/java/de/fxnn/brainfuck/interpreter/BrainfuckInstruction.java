package de.fxnn.brainfuck.interpreter;

import java.util.Optional;

import de.fxnn.brainfuck.program.InstructionPointer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nullable;

@Getter
@AllArgsConstructor
public enum BrainfuckInstruction {

  MOVE_FORWARD('>') {
    @Override
    public Instruction onInstructionSet(final BrainfuckInstructionSet instructionSet) {
      return new Instruction() {

        @Override
        public InstructionPointer step(InstructionPointer instructionPointer) throws InterpreterException {
          return instructionSet.moveForward(instructionPointer);
        }
      };
    }

  },
  MOVE_BACKWARD('<') {
    @Override
    public Instruction onInstructionSet(final BrainfuckInstructionSet instructionSet) {
      return new Instruction() {

        @Override
        public InstructionPointer step(InstructionPointer instructionPointer) throws InterpreterException {
          return instructionSet.moveBackward(instructionPointer);
        }
      };
    }

  },
  INCREMENT('+') {
    @Override
    public Instruction onInstructionSet(final BrainfuckInstructionSet instructionSet) {
      return new Instruction() {

        @Override
        public InstructionPointer step(InstructionPointer instructionPointer) throws InterpreterException {
          return instructionSet.increment(instructionPointer);
        }
      };
    }

  },
  DECREMENT('-') {
    @Override
    public Instruction onInstructionSet(final BrainfuckInstructionSet instructionSet) {
      return new Instruction() {

        @Override
        public InstructionPointer step(InstructionPointer instructionPointer) throws InterpreterException {
          return instructionSet.decrement(instructionPointer);
        }
      };
    }

  },
  OUTPUT('.') {
    @Override
    public Instruction onInstructionSet(final BrainfuckInstructionSet instructionSet) {
      return new Instruction() {

        @Override
        public InstructionPointer step(InstructionPointer instructionPointer) throws InterpreterException {
          return instructionSet.output(instructionPointer);
        }
      };
    }

  },
  INPUT(',') {
    @Override
    public Instruction onInstructionSet(final BrainfuckInstructionSet instructionSet) {
      return new Instruction() {

        @Override
        public InstructionPointer step(InstructionPointer instructionPointer) throws InterpreterException {
          return instructionSet.input(instructionPointer);
        }
      };
    }

  },
  START_OF_LOOP('[') {
    @Override
    public Instruction onInstructionSet(final BrainfuckInstructionSet instructionSet) {
      return new Instruction() {

        @Override
        public InstructionPointer step(InstructionPointer instructionPointer) throws InterpreterException {
          return instructionSet.startOfLoop(instructionPointer);
        }
      };
    }

  },
  END_OF_LOOP(']') {
    @Override
    public Instruction onInstructionSet(final BrainfuckInstructionSet instructionSet) {
      return new Instruction() {

        @Override
        public InstructionPointer step(InstructionPointer instructionPointer) throws InterpreterException {
          return instructionSet.endOfLoop(instructionPointer);
        }
      };
    }
  };

  @Getter
  private final char instructionCharacter;

  public InstructionPointer step(InstructionPointer instructionPointer, BrainfuckInstructionSet instructionSet)
      throws InterpreterException {
    return onInstructionSet(instructionSet).step(instructionPointer);
  }

  public abstract Instruction onInstructionSet(BrainfuckInstructionSet instructionSet);

  @Nullable
  public static BrainfuckInstruction fromCharacter(char instructionCharacter) {
    for (BrainfuckInstruction instruction : values()) {
      if (instruction.getInstructionCharacter() == instructionCharacter) {
        return instruction;
      }
    }

    return null;
  }

}
