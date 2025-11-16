CS4351 - Project 3 README

Team Members:
- Jakobe Allen 
- Dillon Davis 
- Colton Elliott
- Darrius Davidson

Project Overview:
Implemented semantic analysis, escape analysis, and activation records to our C compiler. We also extended the parser to support structs, unions, and array initializers.

Implementation:
- Semantic analysis with full type checking
- Escape analysis (FindEscape)
- Activation records (MipsFrame and allocLocal)
- Updated C type rules (INT/CHAR split, INT to POINTER)
- Structs, unions, and array initializers
- Test cases from TA

Type Rules Implemented:
- INT and CHAR are separate types
- INT can coerce to any pointer type
- Arithmetic operations require INT

How To Run:
cd /classes/cs4351/cs435132/prog3
make clean
make
java Semant.Main test1.c

Run ta’s test cases:
./run_tests.sh typechecking_tests

Run our tests:
java Semant.Main test1.c
java Semant.Main test2.c
java Semant.Main test3.c

Test Files:
test1.c - Basic type rules (Pass)
test2.c - Type errors (Fail)
test3.c - Structs, arrays, and functions (Pass)
typechecking_tests/ - TA’s test cases (4 Pass, 4 Fail)

Resources:
GitHub Copilot helped us with grammar extensions, type checking patterns, and test creation. The rest of the code was done by us and we try to make sure we follow all the guidelines for Project 3.