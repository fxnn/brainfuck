# brainfuck
My personal playground for [Brainfuck](http://en.wikipedia.org/Brainfuck) interpreters in Java. Come in different flavours.

Backed by a set of common interfaces, this Brainfuck interpreter is made to independently exchange implementations of all major components, especially

* Tape (cf. `de.fxnn.brainfuck.tape`)
* Instruction Set and Interpreter (cf. `de.fxnn.brainfuck.interpreter`)
* Program source (cf. `de.fxnn.brainfuck.program`)

[![Build Status](https://travis-ci.org/fxnn/brainfuck.svg)](https://travis-ci.org/fxnn/brainfuck)

## Ideas

* Brainfuck programs preprocessed by context free / context sensitive grammars
* Brainfuck programs where tape and program source are identical

## License

Licensed under MIT, see for [LICENSE](LICENSE) file.
