package de.fxnn.brainfuck.tape;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.nio.charset.Charset;

import com.google.common.base.Charsets;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class InfiniteCharacterTapeTest {

  private Charset tapeCharset;

  private TapeEofBehaviour eofBehaviour;

  private InfiniteCharacterTape sut;

  private String inputString;

  private ByteArrayOutputStream byteArrayOutputStream;

  @Before
  public void setUp() {
    sut = null;
    tapeCharset = Charset.defaultCharset();
    eofBehaviour = TapeEofBehaviour.THROWS;
  }

  @Test
  public void testIsInitiallyZero() {

    assertTrue(getSut().isZero());

  }

  @Test
  public void testValidIncrement() {

    getSut().increment();

    assertThat(getSut().read(), is(1));
    assertFalse(getSut().isZero());

  }

  @Test
  public void testValidDecrement() {

    getSut().write(1);

    getSut().decrement();

    assertThat(getSut().read(), is(0));
    assertTrue(getSut().isZero());

  }

  @Test
  public void testOverflowDecrement() {

    getSut().decrement();

    assertThat(getSut().read(), is(Character.MAX_CODE_POINT));
    assertFalse(getSut().isZero());

  }

  @Test
  public void testOverflowIncrement() {

    getSut().write(Character.MAX_CODE_POINT);

    getSut().increment();

    assertThat(getSut().read(), is(Character.MIN_CODE_POINT));
    assertTrue(getSut().isZero());

  }

  @Test
  public void testInputInUtf8() throws Exception {

    givenTapeCharset(Charsets.UTF_8);
    givenInputString("0");

    getSut().writeFrom(dataInput());

    assertThat(getSut().read(), is(0x30));

  }

  @Test
  public void testOutputInUtf8() throws Exception {

    givenTapeCharset(Charsets.UTF_8);
    getSut().write(0x30);

    getSut().readTo(dataOutput());

    assertArrayEquals(new byte[]{0x30}, getOutputBytes());
    assertEquals("0", getOutputString());

  }

  @Test
  public void testInputInUtf16() throws Exception {

    givenTapeCharset(Charsets.UTF_16);
    givenInputString("0");

    getSut().writeFrom(dataInput());

    assertThat(getSut().read(), is(0x0030));

  }

  @Test
  public void testOutputInUtf16() throws Exception {

    givenTapeCharset(Charsets.UTF_16);
    getSut().write(0x0030);

    getSut().readTo(dataOutput());

    assertArrayEquals(new byte[]{0x00, 0x30}, getOutputBytes());
    assertEquals("0", getOutputString());

  }

  @Test(expected = TapeIOException.class)
  public void testInputEofThrows() throws Exception {

    givenInputString("");
    givenEofBehaviour(TapeEofBehaviour.THROWS);

    getSut().writeFrom(dataInput());

  }

  @Test
  public void testInputEofReadsZero() throws Exception {

    givenInputString("");
    givenEofBehaviour(TapeEofBehaviour.READS_ZERO);

    getSut().writeFrom(dataInput());

    assertThat(getSut().read(), is(0));

  }

  @Test
  public void testInputEofReadsMinusOne() throws Exception {

    givenInputString("");
    givenEofBehaviour(TapeEofBehaviour.READS_MINUS_ONE);

    getSut().writeFrom(dataInput());

    assertThat(getSut().read(), is(-1));

  }

  /**
   * NOTE, that returning -1 leads to an illegal unicode character, so we need to check what happens with it
   */
  @Test
  public void testIncrementAfterEofReadsMinusOne() throws Exception {

    givenInputString("");
    givenEofBehaviour(TapeEofBehaviour.READS_MINUS_ONE);

    getSut().writeFrom(dataInput());
    getSut().increment();

    assertThat(getSut().read(), is(0));

  }

  /**
   * NOTE, that returning -1 leads to an illegal unicode character, so we need to check what happens with it
   */
  @Test
  public void testDecrementAfterEofReadsMinusOne() throws Exception {

    givenInputString("");
    givenEofBehaviour(TapeEofBehaviour.READS_MINUS_ONE);

    getSut().writeFrom(dataInput());
    getSut().decrement();

    assertThat(getSut().read(), is(Character.MAX_CODE_POINT));

  }

  /**
   * NOTE, that returning -1 leads to an illegal unicode character, so we need to check what happens with it
   */
  @Test
  public void testOutputAfterEofReadsMinusOne() throws Exception {

    givenInputString("");
    givenEofBehaviour(TapeEofBehaviour.READS_MINUS_ONE);

    getSut().writeFrom(dataInput());
    getSut().readTo(dataOutput());

    assertArrayEquals(getOutputBytes(), new byte[]{0});

  }

  protected void givenEofBehaviour(TapeEofBehaviour eofBehaviour) {
    this.eofBehaviour = eofBehaviour;
  }

  protected void givenInputString(String inputString) {
    this.inputString = inputString;
  }

  protected void givenTapeCharset(Charset charset) {
    tapeCharset = charset;
  }

  protected InfiniteCharacterTape getSut() {
    if (sut == null) {
      sut = new InfiniteCharacterTape(tapeCharset, eofBehaviour);
    }
    return sut;
  }

  protected byte[] getOutputBytes() {
    return byteArrayOutputStream.toByteArray();
  }

  protected String getOutputString() {
    return new String(byteArrayOutputStream.toByteArray(), tapeCharset);
  }

  protected DataOutput dataOutput() {
    byteArrayOutputStream = new ByteArrayOutputStream();
    return new DataOutputStream(byteArrayOutputStream);
  }

  protected DataInput dataInput() {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputString.getBytes(tapeCharset));
    BOMInputStream bomInputStream = new BOMInputStream(byteArrayInputStream, ByteOrderMark.UTF_8,
        ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE);
    return new DataInputStream(bomInputStream);
  }
}