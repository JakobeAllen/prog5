# C Compiler

## About
A C compiler that translates C source code into MIPS assembly. Includes lexical analysis, parsing, type checking, intermediate code generation, and instruction selection.

**Team:** Jakobe Allen, Dillon Davis, Colton Elliott, Darrius Davidson

---

## How to Run

### Build
```bash
cd /classes/cs4351/cs435132/prog5
make clean
make
```

### Test
```bash
# Run bubble sort example
java Semant.Main bubblesort.c

# Test array memory allocation
java Semant.Main test_large_arrays.c

# Test type error detection
java Semant.Main type_error_test.c
```

---

## Features

- Type checking for int, char, pointers, arrays, structs, and unions
- Control flow: if/else, while, for, break, continue
- Function declarations and calls
- Proper memory allocation for arrays and structs
- Error detection with meaningful messages

---

## Implementation

**Core Components:**
- Lexer and parser for C syntax
- Type checker with symbol table
- IR generation (BINOP, CALL, MEM, MOVE, etc.)
- Canonicalization and basic block formation
- MIPS instruction selection
- Frame management with proper type sizing
