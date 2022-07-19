package de.fxnn.brainfuck.interpreter.tdd;

import de.fxnn.brainfuck.interpreter.InterpreterException;
import de.fxnn.brainfuck.program.InstructionPointer;
import de.fxnn.brainfuck.program.StringInstructionPointer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TddBrainfuckInstructionSetTest {

  private StringWriter output;

  @Before
  public void setup() {
    output = new StringWriter();
  }

  @Test
  public void step__unknownInstruction__forward() throws InterpreterException {
    var result = createUnderTest().step(
        new StringInstructionPointer("x", 0));
    Assert.assertTrue(result.isEndOfProgram());
  }

  @Test
  public void step__beginOfTestWithoutLabel__throws() {
    try {
      var instructionPointer = new StringInstructionPointer("{", 0);
      createUnderTest().step(instructionPointer);
      Assert.fail();
    } catch (InterpreterException ex) {
      Assertions.assertThat(ex).hasMessageContaining("Unexpected instruction '{' (in state 'NoLabelSeen[");
    }
  }

  @Test
  public void step__labelBeginEnd__passes() throws InterpreterException {
    var underTest = createUnderTest();
    stepUntilEndOfProgram(underTest, "#{}");
    Assert.assertEquals("PASSED #\n", output.toString());
  }

  @Test
  public void step__labelAndTwoTests__passesTwoTimes() throws InterpreterException {
    var underTest = createUnderTest();
    stepUntilEndOfProgram(underTest, "#{}{}");
    Assert.assertEquals("PASSED #\nPASSED #\n", output.toString());
  }

  @Test
  public void step__labelWithLongName__cutTo20Characters() throws InterpreterException {
    var underTest = createUnderTest();
    stepUntilEndOfProgram(underTest, "#234567890123456789012345{}");
    Assert.assertEquals("PASSED #2345678901234567890\n", output.toString());
  }

  @Test
  public void step__labelWithMultipleHashChars__allIncluded() throws InterpreterException {
    var underTest = createUnderTest();
    stepUntilEndOfProgram(underTest, "### this is my test{}");
    Assert.assertEquals("PASSED ### this is my test\n", output.toString());
  }

  @Test
  public void step__labelBeginEnd__forward() throws InterpreterException {
    var underTest = createUnderTest();
    var result = underTest.step(
        underTest.step(underTest.step(new StringInstructionPointer("#{}", 0))));
    Assert.assertTrue(result.isEndOfProgram());
  }

  @Test
  public void step__tapeOutputDiffersFromInput__failed() throws InterpreterException {
    var underTest = createUnderTest();
    stepUntilEndOfProgram(underTest, "#{0~1}");
    Assert.assertEquals("FAILED #\n", output.toString());
  }

  private void stepUntilEndOfProgram(TddBrainfuckInstructionSet underTest,
      String stringProgram) throws InterpreterException {
    InstructionPointer instructionPointer = new StringInstructionPointer(stringProgram, 0);
    while (!instructionPointer.isEndOfProgram()) {
      instructionPointer = underTest.step(instructionPointer);
    }
  }

  private TddBrainfuckInstructionSet createUnderTest() {
    return new TddBrainfuckInstructionSet(new PrintWriter(output));
  }

}
