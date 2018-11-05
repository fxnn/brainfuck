# brainfuck
My personal playground for [Brainfuck](http://en.wikipedia.org/Brainfuck) interpreters in Java. Come in different flavours.

Backed by a set of common interfaces, this Brainfuck interpreter is made to independently exchange implementations of all major components, especially

* Tape (cf. `de.fxnn.brainfuck.tape`)
* Instruction Set and Interpreter (cf. `de.fxnn.brainfuck.interpreter`)
* Program source (cf. `de.fxnn.brainfuck.program`)

Also note the [github.com/fxnn/brainfuck-on-genetics](https://github.com/fxnn/brainfuck-on-genetics) project implementing genetic algorithms on top of this interpreter.

[![Build Status](https://travis-ci.org/fxnn/brainfuck.svg)](https://travis-ci.org/fxnn/brainfuck)
[![Coverage Status](https://coveralls.io/repos/fxnn/brainfuck/badge.svg?branch=master)](https://coveralls.io/r/fxnn/brainfuck?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.fxnn/brainfuck/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.fxnn/brainfuck)

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
By default, program names are paths to files containing programs. Programs
are executed sequentially on different tapes. To read a single program
from stdin, use "-" as program name.
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

Brainfuck can be found in Maven Central.

```
<dependency>
  <groupId>de.fxnn</groupId>
  <artifactId>brainfuck</artifactId>
  <version>1.1.0</version>
</dependency>
```

## Components

* Tapes
 * infinite tape of signed 32 bit integers (cf. `InfiniteSignedIntegerTape`)
 * infinite tape of characters in all charsets supported by Java API (cf. `InfiniteCharacterTape`)
* Programs
 * source managed as single string (cf. `StringProgram`)
 * source managed in a tree whose leafs are regular string-sourced programs (cf. `TreeProgram`)

## Related Work

* This project was never designed to (and does terribly fail at) execute Brainfuck code _fast_. For this problem being solved with Java in a variety of ways, you might consider [github.com/Borisvl/brainfuck](https://github.com/Borisvl/brainfuck). (Did anyone say "bytecode"?)
* While I am also working on a project involving [machine learning](https://github.com/fxnn/brainfuck-on-genetics), I haven't been the first to come to this idea. It follows that others are more advanced in their solution. Take [github.com/primaryobjects/AI-Programmer](https://github.com/primaryobjects/AI-Programmer), who even played with Brainfuck extensions supporting functions and published some really interesting [articles](http://www.primaryobjects.com/CMS/Article149) about everything.

## License

Licensed under MIT, see for [LICENSE](LICENSE) file.
