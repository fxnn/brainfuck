package de.fxnn.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.io.ByteOrderMark;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.OptionalInt;

public class UnicodeBuffers {

  static final Collection<ByteOrderMark> BYTE_ORDER_MARKS = Arrays
      .asList(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE,
          ByteOrderMark.UTF_32LE);

  static final Multimap<Charset, ByteOrderMark> BYTE_ORDER_MARKS_PER_CHARSET = mapCharsetsToByteOrderMarks();

  private UnicodeBuffers() {
    // static utility class
  }

  /** Reads the first code point as defined in {@link java.lang.Character} in the charBuffer, if any. */
  public static OptionalInt getFirstCodePoint(CharBuffer charBuffer) {
    charBuffer.position(0);

    if (charBuffer.hasRemaining()) {
      char[] chars = new char[charBuffer.remaining()];
      charBuffer.get(chars);

      return OptionalInt.of(Character.codePointAt(chars, 0, chars.length));
    }

    return OptionalInt.empty();
  }

  /**
   * If the byteBuffer starts with one of the BOMs, set the buffers position behind it.
   */
  public static void skipByteOrderMarks(ByteBuffer byteBuffer, Iterable<ByteOrderMark> byteOrderMarks) {
    for (ByteOrderMark byteOrderMark : byteOrderMarks) {
      if (byteBuffer.remaining() >= byteOrderMark.length()) {

        byte[] bytes = new byte[byteOrderMark.length()];
        byteBuffer.get(bytes);

        if (Arrays.equals(bytes, byteOrderMark.getBytes())) {
          // NOTE, that we leave the byteBuffer at a position AFTER the byte order mark
          return;
        }
        byteBuffer.rewind();

      }
    }
  }

  public static Collection<ByteOrderMark> getByteOrderMarksForCharset(Charset charset) {
    return BYTE_ORDER_MARKS_PER_CHARSET.get(charset);
  }

  public static Collection<ByteOrderMark> getAllKnownByteOrderMarks() {
    return BYTE_ORDER_MARKS;
  }

  private static Multimap<Charset, ByteOrderMark> mapCharsetsToByteOrderMarks() {
    HashMultimap<Charset, ByteOrderMark> result = HashMultimap.create();

    result.put(StandardCharsets.UTF_8, ByteOrderMark.UTF_8);
    result.put(StandardCharsets.UTF_16, ByteOrderMark.UTF_16BE);
    result.put(StandardCharsets.UTF_16, ByteOrderMark.UTF_16LE);
    result.put(StandardCharsets.UTF_16BE, ByteOrderMark.UTF_16BE);
    result.put(StandardCharsets.UTF_16LE, ByteOrderMark.UTF_16LE);
    result.put(Charset.forName("UTF-32"), ByteOrderMark.UTF_32BE);
    result.put(Charset.forName("UTF-32"), ByteOrderMark.UTF_32LE);
    result.put(Charset.forName("UTF-32BE"), ByteOrderMark.UTF_32BE);
    result.put(Charset.forName("UTF-32LE"), ByteOrderMark.UTF_32LE);

    return result;
  }

}
