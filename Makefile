JFLAGS=-g

all: parser translate codegen

parser: Parse/Grm.java Parse/Yylex.java
	javac ${JFLAGS} Parse/*.java ErrorMsg/*.java Symbol/*.java Absyn/*.java java_cup/runtime/*.java

translate: parser
	javac ${JFLAGS} Types/*.java Util/*.java Temp/*.java Frame/*.java Tree/*.java Translate/*.java FindEscape/*.java Semant/*.java

codegen: translate
	javac ${JFLAGS} Canon/*.java Assem/*.java Mips/*.java Main/*.java

Parse/Grm.java: Parse/Grm.cup
	cd Parse; java java_cup.Main -parser Grm -expect 100 <Grm.cup >Grm.out 2>Grm.err

Parse/Yylex.java: Parse/C.lex
	cd Parse; java JLex.Main C.lex; mv C.lex.java Yylex.java

test-parser:
	java Parse.Main test1.c

test-semant:
	java Semant.Main test1.c

test-translate:
	java Translate.Main test1.c

test-main:
	java Main.Main test1.c

test-all:
	@for test in test1.c test2.c test3.c; do echo "=== Testing $$test ===" && java Semant.Main $$test && echo ""; done

clean:
	rm -f */*.class Parse/Grm.java Parse/Grm.err Parse/Grm.out Parse/Yylex.java *.s

.PHONY: all parser translate codegen test-parser test-semant test-translate test-main test-all clean
