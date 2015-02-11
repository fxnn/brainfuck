package de.fxnn.brainfuck.tape;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

import static de.fxnn.util.UnicodeBuffers.*;

/**
 * NOTE, that a {@link java.lang.Character} is not sufficient to represent each Unicode character. Actually, two
 * chars are needed, a high and a low surrogate one. Therefore, we use {@link java.lang.Integer} here.
 * <p/>
 * <strong>Overflow</strong> is handled such that each value on tape is in the range between
 * {@link java.lang.Character#MIN_CODE_POINT} and {@link java.lang.Character#MAX_CODE_POINT}, decrementing on the
 * minimum value leads to the maximum one and vice versa.
 * <p/>
 * <strong>Reading EOF:</strong>
 * There's the special case of {@link de.fxnn.brainfuck.tape.TapeEofBehaviour#READS_MINUS_ONE}. Then, on reading EOF
 * from the input stream, the tape will contain a <code>-1</code>, which denotes no legal Character and behaves as
 * follows:
 * <ul>
 * <li>Incrementing it leads to <code>0</code>,</li>
 * <li>decrementing it leads to {@link java.lang.Character#MAX_CODE_POINT}.</li>
 * <li>On output it is treated as the <code>NUL</code> character.</li>
 * </ul>
 * <p/>
 * See the unit tests for details.
 */
public class InfiniteCharacterTape extends AbstractInfiniteTape<Integer> {

  private static final int DEFAULT_TAPE_SEGMENT_SIZE = 1024;

  private static final Integer DEFAULT_VALUE = Character.MIN_CODE_POINT;

  private final Charset charset;

  private final CharsetDecoder charsetDecoder;

  private final TapeEofBehaviour eofBehaviour;

  public InfiniteCharacterTape(Charset charset, TapeEofBehaviour eofBehaviour) {
    this.charset = charset;
    this.charsetDecoder = charset.newDecoder() //
        .onMalformedInput(CodingErrorAction.REPORT) //
        .onUnmappableCharacter(CodingErrorAction.REPORT);

    this.eofBehaviour = eofBehaviour;
  }

  @Override
  protected TapeSegment<Integer> createSegment() {
    return new TapeSegment<>(DEFAULT_TAPE_SEGMENT_SIZE, DEFAULT_VALUE);
  }

  @Override
  public void increment() {
    write(incrementCodePoint(read()));
  }

  private int incrementCodePoint(int value) {
    if (Character.isDefined(value + 1)) {
      return value + 1;
    }

    // character overflow
    return Character.MIN_CODE_POINT;
  }

  @Override
  public void decrement() {
    write(decrementCodePoint(read()));
  }

  private int decrementCodePoint(int value) {
    if (Character.isDefined(value - 1)) {
      return value - 1;
    }

    // character overflow
    return Character.MAX_CODE_POINT;
  }

  @Override
  public void readTo(DataOutput output) throws TapeIOException {
    try {
      Integer codePoint = read();

      // HINT: special check if TapeEofBehaviour.READS_MINUS_ONE is set
      if (codePoint == -1) {
        codePoint = 0;
      }

      CharBuffer charBuffer = CharBuffer.wrap(Character.toChars(codePoint));
      ByteBuffer byteBuffer = charset.encode(charBuffer);

      skipByteOrderMarks(byteBuffer, getByteOrderMarksForCharset(charset));

      // NOTE, that we CAN'T use byteBuffer.array(), as it might give us more bytes than actually there
      byte[] bytes = new byte[byteBuffer.remaining()];
      byteBuffer.get(bytes);

      output.write(bytes);
    } catch (IOException ex) {
      throw new TapeIOException("I/O error while writing from tape to output [" + output + "]: " + ex.getMessage(), ex);
    }
  }

  @Override
  public void writeFrom(DataInput input) throws TapeIOException {
    try {
      charsetDecoder.reset();

      ByteBuffer writeBuffer = ByteBuffer.allocate(4);
      CharBuffer charBuffer = (CharBuffer) CharBuffer.allocate(4).flip();

      while (!getFirstCodePoint(charBuffer).isPresent()) {
        writeBuffer.put(input.readByte());

        // HINT: wrap all bytes read so far into this new buffer
        ByteBuffer readBuffer = ByteBuffer.wrap(writeBuffer.array(), 0, writeBuffer.position());

        // HINT: clear() first as decode() always decodes everything; flip() to set position(), limit() and remaining()
        charBuffer.clear();
        CoderResult codeResult = charsetDecoder.decode(readBuffer, charBuffer, false);
        charBuffer.flip();

        if (codeResult.isOverflow() || codeResult.isError()) {
          codeResult.throwException();
        }
      }

      write(getFirstCodePoint(charBuffer).getAsInt());

    } catch (EOFException ex) {
      write(eofBehaviour.getEofValue(this, input));

    } catch (IOException ex) {
      throw new TapeIOException("I/O error while reading from input [" + input + "] to tape: " + ex.getMessage(), ex);
    }
  }

  @Override
  public boolean isZero() {
    return read().equals(Character.MIN_CODE_POINT);
  }
}
