package de.fxnn.brainfuck.interpreter.tdd;

import de.fxnn.brainfuck.program.InstructionPointer;

sealed interface State {
  record NoLabelSeen() implements State {}
  record StartOfLabelKnown(InstructionPointer labelPointer) implements State {}
  record LabelKnown(String labelName, InstructionPointer labelPointer) implements State {}
  record WithinTest(String labelName, InstructionPointer labelPointer) implements State {}
}
