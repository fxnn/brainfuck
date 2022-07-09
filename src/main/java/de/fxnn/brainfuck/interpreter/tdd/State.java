package de.fxnn.brainfuck.interpreter.tdd;

import de.fxnn.brainfuck.interpreter.InterpreterException;
import de.fxnn.brainfuck.program.InstructionPointer;
import java.util.function.Consumer;

sealed interface State {

  record NoLabelSeen() implements State {

    @Override
    public State findingLabel(InstructionPointer instructionPointer) {
      return new StartOfLabelKnown(instructionPointer);
    }
  }

  record StartOfLabelKnown(InstructionPointer labelPointer) implements State {

    @Override
    public State findingLabel(InstructionPointer instructionPointer) {
      if (isSameLabel(instructionPointer)) {
        return this;
      }
      return new StartOfLabelKnown(instructionPointer);
    }

    private boolean isSameLabel(InstructionPointer endPointer) {
      InstructionPointer currentPointer = labelPointer;
      while (!currentPointer.isEndOfProgram() && currentPointer.getInstruction() == '#') {
        if (endPointer.equals(currentPointer)) {
          return true;
        }
        currentPointer = currentPointer.forward();
      }
      return false;
    }

    @Override
    public State enteringTest(InstructionPointer instructionPointer) {
      var name = extractName(instructionPointer);
      return new WithinTest(name, labelPointer);
    }

    private String extractName(InstructionPointer startOfTestPointer) {
      var nameBuilder = new StringBuilder();
      var current = labelPointer;
      while (nameBuilder.length() < 20 && !current.equals(startOfTestPointer)) {
        nameBuilder.append(current.getInstruction());
        current = current.forward();
      }
      return nameBuilder.toString();
    }
  }

  record LabelKnown(String labelName, InstructionPointer labelPointer) implements State {

    @Override
    public State enteringTest(InstructionPointer instructionPointer) {
      return new WithinTest(labelName, labelPointer);
    }
  }

  record WithinTest(String labelName, InstructionPointer labelPointer) implements State {

    @Override
    public State leavingTest(InstructionPointer instructionPointer,
        Consumer<String> labelNameConsumer) {
      labelNameConsumer.accept(labelName());
      return new LabelKnown(labelName, labelPointer);
    }

  }

  default boolean isOutsideTest() {
    return this instanceof NoLabelSeen || this instanceof StartOfLabelKnown
        || this instanceof LabelKnown;
  }

  default boolean isWithinTest() {
    return this instanceof WithinTest;
  }

  default State findingLabel(InstructionPointer instructionPointer) throws InterpreterException {
    throw new UnexpectedInstructionException(instructionPointer.getInstruction(), this);
  }

  default State enteringTest(InstructionPointer instructionPointer) throws InterpreterException {
    throw new UnexpectedInstructionException(instructionPointer.getInstruction(), this);
  }

  default State leavingTest(InstructionPointer instructionPointer,
      Consumer<String> labelNameConsumer) throws InterpreterException {
    throw new UnexpectedInstructionException(instructionPointer.getInstruction(), this);
  }

}
