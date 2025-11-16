JFLAGS=-g

all: parser semant

parser: Parse/Grm.java Parse/Yylex.java
	javac ${JFLAGS} Parse/*.java ErrorMsg/*.java Symbol/*.java Absyn/*.java java_cup/runtime/*.java

semant: parser
	javac ${JFLAGS} Types/*.java Translate/*.java Util/*.java Temp/*.java Frame/*.java Mips/*.java FindEscape/*.java Semant/*.java

Parse/Grm.java: Parse/Grm.cup
	cd Parse; java java_cup.Main -parser Grm -expect 100 <Grm.cup >Grm.out 2>Grm.err

Parse/Yylex.java: Parse/C.lex
	cd Parse; java JLex.Main C.lex; mv C.lex.java Yylex.java

test-parser:
	java Parse.Main test1.c

test-semant:
	java Semant.Main test1.c

test-all:
	@for test in test1.c test2.c test3.c test_complete.c; do echo "=== Testing $$test ===" && java Semant.Main $$test && echo ""; done

clean:
	rm -f */*.class Parse/Grm.java Parse/Grm.err Parse/Grm.out Parse/Yylex.java

.PHONY: all parser semant test-parser test-semant test-all clean
