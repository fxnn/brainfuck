package de.fxnn.brainfuck.cli;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;
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

    assertThat(errStreamByLines(), hasItem("No arguments given."));
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
  public void testInputCharset() throws Exception {

    String charsetName = getNonDefaultCharsetName();
    givenArgument("--inenc=" + charsetName);
    givenArgument("some program");

    whenSutIsInvoked();

    Assert.assertEquals(charsetName, brainfuckApplicationConfiguration.getInputCharset().name());

    assertThat(errStreamAsString(), isEmptyString());

  }

  @Test
  public void testOutputCharset() throws Exception {

    String charsetName = getNonDefaultCharsetName();
    givenArgument("--outenc=" + charsetName);
    givenArgument("some program");

    whenSutIsInvoked();

    Assert.assertEquals(charsetName, brainfuckApplicationConfiguration.getOutputCharset().name());

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