package de.fxnn.brainfuck.simple;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InfiniteSignedIntegerTapeTest {

  InfiniteSignedIntegerTape sut;

  BufferedWriter outputStreamWriter;

  ByteArrayOutputStream outputStream;

  @Before
  public void setUp() {
    sut = new InfiniteSignedIntegerTape();
    outputStream = new ByteArrayOutputStream();
    outputStreamWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
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
  public void testReadInitially() throws Exception {

    sut.readTo(outputStreamWriter);
    outputStreamWriter.close();

    Assert.assertEquals(1, outputStream.size());
    Assert.assertArrayEquals(new byte[]{0}, outputStream.toByteArray());

  }

  @Test
  public void testReadAfterIncrement() throws Exception {

    sut.increment();
    sut.readTo(outputStreamWriter);
    outputStreamWriter.close();

    Assert.assertEquals(1, outputStream.size());
    Assert.assertArrayEquals(new byte[]{1}, outputStream.toByteArray());

  }

  @Test
  public void testReadAfterDecrement() throws Exception {

    sut.decrement();
    sut.readTo(outputStreamWriter);
    outputStreamWriter.close();

    // TODO: Tapes sollten lieber auf Zeichenklassen (Unicode, ASCII etc.) basieren
    Assert.assertArrayEquals(new byte[]{-17, -65, -65}, outputStream.toByteArray());

  }

}