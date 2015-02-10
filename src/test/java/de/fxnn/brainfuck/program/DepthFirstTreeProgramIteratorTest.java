package de.fxnn.brainfuck.program;

import java.util.Arrays;

import com.google.common.collect.ImmutableList;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class DepthFirstTreeProgramIteratorTest {

  @Test
  public void testEmptyTreeProgram() {

    Assert.assertFalse(treeProgram().iterator().hasNext());

  }

  @Test
  public void testFlatTreeProgram() {

    assertIteratesAs(treeProgram(stringProgram("+")), stringProgram("+"));
    assertIteratesAs(treeProgram(stringProgram("+"), stringProgram("-")), stringProgram("+"), stringProgram("-"));

  }

  @Test
  public void testNestedProgram() {

    assertIteratesAs(treeProgram(treeProgram()), treeProgram());

    assertIteratesAs(treeProgram( //
            treeProgram(stringProgram("+"), stringProgram("-")), //
            stringProgram("<") //
        ), //
        treeProgram(stringProgram("+"), stringProgram("-")), //
        stringProgram("+"), //
        stringProgram("-"), //
        stringProgram("<") //
    );

  }

  private void assertIteratesAs(TreeProgram treeProgram, Program... itemsInOrder) {
    Assert.assertThat(ImmutableList.copyOf(treeProgram.iterator()), Matchers.contains(itemsInOrder));
  }

  protected static StringProgram stringProgram(String program) {
    return new StringProgram(program);
  }

  protected static TreeProgram treeProgram(Program... childPrograms) {
    return new TreeProgram(Arrays.asList(childPrograms));
  }

}