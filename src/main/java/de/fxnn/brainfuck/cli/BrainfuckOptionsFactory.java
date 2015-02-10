package de.fxnn.brainfuck.cli;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.base.Joiner;
import de.fxnn.brainfuck.tape.TapeEofBehaviour;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class BrainfuckOptionsFactory {

  static final boolean HAS_NO_ARG = false;

  public static final String HELP = "help";

  public static final String PROGRAM_CHARSET = "progenc";

  public static final String TAPE_CHARSET = "tapeenc";

  public static final String INPUT_FILE = "infile";

  public static final String OUTPUT_FILE = "outfile";

  public static final String EOF_BEHAVIOUR = "eof";

  public static final Map<String, TapeEofBehaviour> EOF_BEHAVIOUR_ARGUMENTS = createEofBehaviourArgumentsMap();

  public static final String PROGRAM_GIVEN_AS_ARGUMENT = "progarg";

  @SuppressWarnings("AccessStaticViaInstance")
  public Options createOptions() {
    Options options = new Options();

    options.addOption("h", HELP, HAS_NO_ARG, "Display usage information.");
    options.addOption("a", PROGRAM_GIVEN_AS_ARGUMENT, HAS_NO_ARG, "Give brainfuck source as commandline argument.");

    options.addOption(OptionBuilder //
        .withLongOpt(TAPE_CHARSET) //
        .hasArg() //
        .withArgName("CHARSET") //
        .withDescription("Use given encoding for tape, input and output") //
        .create());
    options.addOption(OptionBuilder //
        .withLongOpt(PROGRAM_CHARSET) //
        .hasArg() //
        .withArgName("CHARSET") //
        .withDescription("Use given encoding for programs") //
        .create());

    options.addOption(OptionBuilder //
        .withLongOpt(EOF_BEHAVIOUR) //
        .hasArg() //
        .withArgName(Joiner.on("|").join(EOF_BEHAVIOUR_ARGUMENTS.keySet())) //
        .withDescription("Controls how to behave after reading an EOF") //
        .create("e"));

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

  public static TapeEofBehaviour eofBehaviourFromArgument(@Nullable String value, TapeEofBehaviour defaultValue) throws ProgramStartupException {
    if (value == null) {
      return defaultValue;
    }

    if (EOF_BEHAVIOUR_ARGUMENTS.containsKey(value)) {
      return EOF_BEHAVIOUR_ARGUMENTS.get(value);
    }

    throw new ProgramStartupException("Unknown value for the --" + EOF_BEHAVIOUR + " option: " + value);
  }

  private static Map<String, TapeEofBehaviour> createEofBehaviourArgumentsMap() {
    Map<String, TapeEofBehaviour> result = new HashMap<>();

    result.put("-1", TapeEofBehaviour.READS_MINUS_ONE);
    result.put("0", TapeEofBehaviour.READS_ZERO);
    result.put("TERM", TapeEofBehaviour.THROWS);

    return result;
  }

}
