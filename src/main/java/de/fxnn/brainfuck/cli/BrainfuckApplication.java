package de.fxnn.brainfuck.cli;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;

import com.google.common.base.Joiner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import static de.fxnn.brainfuck.cli.BrainfuckOptionsFactory.*;

public class BrainfuckApplication implements Runnable {

  static final String APPLICATION_URL = "http://github.com/fxnn/brainfuck";

  public static void main(String[] args) {

    new BrainfuckApplication(args, System.in, System.out, System.err).run();

  }

  private final String[] args;

  private final InputStream inStream;

  private final OutputStream outStream;

  private final PrintWriter errWriter;

  private final Options options;

  public BrainfuckApplication(String[] args, InputStream inStream, OutputStream outStream, PrintStream errStream) {
    this.args = args;
    this.inStream = inStream;
    this.outStream = outStream;
    this.errWriter = new PrintWriter(errStream);
    this.options = new BrainfuckOptionsFactory().createOptions();
  }

  public void run() {

    try {

      CommandLine commandLine = new PosixParser().parse(options, args);
      BrainfuckApplicationConfiguration configuration = new BrainfuckApplicationConfiguration();

      if (commandLine.getArgList().isEmpty()) {
        displayHelp();
        return;
      }

      if (commandLine.hasOption(HELP)) {
        displayHelp();
        return;
      }

      configuration.setInputCharset(readCharset(commandLine, INPUT_CHARSET));
      configuration.setOutputCharset(readCharset(commandLine, OUTPUT_CHARSET));
      configuration.setProgramCharset(readCharset(commandLine, PROGRAM_CHARSET));

      configuration.setProgramGivenAsArgument(commandLine.hasOption(PROGRAM_GIVEN_AS_ARGUMENT));

      configuration.setInputFile((File) commandLine.getParsedOptionValue(INPUT_FILE));
      configuration.setOutputFile((File) commandLine.getParsedOptionValue(OUTPUT_FILE));

      runBrainfuckPrograms(commandLine.getArgList(), configuration);

    } catch (ParseException e) {
      errWriter.println("ERROR: Could not parse commandline arguments.");
      errWriter.println(e.getMessage());
      displayHelp();

    } catch (ProgramStartupException e) {
      errWriter.println("ERROR: " + e.getMessage());

    } finally {
      errWriter.close();
    }

  }

  protected Charset readCharset(CommandLine commandLine, String opt) throws ProgramStartupException {
    String defaultValue = Charset.defaultCharset().name();
    String optionValue = commandLine.getOptionValue(opt, defaultValue);

    try {
      return Charset.forName(optionValue);

    } catch (IllegalCharsetNameException ex) {
      throw new ProgramStartupException("That is no legal charset name: " + ex.getCharsetName());

    } catch (UnsupportedCharsetException ex) {
      throw new ProgramStartupException("This charset is not supported on this JVM: " + ex.getCharsetName());
    }
  }

  protected void runBrainfuckPrograms(List<String> arguments, BrainfuckApplicationConfiguration configuration) throws ProgramStartupException {

    try (BrainfuckProgramStartup programStartup = createBrainfuckProgramStartup(configuration)) {

      if (configuration.isProgramGivenAsArgument()) {
        programStartup.startProgramFromCommandlineArgument(Joiner.on(" ").join(arguments));
      } else {
        for (String argument : arguments) {
          programStartup.startProgramFromCommandlineArgument(argument);
        }
      }

    } catch (IOException ex) {
      throw new ProgramStartupException("Error while handling i/o: " + ex.getMessage(), ex);
    }

  }

  protected BrainfuckProgramStartup createBrainfuckProgramStartup(BrainfuckApplicationConfiguration configuration) {
    return new BrainfuckProgramStartup(inStream, outStream, configuration);
  }

  protected void displayHelp() {

    HelpFormatter helpFormatter = new HelpFormatter();

    String header = APPLICATION_URL;
    String footer = "To read a program from stdin, use \"-\" as program name.";
    String usage = getExecutableName() + " [options] program1 [program2 [...]]";

    helpFormatter.printHelp(errWriter, HelpFormatter.DEFAULT_WIDTH, usage, header, options,
        HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, footer, false);

  }

  private String getExecutableName() {
    return "java -jar brainfuck.jar";
  }

}
