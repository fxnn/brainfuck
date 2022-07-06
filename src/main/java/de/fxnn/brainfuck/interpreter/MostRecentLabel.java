package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.program.InstructionPointer;

sealed interface MostRecentLabel {
  record NoneYet() implements MostRecentLabel {}
  record StartOfLabelKnown(InstructionPointer startPointer) implements MostRecentLabel {}
  record Known(String name, InstructionPointer startPointer) implements MostRecentLabel {}
}
