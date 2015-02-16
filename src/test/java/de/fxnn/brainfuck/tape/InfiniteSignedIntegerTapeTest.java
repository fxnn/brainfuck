package de.fxnn.brainfuck.tape;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class InfiniteSignedIntegerTapeTest {

  InfiniteSignedIntegerTape sut;

  DataOutput output;

  DataInput input;

  @Before
  public void setUp() {
    sut = new InfiniteSignedIntegerTape(TapeEofBehaviour.THROWS);
    output = ByteStreams.newDataOutput();
    input = new DataInputStream(new ByteArrayInputStream(new byte[0]));
  }

  @Test
  public void testInitiallyZero() {

    Assert.assertTrue(sut.isZero());

  }

  @Test
  public void testNotZeroAfterIncrement() {

    sut.increment();

    Assert.assertFalse(sut.isZero());

  }

  @Test
  public void testNotZeroAfterDecrement() {

    sut.decrement();

    Assert.assertFalse(sut.isZero());

  }

  @Test
  public void testMovement() throws Exception {

    sut.write(1);
    sut.moveForward();
    sut.write(2);
    sut.moveBackward();

    Assert.assertEquals(1, (int) sut.read());

  }

  @Test
  public void testMovementOverSegmentBorders() throws Exception {

    sut.write(1);
    sut.moveBackward();
    sut.write(2);
    sut.moveForward();

    Assert.assertEquals(1, (int) sut.read());

  }

  @Test
  public void testOutputInitially() throws Exception {

    sut.readTo(output);

    Assert.assertArrayEquals(new byte[]{0, 0, 0, 0}, outputAsByteArray());

  }

  @Test
  public void testOutputAfterIncrement() throws Exception {

    sut.increment();
    sut.readTo(output);

    Assert.assertArrayEquals(new byte[]{0, 0, 0, 1}, outputAsByteArray());

  }

  @Test
  public void testOutputAfterDecrement() throws Exception {

    sut.decrement();
    sut.readTo(output);

    Assert.assertArrayEquals(new byte[]{-1, -1, -1, -1}, outputAsByteArray());

  }

  @Test(expected = TapeIOException.class)
  public void testInputAfterEof_throws() throws Exception {

    givenEofBehaviour(TapeEofBehaviour.THROWS);

    sut.writeFrom(input);

  }

  @Test
  public void testInputAfterEof_writesZero() throws Exception {

    givenEofBehaviour(TapeEofBehaviour.READS_ZERO);

    sut.writeFrom(input);

    Assert.assertEquals(0, (int) sut.read());

  }

  @Test
  public void testInputAfterEof_writesMinusOne() throws Exception {

    givenEofBehaviour(TapeEofBehaviour.READS_MINUS_ONE);

    sut.writeFrom(input);

    Assert.assertEquals(-1, (int) sut.read());

  }

  @Test(expected = TapeIOException.class)
  public void testIoExceptionOnInput() throws Exception {

    input = mock(DataInput.class);
    doThrow(IOException.class).when(input).readInt();

    sut.writeFrom(input);

  }

  @Test(expected = TapeIOException.class)
  public void testIoExceptionOnOutput() throws Exception {

    output = mock(ByteArrayDataOutput.class);
    doThrow(IOException.class).when(output).writeInt(anyInt());

    sut.readTo(output);

  }

  protected void givenEofBehaviour(TapeEofBehaviour eofBehaviour) {
    sut = new InfiniteSignedIntegerTape(eofBehaviour);
  }

  protected byte[] outputAsByteArray() {
    return ((ByteArrayDataOutput) output).toByteArray();
  }

}