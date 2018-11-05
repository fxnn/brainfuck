package de.fxnn.brainfuck.tape;

import com.google.common.base.Charsets;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

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

    assertTrue(sut().isZero());

  }

  @Test
  public void testValidIncrement() {

    sut().increment();

    assertThat(sut().read(), is(1));
    assertFalse(sut().isZero());

  }

  @Test
  public void testValidDecrement() {

    sut().write(1);

    sut().decrement();

    assertThat(sut().read(), is(0));
    assertTrue(sut().isZero());

  }

  @Test
  public void testOverflowDecrement() {

    sut().decrement();

    assertThat(sut().read(), is(Character.MAX_CODE_POINT));
    assertFalse(sut().isZero());

  }

  @Test
  public void testOverflowIncrement() {

    sut().write(Character.MAX_CODE_POINT);

    sut().increment();

    assertThat(sut().read(), is(Character.MIN_CODE_POINT));
    assertTrue(sut().isZero());

  }

  @Test
  public void testInputInUtf8() throws Exception {

    givenTapeCharset(Charsets.UTF_8);
    givenDataInputAsString("0");

    sut().writeFrom(dataInput());

    assertThat(sut().read(), is(0x30));

  }

  @Test
  public void testOutputInUtf8() throws Exception {

    givenTapeCharset(Charsets.UTF_8);
    sut().write(0x30);

    sut().readTo(dataOutput());

    assertArrayEquals(new byte[]{0x30}, dataOutputAsBytes());
    assertEquals("0", dataOutputAsString());

  }

  @Test
  public void testInputInUtf16() throws Exception {

    givenTapeCharset(Charsets.UTF_16);
    givenDataInputAsString("0");

    sut().writeFrom(dataInput());

    assertThat(sut().read(), is(0x0030));

  }

  @Test
  public void testOutputInUtf16() throws Exception {

    givenTapeCharset(Charsets.UTF_16);
    sut().write(0x0030);

    sut().readTo(dataOutput());

    assertArrayEquals(new byte[]{0x00, 0x30}, dataOutputAsBytes());
    assertEquals("0", dataOutputAsString());

  }

  @Test(expected = TapeIOException.class)
  public void testInputEofThrows() throws Exception {

    givenEmptyDataInput();
    givenEofBehaviour(TapeEofBehaviour.THROWS);

    sut().writeFrom(dataInput());

  }

  @Test
  public void testInputEofReadsZero() throws Exception {

    givenEmptyDataInput();
    givenEofBehaviour(TapeEofBehaviour.READS_ZERO);

    sut().writeFrom(dataInput());

    assertThat(sut().read(), is(0));

  }

  @Test
  public void testInputEofReadsMinusOne() throws Exception {

    givenEmptyDataInput();
    givenEofBehaviour(TapeEofBehaviour.READS_MINUS_ONE);

    sut().writeFrom(dataInput());

    assertThat(sut().read(), is(-1));

  }

  /**
   * NOTE, that returning -1 leads to an illegal unicode character, so we need to check what happens with it
   */
  @Test
  public void testIncrementAfterEofReadsMinusOne() throws Exception {

    givenEmptyDataInput();
    givenEofBehaviour(TapeEofBehaviour.READS_MINUS_ONE);

    sut().writeFrom(dataInput());
    sut().increment();

    assertThat(sut().read(), is(0));

  }

  /**
   * NOTE, that returning -1 leads to an illegal unicode character, so we need to check what happens with it
   */
  @Test
  public void testDecrementAfterEofReadsMinusOne() throws Exception {

    givenEmptyDataInput();
    givenEofBehaviour(TapeEofBehaviour.READS_MINUS_ONE);

    sut().writeFrom(dataInput());
    sut().decrement();

    assertThat(sut().read(), is(Character.MAX_CODE_POINT));

  }

  /**
   * NOTE, that returning -1 leads to an illegal unicode character, so we need to check what happens with it
   */
  @Test
  public void testOutputAfterEofReadsMinusOne() throws Exception {

    givenEmptyDataInput();
    givenEofBehaviour(TapeEofBehaviour.READS_MINUS_ONE);

    sut().writeFrom(dataInput());
    sut().readTo(dataOutput());

    assertArrayEquals(dataOutputAsBytes(), new byte[]{0});

  }

  protected void givenEofBehaviour(TapeEofBehaviour eofBehaviour) {
    this.eofBehaviour = eofBehaviour;
  }

  protected void givenEmptyDataInput() {
    givenDataInputAsString("");
  }

  protected void givenDataInputAsString(String inputString) {
    this.inputString = inputString;
  }

  protected void givenTapeCharset(Charset charset) {
    tapeCharset = charset;
  }

  protected InfiniteCharacterTape sut() {
    if (sut == null) {
      sut = new InfiniteCharacterTape(tapeCharset, eofBehaviour);
    }
    return sut;
  }

  protected byte[] dataOutputAsBytes() {
    return byteArrayOutputStream.toByteArray();
  }

  protected String dataOutputAsString() {
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