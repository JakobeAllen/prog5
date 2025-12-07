CS4351 - Project 4 README

Team Members:
- Jakobe Allen 
- Dillon Davis 
- Colton Elliott
- Darrius Davidson

Project Overview:
Implemented intermediate code generation and instruction selection for our C compiler. We adapted Tiger compiler components to work with C language.

Implementation:
- Tree Package: IR data structures (17 node types like BINOP, CALL, MEM, MOVE)
- Translate Package: Converts code to IR with Ex/Nx/Cx wrappers
- Canon Package: Cleans up IR trees and creates basic blocks
- Assem Package: Assembly instruction format (OPER, LABEL, MOVE)
- Mips.Codegen: Turns the code into MIPS assembly using the biggest matching patterns
- Frame Improvements: Arrays and structs now allocate correct sizes

Key Adaptations for C:
- Variable-sized data: Added 'size' field to handle bytes, shorts, ints, and long longs
- Type sizing: INT=4 bytes, CHAR=1 byte, POINTER=4 bytes, arrays and structs calculated
- Frame allocation: allocLocal() now uses actual type sizes (int[100]=400 bytes, not 4)
- No static links: C functions don’t go inside other functions.


What Works:
- All IR nodes create intermediate code correctly
- Canonicalization removes nested expressions and creates basic blocks
- Instruction selection converts IR to MIPS assembly instructions
- Frame allocation gives correct memory sizes to arrays and structs
- Type checking and semantic analysis work from Project 3

What Doesn't Work:
- Not a complete compiler yet (All Project 4 parts are done though!)
- Doesn't create final runnable assembly files
- Parts not fully connected, so the compiler can’t run everything from start to finish

How To Run:
cd /classes/cs4351/cs435132/prog5
make clean
make

Run the demo:
java Semant.Main bubblesort.c

Test array allocation:
java Semant.Main test_large_arrays.c

Test Files:
bubblesort.c - Bubble sort with arrays and loops (main demo)
test_large_arrays.c - Shows 420 bytes allocated for big arrays
test_arrays.c - Basic array operations
test_simple.c - Simple variables and arithmetic

Design Decisions:
- Size field in nodes: C has different sized data (chars vs ints), Tiger doesn't
- Type.size() method: Calculates how many bytes each type needs
- allocLocal with size: Arrays need more space than regular variables
- Maximal Munch: Pattern matches IR nodes to pick best MIPS instructions

What's Needed to Finish:
- Connect Translate to Semant so IR gets generated during type checking
- Wire Main.Main to run all phases in order
- Add runtime library for input/output

Resources:
GitHub Copilot helped us with IR node structure patterns, canonicalization algorithm, and instruction selection templates. The rest of the code was done by us and we followed all the guidelines for Project 4.