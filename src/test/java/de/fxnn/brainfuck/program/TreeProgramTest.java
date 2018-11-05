package de.fxnn.brainfuck.program;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class TreeProgramTest {

  @Test
  public void testEmptyTreeProgramReturnsInvalidPointer() {

    TreeProgram sut = new TreeProgram(Collections.<Program>emptyList());
    Assert.assertEquals(InvalidInstructionPointer.invalidInstructionPointer(), sut.getStartOfProgram());

  }

  @Test
  public void testIteratesLeafsInExecutionOrder() {

    TreeProgram sut = new TreeProgram(Arrays.asList( //
        new TreeProgram(Arrays.<Program>asList(new StringProgram("++"), new StringProgram("--"))), //
        new StringProgram("<<"), //
        new TreeProgram(Arrays.<Program>asList(new StringProgram(">>"))) //
    ));

    ImmutableList<Program> stringProgramsInIterationOrder = ImmutableList
        .copyOf(Iterators.filter(sut.iterator(), Predicates.instanceOf(StringProgram.class)));

    Assert.assertThat(stringProgramsInIterationOrder, Matchers.<Program>contains(//
        new StringProgram("++"), new StringProgram("--"), new StringProgram("<<"), new StringProgram(">>")) //
    );

  }

}