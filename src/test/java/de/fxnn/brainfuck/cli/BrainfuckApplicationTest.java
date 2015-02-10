package de.fxnn.brainfuck.cli;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;
import de.fxnn.brainfuck.tape.TapeEofBehaviour;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.marvinformatics.kiss.matchers.file.FileMatchers.withName;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class BrainfuckApplicationTest {

  private List<String> args;

  private ByteArrayInputStream inStream;

  private ByteArrayOutputStream outStream;

  private ByteArrayOutputStream errStream;

  private BrainfuckApplicationConfiguration brainfuckApplicationConfiguration;

  private BrainfuckProgramStartup brainfuckProgramStartup;

  @Before
  public void setUp() throws Exception {
    args = new ArrayList<>();
    inStream = new ByteArrayInputStream(new byte[0]);
    outStream = new ByteArrayOutputStream();
    errStream = new ByteArrayOutputStream();
    brainfuckApplicationConfiguration = null;
    brainfuckProgramStartup = Mockito.mock(BrainfuckProgramStartup.class);
  }

  @Test
  public void testShowsHelp_whenHelpOptionIsGiven() throws Exception {

    givenArgument("-h");

    whenSutIsInvoked();

    assertThat(errStreamByLines(), hasItem(containsString("usage:")));
    assertThat(outStreamAsString(), isEmptyString());

  }

  @Test
  public void testShowsHelp_whenNoArgumentsGiven() throws Exception {

    whenSutIsInvoked();

    assertThat(errStreamByLines(), hasItem(containsString("usage:")));

    assertThat(outStreamAsString(), isEmptyString());

  }

  @Test
  public void testProgramStartup_byCommandlineArguments() throws Exception {

    givenArgument("program 1");
    givenArgument("program 2");
    givenArgument("-");
    givenArgument("program 3");

    whenSutIsInvoked();

    Mockito.verify(brainfuckProgramStartup).startProgramFromCommandlineArgument("program 1");
    Mockito.verify(brainfuckProgramStartup).startProgramFromCommandlineArgument("program 2");
    Mockito.verify(brainfuckProgramStartup).startProgramFromCommandlineArgument("-");
    Mockito.verify(brainfuckProgramStartup).startProgramFromCommandlineArgument("program 3");
    Mockito.verify(brainfuckProgramStartup).close();
    Mockito.verifyNoMoreInteractions(brainfuckProgramStartup);

    assertThat(errStreamAsString(), isEmptyString());

  }

  @Test
  public void testTapeCharset() throws Exception {

    String charsetName = getNonDefaultCharsetName();
    givenArgument("--tapeenc=" + charsetName);
    givenArgument("some program");

    whenSutIsInvoked();

    Assert.assertEquals(charsetName, brainfuckApplicationConfiguration.getTapeCharset().name());

    assertThat(errStreamAsString(), isEmptyString());

  }

  @Test
  public void testProgramCharset() throws Exception {

    String charsetName = getNonDefaultCharsetName();
    givenArgument("--progenc=" + charsetName);
    givenArgument("some program");

    whenSutIsInvoked();

    Assert.assertEquals(charsetName, brainfuckApplicationConfiguration.getProgramCharset().name());

    assertThat(errStreamAsString(), isEmptyString());

  }

  @Test
  public void testProgramGivenAsArgument() throws Exception {

    givenArgument("--progarg");
    givenArgument("--");
    givenArgument("-+[]");

    whenSutIsInvoked();

    Assert.assertTrue(brainfuckApplicationConfiguration.isProgramGivenAsArgument());

    assertThat(errStreamAsString(), isEmptyString());

  }

  @Test
  public void testProgramGivenAsArgument_takenAsSingleProgram() throws Exception {

    givenArgument("--progarg");
    givenArgument("--");
    givenArgument("-+[]");
    givenArgument("++");

    whenSutIsInvoked();

    Mockito.verify(brainfuckProgramStartup).startProgramFromCommandlineArgument("-+[] ++");
    Mockito.verify(brainfuckProgramStartup).close();
    Mockito.verifyNoMoreInteractions(brainfuckProgramStartup);

    assertThat(errStreamAsString(), isEmptyString());

  }

  @Test
  public void testInputFile_short() throws Exception {

    givenArgument("-i");
    givenArgument("myFile.test");
    givenArgument("program");

    whenSutIsInvoked();

    Assert.assertThat(brainfuckApplicationConfiguration.getInputFile(), is(withName("myFile.test")));
    Assert.assertNull(brainfuckApplicationConfiguration.getOutputFile());

    assertThat(errStreamAsString(), isEmptyString());

  }

  @Test
  public void testInputFile_long() throws Exception {

    givenArgument("--infile=myFile.test");
    givenArgument("program");

    whenSutIsInvoked();

    Assert.assertThat(brainfuckApplicationConfiguration.getInputFile(), is(withName("myFile.test")));
    Assert.assertNull(brainfuckApplicationConfiguration.getOutputFile());

    assertThat(errStreamAsString(), isEmptyString());

  }

  @Test
  public void testOutputFile_short() throws Exception {

    givenArgument("-o");
    givenArgument("myFile.test");
    givenArgument("program");

    whenSutIsInvoked();

    Assert.assertThat(brainfuckApplicationConfiguration.getOutputFile(), is(withName("myFile.test")));
    Assert.assertNull(brainfuckApplicationConfiguration.getInputFile());

    assertThat(errStreamAsString(), isEmptyString());

  }

  @Test
  public void testOutputFile_long() throws Exception {

    givenArgument("--outfile=myFile.test");
    givenArgument("program");

    whenSutIsInvoked();

    Assert.assertThat(brainfuckApplicationConfiguration.getOutputFile(), is(withName("myFile.test")));
    Assert.assertNull(brainfuckApplicationConfiguration.getInputFile());

    assertThat(errStreamAsString(), isEmptyString());

  }

  private String getNonDefaultCharsetName() {
    if (Charset.defaultCharset().name().equals("UTF-8")) {
      return Charset.forName("latin1").name();
    }

    return Charset.forName("UTF-8").name();
  }

  @Test
  public void testEofBehaviour_default() throws Exception {

    givenArgument("program");

    whenSutIsInvoked();

    Assert.assertThat(brainfuckApplicationConfiguration.getEofBehaviour(), is(TapeEofBehaviour.READS_ZERO));

    assertThat(errStreamAsString(), isEmptyString());

  }

  @Test
  public void testEofBehaviour_unknownValueShort() throws Exception {

    givenArgument("-e");
    givenArgument("x");
    givenArgument("program");

    whenSutIsInvoked();

    assertThat(errStreamAsString(), containsString(BrainfuckOptionsFactory.EOF_BEHAVIOUR));

  }

  @Test
  public void testEofBehaviour_unknownValueLong() throws Exception {

    givenArgument("--eof=x");
    givenArgument("program");

    whenSutIsInvoked();

    assertThat(errStreamAsString(), containsString(BrainfuckOptionsFactory.EOF_BEHAVIOUR));

  }

  @Test
  public void testEofBehaviour_zeroShort() throws Exception {

    givenArgument("-e");
    givenArgument("0");
    givenArgument("program");

    whenSutIsInvoked();

    Assert.assertThat(brainfuckApplicationConfiguration.getEofBehaviour(), is(TapeEofBehaviour.READS_ZERO));

    assertThat(errStreamAsString(), isEmptyString());

  }

  @Test
  public void testEofBehaviour_zeroLong() throws Exception {

    givenArgument("--eof=0");
    givenArgument("program");

    whenSutIsInvoked();

    Assert.assertThat(brainfuckApplicationConfiguration.getEofBehaviour(), is(TapeEofBehaviour.READS_ZERO));

    assertThat(errStreamAsString(), isEmptyString());

  }

  @Test
  public void testEofBehaviour_minusOneShort() throws Exception {

    givenArgument("-e");
    givenArgument("-1");
    givenArgument("program");

    whenSutIsInvoked();

    Assert.assertThat(brainfuckApplicationConfiguration.getEofBehaviour(), is(TapeEofBehaviour.READS_MINUS_ONE));

    assertThat(errStreamAsString(), isEmptyString());

  }

  @Test
  public void testEofBehaviour_minusOneLong() throws Exception {

    givenArgument("--eof=-1");
    givenArgument("program");

    whenSutIsInvoked();

    Assert.assertThat(brainfuckApplicationConfiguration.getEofBehaviour(), is(TapeEofBehaviour.READS_MINUS_ONE));

    assertThat(errStreamAsString(), isEmptyString());

  }

  @Test
  public void testEofBehaviour_throwsShort() throws Exception {

    givenArgument("-e");
    givenArgument("TERM");
    givenArgument("program");

    whenSutIsInvoked();

    Assert.assertThat(brainfuckApplicationConfiguration.getEofBehaviour(), is(TapeEofBehaviour.THROWS));

    assertThat(errStreamAsString(), isEmptyString());

  }

  @Test
  public void testEofBehaviour_throwsLong() throws Exception {

    givenArgument("--eof=TERM");
    givenArgument("program");

    whenSutIsInvoked();

    Assert.assertThat(brainfuckApplicationConfiguration.getEofBehaviour(), is(TapeEofBehaviour.THROWS));

    assertThat(errStreamAsString(), isEmptyString());

  }

  protected void givenArgument(String argumentString) {
    args.add(argumentString);
  }

  protected Iterable<String> outStreamByLines() {
    return Splitter.on(System.lineSeparator()).trimResults().omitEmptyStrings().split(outStreamAsString());
  }

  protected Iterable<String> errStreamByLines() {
    return Splitter.on(System.lineSeparator()).trimResults().omitEmptyStrings().split(errStreamAsString());
  }

  protected String outStreamAsString() {
    return new String(outStream.toByteArray());
  }

  protected String errStreamAsString() {
    return new String(errStream.toByteArray());
  }

  protected void whenSutIsInvoked() throws IOException {
    String[] argsAsArray = args.toArray(new String[args.size()]);
    PrintStream errPrintStream = new PrintStream(errStream);

    new BrainfuckApplication(argsAsArray, inStream, outStream, errPrintStream) {

      @Override
      protected BrainfuckProgramStartup createBrainfuckProgramStartup(BrainfuckApplicationConfiguration configuration) {
        BrainfuckApplicationTest.this.brainfuckApplicationConfiguration = configuration;
        return brainfuckProgramStartup;
      }
    }.run();
  }
}