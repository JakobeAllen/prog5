package Translate;

import java.io.*;
import Parse.Parse;
import Semant.Semant;
import Tree.*;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Translate.Main <filename>");
            System.exit(1);
        }
        
        String filename = args[0];
        
        try {
            Parse parse = new Parse(filename);
            ErrorMsg.ErrorMsg errorMsg = parse.errorMsg;
            Absyn.Program prog = parse.absyn;
            
            if (prog == null || errorMsg.anyErrors) {
                System.err.println("Syntax errors detected");
                System.exit(1);
            }
            
            Semant semant = new Semant(errorMsg);
            semant.transProg(prog);
            
            if (errorMsg.anyErrors) {
                System.err.println("Semantic errors detected");
                System.exit(1);
            }
            
            System.out.println("Translation to IR successful");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
