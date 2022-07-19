package de.fxnn.brainfuck.interpreter.tdd;

import de.fxnn.brainfuck.interpreter.InterpreterException;
import de.fxnn.brainfuck.program.InstructionPointer;
import de.fxnn.brainfuck.tape.OutOfTapeBoundsException;
import de.fxnn.brainfuck.tape.Tape;
import java.util.Objects;

sealed interface State {

  interface HandlesEvent {
    void testPassed(String labelName);
    void testFailed(String labelName);
  }

  record NoLabelSeen(HandlesEvent stateEventHandler) implements State {

    @Override
    public boolean isOutsideTest() {
      return true;
    }

    @Override
    public State findingLabel(InstructionPointer instructionPointer) {
      return new StartOfLabelKnown(instructionPointer, stateEventHandler);
    }
  }

  record StartOfLabelKnown(InstructionPointer labelPointer, HandlesEvent stateEventHandler) implements State {

    @Override
    public boolean isOutsideTest() {
      return true;
    }

    @Override
    public State findingLabel(InstructionPointer instructionPointer) {
      if (isSameLabel(instructionPointer)) {
        return this;
      }
      return new StartOfLabelKnown(instructionPointer, stateEventHandler);
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
    public State enteringTest(InstructionPointer instructionPointer, Tape<?> tape) {
      var name = extractName(instructionPointer);
      return new ReadFixture(tape, name, labelPointer, stateEventHandler);
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

  record LabelKnown(String labelName, InstructionPointer labelPointer,
                    HandlesEvent stateEventHandler) implements State {

    @Override
    public boolean isOutsideTest() {
      return true;
    }

    @Override
    public State enteringTest(InstructionPointer instructionPointer, Tape<?> tape) {
      return new ReadFixture(tape, labelName, labelPointer, stateEventHandler);
    }
  }

  record ReadFixture(Tape<?> fixtureTape, String labelName,
                     InstructionPointer labelPointer, HandlesEvent stateEventHandler) implements State {

    @Override
    public boolean isOutsideTest() {
      return false;
    }

    @Override
    public State readDigit(InstructionPointer instructionPointer) throws InterpreterException {
      try {
        var digit = Integer.valueOf("" + instructionPointer.getInstruction());
        fixtureTape.writeInteger(digit);
        fixtureTape.moveForward();
        return this;
      } catch (OutOfTapeBoundsException e) {
        throw new InterpreterException("Cannot add digit to tape", e);
      }
    }

    @Override
    public State enteringAssertion(InstructionPointer instructionPointer) {
      fixtureTape.rewind();
      return new ReadAssertion(fixtureTape, labelName, labelPointer, stateEventHandler);
    }

    @Override
    public State leavingTest(InstructionPointer instructionPointer) {
      stateEventHandler.testPassed(labelName);
      return new LabelKnown(labelName, labelPointer, stateEventHandler);
    }

  }

  record ReadAssertion(Tape<?> fixtureTape, String labelName,
                       InstructionPointer labelPointer, HandlesEvent stateEventHandler) implements State {

    @Override
    public boolean isOutsideTest() {
      return false;
    }

    @Override
    public State readDigit(InstructionPointer instructionPointer) throws InterpreterException {
      try {
        var expected = Integer.valueOf("" + instructionPointer.getInstruction());
        var actual = fixtureTape.readInteger();
        fixtureTape.moveForward();

        if (Objects.equals(expected, actual)) {
          return this;
        }

        return new FailTest(labelName, labelPointer, stateEventHandler);
      } catch (OutOfTapeBoundsException e) {
        throw new InterpreterException("Cannot add digit to tape", e);
      }
    }

    @Override
    public State leavingTest(InstructionPointer instructionPointer) {
      stateEventHandler.testPassed(labelName);
      return new LabelKnown(labelName, labelPointer, stateEventHandler);
    }
  }

  record FailTest(String labelName,
                       InstructionPointer labelPointer, HandlesEvent stateEventHandler) implements State {

    @Override
    public boolean isOutsideTest() {
      return false;
    }

    @Override
    public State readDigit(InstructionPointer instructionPointer) {
      // HINT: ignore, as test already fails
      return this;
    }

    @Override
    public State leavingTest(InstructionPointer instructionPointer) {
      stateEventHandler.testFailed(labelName);
      return new LabelKnown(labelName, labelPointer, stateEventHandler);
    }
  }

  boolean isOutsideTest();

  default boolean isWithinTest() {
    return !isOutsideTest();
  }

  default State findingLabel(InstructionPointer instructionPointer) throws InterpreterException {
    throw new UnexpectedInstructionException(instructionPointer.getInstruction(), this);
  }

  default State enteringTest(InstructionPointer instructionPointer, Tape<?> tape)
      throws InterpreterException {
    throw new UnexpectedInstructionException(instructionPointer.getInstruction(), this);
  }

  default State leavingTest(InstructionPointer instructionPointer) throws InterpreterException {
    throw new UnexpectedInstructionException(instructionPointer.getInstruction(), this);
  }

  default State enteringAssertion(InstructionPointer instructionPointer)
      throws InterpreterException {
    throw new UnexpectedInstructionException(instructionPointer.getInstruction(), this);
  }

  default State readDigit(InstructionPointer instructionPointer)
      throws InterpreterException {
    throw new UnexpectedInstructionException(instructionPointer.getInstruction(), this);
  }

}
