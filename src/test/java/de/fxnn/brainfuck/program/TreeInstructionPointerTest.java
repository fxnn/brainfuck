package de.fxnn.brainfuck.program;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static de.fxnn.brainfuck.program.InvalidInstructionPointer.invalidInstructionPointer;

public class TreeInstructionPointerTest {

  List<Program> listOfPrograms;

  InstructionPointer sut;

  @Before
  public void setUp() {
    listOfPrograms = null;
    sut = null;
  }

  @Test(expected = IllegalStateException.class)
  public void emptyProgram_hasNoInstruction() {

    givenListOfPrograms();

    whenSut().getInstruction();

  }

  @Test
  public void emptyProgram_forwardReturnsInvalidInstructionPointer() {

    givenListOfPrograms();

    InstructionPointer result = whenSut().forward();

    Assert.assertEquals(invalidInstructionPointer(), result);

  }

  @Test(expected = IllegalStateException.class)
  public void emptyProgramWithEmptyChildProgram_hasNoInstruction() {

    givenListOfPrograms(treeProgram());

    whenSut().getInstruction();

  }

  @Test
  public void emptyProgramWithEmptyChildProgram_forwardReturnsInvalidInstructionPointer() {

    givenListOfPrograms(treeProgram());

    InstructionPointer result = whenSut().forward();

    Assert.assertEquals(invalidInstructionPointer(), result);

  }

  @Test
  public void iterateThroughSingleChildProgram() {

    givenListOfPrograms(stringProgram("+"));

    Assert.assertEquals('+', whenSut().getInstruction());
    Assert.assertEquals(invalidInstructionPointer(), whenSut().forward());

  }

  @Test
  public void iterateThroughMultipleChildPrograms() {

    givenListOfPrograms(stringProgram("+"), stringProgram("-"));

    Assert.assertEquals('+', whenSut().getInstruction());
    Assert.assertEquals('-', whenSut().forward().getInstruction());
    Assert.assertEquals(invalidInstructionPointer(), whenSut().forward().forward());

  }

  @Test
  public void iterateThroughSingleNestedChildProgram() {

    givenListOfPrograms(treeProgram(stringProgram("+")));

    Assert.assertEquals('+', whenSut().getInstruction());
    Assert.assertEquals(invalidInstructionPointer(), whenSut().forward());

  }

  @Test
  public void iterateThroughMultipleNestedChildPrograms() {

    givenListOfPrograms(treeProgram(stringProgram("+")), treeProgram(stringProgram("-")));

    Assert.assertEquals('+', whenSut().getInstruction());
    Assert.assertEquals('-', whenSut().forward().getInstruction());
    Assert.assertEquals(invalidInstructionPointer(), whenSut().forward().forward());

  }

  @Test
  public void leaveNestedTreeProgram() {

    givenListOfPrograms(treeProgram(stringProgram("+"), stringProgram("+")), stringProgram("-"));

    Assert.assertEquals('+', whenSut().getInstruction());
    Assert.assertEquals('+', whenSut().forward().getInstruction());
    Assert.assertEquals('-', whenSut().forward().forward().getInstruction());
    Assert.assertEquals(invalidInstructionPointer(), whenSut().forward().forward().forward());

  }

  @Test
  public void skipsEmptyChildPrograms() {

    givenListOfPrograms(stringProgram("+"), treeProgram(), stringProgram("-"));

    Assert.assertEquals('+', whenSut().getInstruction());
    Assert.assertEquals('-', whenSut().forward().getInstruction());

  }

  @Test
  public void canSkipNestedEmptyChildPrograms() {

    givenListOfPrograms(treeProgram(stringProgram("+"), treeProgram(), stringProgram("-")), stringProgram("+"));

    Assert.assertEquals('+', whenSut().getInstruction());
    Assert.assertEquals('-', whenSut().forward().getInstruction());
    Assert.assertEquals('+', whenSut().forward().forward().getInstruction());
    Assert.assertEquals(invalidInstructionPointer(), whenSut().forward().forward().forward());


  }

  private InstructionPointer whenSut() {
    if (sut == null) {
      sut = TreeInstructionPointer.createInstructionPointer(listOfPrograms);
    }

    return sut;
  }

  private void givenListOfPrograms(Program... childPrograms) {
    this.listOfPrograms = Arrays.asList(childPrograms);
  }

  protected static StringProgram stringProgram(String program) {
    return new StringProgram(program);
  }

  protected static TreeProgram treeProgram(Program ... childPrograms) {
    return new TreeProgram(Arrays.asList(childPrograms));
  }

}