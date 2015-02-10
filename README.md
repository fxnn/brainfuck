# brainfuck
My personal playground for [Brainfuck](http://en.wikipedia.org/Brainfuck) interpreters in Java. Come in different flavours.

Backed by a set of common interfaces, this Brainfuck interpreter is made to independently exchange implementations of all major components, especially

* Tape (cf. `de.fxnn.brainfuck.tape`)
* Instruction Set and Interpreter (cf. `de.fxnn.brainfuck.interpreter`)
* Program source (cf. `de.fxnn.brainfuck.program`)

[![Build Status](https://travis-ci.org/fxnn/brainfuck.svg)](https://travis-ci.org/fxnn/brainfuck)

## Usage

From commandline:

```
$ git clone https://github.com/fxnn/brainfuck
$ mvn package
$ java -jar target\brainfuck-1.0-SNAPSHOT-jar-with-dependencies.jar

usage: java -jar brainfuck.jar [options] program1 [program2 [...]]
http://github.com/fxnn/brainfuck
 -a,--progarg             Give brainfuck source as commandline argument.
 -e,--eof <0|-1|TERM>     Controls how to behave after reading an EOF
 -h,--help                Display usage information.
 -i,--infile <PATH>       Read input from file
 -o,--outfile <PATH>      Write output to file
    --progenc <CHARSET>   Use given encoding for programs
    --tapeenc <CHARSET>   Use given encoding for tape, input and output
To read a program from stdin, use "-" as program name.
```

Or from your JVM program:

```java
Runnable programExecutor = new ProgramExecutor(
    new StringProgram(",[->+<]>."),
    new BrainfuckInterpreter(
        new InfiniteCharacterTape(StandardCharsets.UTF_8, TapeEofBehaviour.READS_ZERO),
        javaIoDataInput,
        javaIoDataOutput
    )
);
programExecutor.run();
```

## License

Licensed under MIT, see for [LICENSE](LICENSE) file.
