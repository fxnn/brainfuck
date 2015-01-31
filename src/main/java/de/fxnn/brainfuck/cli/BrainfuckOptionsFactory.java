package de.fxnn.brainfuck.cli;

import java.io.File;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class BrainfuckOptionsFactory {

  static final boolean HAS_NO_ARG = false;

  public static final String HELP = "help";

  public static final String PROGRAM_CHARSET = "progenc";

  public static final String INPUT_CHARSET = "inenc";

  public static final String OUTPUT_CHARSET = "outenc";

  public static final String INPUT_FILE = "infile";

  public static final String OUTPUT_FILE = "outfile";

  public static final String PROGRAM_GIVEN_AS_ARGUMENT = "progarg";

  @SuppressWarnings("AccessStaticViaInstance")
  public Options createOptions() {
    Options options = new Options();

    options.addOption("h", HELP, HAS_NO_ARG, "Display usage information.");
    options.addOption("a", PROGRAM_GIVEN_AS_ARGUMENT, HAS_NO_ARG, "Give brainfuck source as commandline argument.");

    options.addOption(OptionBuilder //
        .withLongOpt(INPUT_CHARSET) //
        .hasArg() //
        .withArgName("CHARSET") //
        .withDescription("Use given encoding for program input") //
        .create());
    options.addOption(OptionBuilder //
        .withLongOpt(OUTPUT_CHARSET) //
        .hasArg() //
        .withArgName("CHARSET") //
        .withDescription("Use given encoding for program output") //
        .create());
    options.addOption(OptionBuilder //
        .withLongOpt(PROGRAM_CHARSET) //
        .hasArg() //
        .withArgName("CHARSET") //
        .withDescription("Use given encoding for programs") //
        .create());

    options.addOption(OptionBuilder //
        .withLongOpt(INPUT_FILE) //
        .hasArg() //
        .withArgName("PATH") //
        .withDescription("Read input from file") //
        .withType(File.class) //
        .create("i"));
    options.addOption(OptionBuilder //
        .withLongOpt(OUTPUT_FILE) //
        .hasArg() //
        .withArgName("PATH") //
        .withDescription("Write output to file") //
        .withType(File.class) //
        .create("o"));

    return options;
  }

}
