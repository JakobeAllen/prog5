package Semant;

import Parse.Parse;
import Absyn.Program;
import ErrorMsg.ErrorMsg;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java Semant.Main <source file>");
            System.exit(1);
        }
        
        String filename = args[0];
        Parse parser = new Parse(filename);
        
        if (parser.errorMsg.anyErrors) {
            System.err.println("Parsing failed with errors");
            System.exit(1);
        }
        
        System.out.println("=== Abstract Syntax Tree ===");
        if (parser.absyn != null) {
            System.out.println(parser.absyn.toString());
        }
        System.out.println();
        
        System.out.println("=== Semantic Analysis ===");
        Semant semantic = new Semant(parser.errorMsg);
        semantic.transProg(parser.absyn);
        
        if (parser.errorMsg.anyErrors) {
            System.err.println("\nSemantic analysis failed with errors");
            System.exit(1);
        }
        
        System.out.println("Semantic analysis completed successfully");
        System.out.println();
        
        System.out.println("=== Annotated AST (with escape and frame info) ===");
        if (parser.absyn != null) {
            System.out.println(parser.absyn.toString());
        }
    }
}
