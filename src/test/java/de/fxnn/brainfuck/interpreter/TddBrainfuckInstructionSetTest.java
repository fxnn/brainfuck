package de.fxnn.brainfuck.interpreter;

import de.fxnn.brainfuck.program.InstructionPointer;
import de.fxnn.brainfuck.program.StringInstructionPointer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.Assert;
import org.junit.Test;

public class TddBrainfuckInstructionSetTest {

  @Test
  public void step__unknownInstruction__forward() throws InterpreterException {
    var underTest = new TddBrainfuckInstructionSet(
        new PrintWriter(new StringWriter()));
    var result = underTest.step(
        new StringInstructionPointer("x", 0));
    Assert.assertTrue(result.isEndOfProgram());
  }

  @Test
  public void step__beginOfTestWithoutLabel__throws() {
    try {
      var instructionPointer = new StringInstructionPointer("{", 0);
      new TddBrainfuckInstructionSet(new PrintWriter(new StringWriter())).step(instructionPointer);
      Assert.fail();
    } catch (InterpreterException ex) {
      Assert.assertEquals("'{' without preceding '#' instruction",
          ex.getMessage());
    }
  }

  @Test
  public void step__endOfTestWithoutBeginOfTest__throws() {
    try {
      var instructionPointer = new StringInstructionPointer("}", 0);
      new TddBrainfuckInstructionSet(new PrintWriter(new StringWriter())).step(instructionPointer);
      Assert.fail();
    } catch (InterpreterException ex) {
      Assert.assertEquals("'}' without preceding '{' instruction",
          ex.getMessage());
    }
  }

  @Test
  public void step__labelBeginEnd__passes() throws InterpreterException {
    var output = new StringWriter();
    var underTest = new TddBrainfuckInstructionSet(new PrintWriter(output));
    underTest.step(
        underTest.step(underTest.step(new StringInstructionPointer("#{}", 0))));
    Assert.assertEquals("PASSED\n", output.toString());
  }

  @Test
  public void step__labelBeginEnd__forward() throws InterpreterException {
    var output = new StringWriter();
    var underTest = new TddBrainfuckInstructionSet(new PrintWriter(output));
    var result = underTest.step(
        underTest.step(underTest.step(new StringInstructionPointer("#{}", 0))));
    Assert.assertTrue(result.isEndOfProgram());
  }

}
