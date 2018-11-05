package de.fxnn.brainfuck.program;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class TreeProgramsTest {

  @Test
  public void testSearchProgramsDepthFirst_emptyProgram() {

    Iterator<Program> programIterator = TreePrograms.searchProgramsDepthFirst(EmptyProgram.emptyProgram());

    List<Program> iteratedPrograms = newArrayList(programIterator);
    assertThat(iteratedPrograms, contains((Program) EmptyProgram.emptyProgram()));

  }

  @Test
  public void testSearchProgramsDepthFirst_singleStringProgram() {

    StringProgram stringProgram = new StringProgram("+-");

    Iterator<Program> programIterator = TreePrograms.searchProgramsDepthFirst(stringProgram);

    List<Program> iteratedPrograms = newArrayList(programIterator);
    assertThat(iteratedPrograms, contains((Program) stringProgram));

  }

  @Test
  public void testSearchProgramsDepthFirst_treeProgram_singleLevel() {

    StringProgram stringProgram1 = new StringProgram("+-");
    StringProgram stringProgram2 = new StringProgram(",.");
    TreeProgram treeProgram = new TreeProgram(Arrays.<Program>asList(stringProgram1, stringProgram2));

    Iterator<Program> programIterator = TreePrograms.searchProgramsDepthFirst(treeProgram);

    List<Program> iteratedPrograms = newArrayList(programIterator);
    assertThat(iteratedPrograms, contains(treeProgram, stringProgram1, stringProgram2));

  }

  @Test
  public void testSearchProgramsDepthFirst_treeProgram_twoLevels() {

    StringProgram stringProgram1 = new StringProgram("+-");
    StringProgram stringProgram2 = new StringProgram(",.");
    StringProgram stringProgram3 = new StringProgram("><");
    TreeProgram treeProgram2 = new TreeProgram(Arrays.<Program>asList(stringProgram2));
    TreeProgram treeProgram1 = new TreeProgram(asList(stringProgram1, treeProgram2, stringProgram3));

    Iterator<Program> programIterator = TreePrograms.searchProgramsDepthFirst(treeProgram1);

    List<Program> iteratedPrograms = newArrayList(programIterator);
    assertThat(iteratedPrograms, contains(treeProgram1, stringProgram1, treeProgram2, stringProgram2, stringProgram3));

  }

  @Test
  public void testSearchProgramsDepthFirst_treeProgram_threeLevels() {

    StringProgram stringProgram = new StringProgram("+-");
    TreeProgram treeProgram3 = new TreeProgram(Arrays.<Program>asList(stringProgram));
    TreeProgram treeProgram2 = new TreeProgram(Arrays.<Program>asList(treeProgram3));
    TreeProgram treeProgram1 = new TreeProgram(Arrays.<Program>asList(treeProgram2));

    Iterator<Program> programIterator = TreePrograms.searchProgramsDepthFirst(treeProgram1);

    List<Program> iteratedPrograms = newArrayList(programIterator);
    assertThat(iteratedPrograms, contains(treeProgram1, treeProgram2, treeProgram3, stringProgram));

  }

  @Test
  public void testToString_emptyProgram() {

    assertThat(TreePrograms.toString(EmptyProgram.emptyProgram()), equalTo(""));

  }

  @Test
  public void testToString_singleStringProgram() {

    assertThat(TreePrograms.toString(new StringProgram("+-")), equalTo("+-"));

  }

  @Test
  public void testToString_treeProgram_singleLevel() {

    StringProgram stringProgram1 = new StringProgram("+-");
    StringProgram stringProgram2 = new StringProgram(",.");
    TreeProgram treeProgram = new TreeProgram(Arrays.<Program>asList(stringProgram1, stringProgram2));

    assertThat(TreePrograms.toString(treeProgram), equalTo("+-,."));

  }

  @Test
  public void testToString_treeProgram_twoLevels() {

    StringProgram stringProgram1 = new StringProgram("+-");
    StringProgram stringProgram2 = new StringProgram(",.");
    StringProgram stringProgram3 = new StringProgram("<>");
    TreeProgram treeProgram2 = new TreeProgram(Arrays.<Program>asList(stringProgram2));
    TreeProgram treeProgram1 = new TreeProgram(asList(stringProgram1, treeProgram2, stringProgram3));

    assertThat(TreePrograms.toString(treeProgram1), equalTo("+-,.<>"));

  }

  @Test
  public void testToString_treeProgram_threeLevels() {

    StringProgram stringProgram = new StringProgram("+-");
    TreeProgram treeProgram3 = new TreeProgram(Arrays.<Program>asList(stringProgram));
    TreeProgram treeProgram2 = new TreeProgram(Arrays.<Program>asList(treeProgram3));
    TreeProgram treeProgram1 = new TreeProgram(Arrays.<Program>asList(treeProgram2));

    assertThat(TreePrograms.toString(treeProgram1), equalTo("+-"));

  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTotalStringProgramLength_emptyProgram_unsupported() {

    TreePrograms.getTotalStringProgramLength(EmptyProgram.emptyProgram());

  }

  @Test
  public void testGetTotalStringProgramLength_singleStringProgram() {

    assertThat(TreePrograms.getTotalStringProgramLength(new StringProgram("+-")), equalTo(2));

  }

  @Test
  public void testGetTotalStringProgramLength_treeProgram_singleLevel() {

    StringProgram stringProgram1 = new StringProgram("+-");
    StringProgram stringProgram2 = new StringProgram(",.");
    TreeProgram treeProgram = new TreeProgram(Arrays.<Program>asList(stringProgram1, stringProgram2));

    assertThat(TreePrograms.getTotalStringProgramLength(treeProgram), equalTo(4));

  }

  @Test
  public void testGetTotalStringProgramLength_treeProgram_twoLevels() {

    StringProgram stringProgram1 = new StringProgram("+-");
    StringProgram stringProgram2 = new StringProgram(",.");
    StringProgram stringProgram3 = new StringProgram("<>");
    TreeProgram treeProgram2 = new TreeProgram(Arrays.<Program>asList(stringProgram2));
    TreeProgram treeProgram1 = new TreeProgram(asList(stringProgram1, treeProgram2, stringProgram3));

    assertThat(TreePrograms.getTotalStringProgramLength(treeProgram1), equalTo(6));

  }

  @Test
  public void testGetTotalStringProgramLength_treeProgram_threeLevels() {

    StringProgram stringProgram = new StringProgram("+-");
    TreeProgram treeProgram3 = new TreeProgram(Arrays.<Program>asList(stringProgram));
    TreeProgram treeProgram2 = new TreeProgram(Arrays.<Program>asList(treeProgram3));
    TreeProgram treeProgram1 = new TreeProgram(Arrays.<Program>asList(treeProgram2));

    assertThat(TreePrograms.getTotalStringProgramLength(treeProgram1), equalTo(2));

  }

}