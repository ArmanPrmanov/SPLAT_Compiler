# SPLAT Compiler

A complete implementation of the Basic SPLAT (Software Principles Language And Tools) programming language compiler, developed as part of CSCI 501/701 Advanced Software Principles and Practice course.

## Overview

SPLAT is a simple imperative programming language with support for:

- **Data Types**: Integer, Boolean, String
- **Control Structures**: If-then-else statements, while loops
- **Functions**: Both void and non-void functions with parameters
- **Variables**: Global and local variable declarations
- **Operations**: Arithmetic, logical, and comparison operations
- **I/O**: Print statements for console output

## Project Structure

```
src/splat/
├── Splat.java                    # Main compiler class
├── SplatTester.java             # Test runner
├── SplatException.java          # Base exception class
├── lexer/                       # Phase 1: Lexical Analysis
│   ├── Lexer.java              # Tokenizer implementation
│   ├── Token.java              # Token representation
│   └── LexException.java       # Lexical analysis exceptions
├── parser/                      # Phase 2: Syntax Analysis
│   ├── Parser.java             # Recursive descent parser
│   ├── ParseException.java     # Parsing exceptions
│   └── elements/               # AST node classes
│       ├── ASTElement.java     # Base AST class
│       ├── ProgramAST.java     # Program root node
│       ├── Statement.java      # Statement interface
│       ├── Expression.java     # Expression interface
│       ├── Declaration.java    # Declaration interface
│       ├── FunctionDecl.java   # Function declarations
│       ├── VariableDecl.java   # Variable declarations
│       └── [Other AST nodes]   # Various language constructs
├── semanticanalyzer/            # Phase 3: Semantic Analysis
│   ├── SemanticAnalyzer.java   # Type checker and semantic analyzer
│   └── SemanticAnalysisException.java
└── executor/                    # Phase 4: Code Execution
    ├── Executor.java           # Interpreter implementation
    ├── Value.java              # Runtime value representation
    ├── ExecutionException.java # Execution exceptions
    └── ReturnFromCall.java     # Function return mechanism
```

## Language Features

### Data Types

- **Integer**: 32-bit signed integers
- **Boolean**: true/false values
- **String**: Character sequences (no escape characters)

### Variable Declarations

```splat
program
    x: Integer;
    flag: Boolean;
    message: String;
begin
    // program body
end;
```

### Function Declarations

```splat
program
    // Void function
    printMessage() : void is
    begin
        print "Hello World";
    end;

    // Non-void function
    add(x: Integer, y: Integer) : Integer is
    begin
        return (x + y);
    end;
begin
    // program body
end;
```

### Control Structures

```splat
// If-then-else
if (x > 0) then
    print "Positive";
else
    print "Non-positive";
end if;

// While loop
while (x > 0) do
    x := (x - 1);
    print x;
end while;
```

### Expressions

All expressions must be parenthesized to avoid ambiguity:

```splat
// Arithmetic
result := ((x + y) * z);

// Logical
flag := ((x > 0) and (y < 10));

// Comparison
isEqual := ((x == y));
```

### Print Statements

```splat
print (x + y);        // Print expression value
print "Hello";        // Print string literal
print_line;           // Print newline
```

## Compilation Process

The SPLAT compiler follows a traditional four-phase compilation process:

### Phase 1: Lexical Analysis

- Converts source code into a stream of tokens
- Handles whitespace, comments, and token separation
- Tracks line and column numbers for error reporting

### Phase 2: Syntax Analysis

- Uses recursive descent parsing
- Builds Abstract Syntax Tree (AST) from tokens
- Validates syntax according to SPLAT grammar

### Phase 3: Semantic Analysis

- Performs type checking
- Validates variable and function declarations
- Ensures proper scope and usage rules
- Checks function call signatures

### Phase 4: Execution

- Interprets the AST to execute the program
- Manages variable state and function calls
- Handles runtime errors and exceptions

## Building and Running

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, etc.)

### Compilation

```bash
# Compile the project
javac -d out/production/lex_start -cp src src/splat/*.java src/splat/*/*.java src/splat/*/*/*.java
```

### Running Tests

```bash
# Run the test suite
java -cp out/production/lex_start splat.SplatTester
```

### Running Individual Programs

```bash
# Compile and run a SPLAT program
java -cp out/production/lex_start splat.Splat path/to/program.splat
```

## Test Suite

The project includes 102 comprehensive test cases covering:

- **Lexical errors** (`*_badlex.splat`): Invalid tokens, unterminated strings
- **Parse errors** (`*_badparse.splat`): Syntax violations, missing tokens
- **Semantic errors** (`*_badsemantics.splat`): Type mismatches, undefined variables
- **Execution errors** (`*_badexecution.splat`): Runtime exceptions, division by zero
- **Successful execution** (`*_goodexecution.splat`): Valid programs with expected output

## Language Grammar

```
<program> ::= program <decls> begin <stmts> end;

<decls> ::= <decl> <decls> | ε
<decl> ::= <var-decl> | <func-decl>

<var-decl> ::= <label> : <type> ;
<func-decl> ::= <label> ( <params> ) : <ret-type> is <loc-var-decls> begin <stmts> end ;

<stmt> ::= <label> := <expr> ;
       | while <expr> do <stmts> end while ;
       | if <expr> then <stmts> else <stmts> end if ;
       | if <expr> then <stmts> end if ;
       | <label> ( <args> ) ;
       | print <expr> ;
       | print_line ;
       | return <expr> ;
       | return ;

<expr> ::= ( <expr> <bin-op> <expr> )
       | ( <unary-op> <expr> )
       | <label> ( <args> )
       | <label>
       | <literal>

<bin-op> ::= and | or | > | < | == | >= | <= | + | - | * | / | %
<unary-op> ::= not | -

<type> ::= Integer | Boolean | String
<ret-type> ::= <type> | void
```

## Error Handling

The compiler provides detailed error reporting with:

- **Location information**: Line and column numbers
- **Error categorization**: Lexical, syntax, semantic, or execution errors
- **Descriptive messages**: Clear explanation of what went wrong

## Example Program

```splat
program
    // Function to calculate factorial
    factorial(n: Integer) : Integer is
    begin
        if (n <= 1) then
            return 1;
        else
            return (n * factorial((n - 1)));
        end if;
    end;

    // Main program
    num: Integer;
    result: Integer;
begin
    num := 5;
    result := factorial(num);
    print "Factorial of ";
    print num;
    print " is ";
    print result;
    print_line;
end;
```

## Development Phases

This project was developed in four phases:

1. **Phase 1**: Lexical analysis and tokenization
2. **Phase 2**: Syntax analysis and AST construction
3. **Phase 3**: Semantic analysis and type checking
4. **Phase 4**: Code execution and interpretation

## Academic Context

This project was developed as part of CSCI 501/701 (Advanced) Software Principles and Practice course, demonstrating:

- Compiler design principles
- Lexical analysis techniques
- Recursive descent parsing
- Semantic analysis and type checking
- Abstract syntax tree construction
- Interpreter implementation
- Software engineering best practices

## License

This project is developed for educational purposes as part of academic coursework.
