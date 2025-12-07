package Main;

import java.io.*;
import Parse.Parse;
import Semant.Semant;
import Canon.*;
import Assem.*;
import Mips.Codegen;
import Tree.*;
import Temp.*;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Main.Main <filename>");
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
            
            String outname = filename.substring(0, filename.lastIndexOf('.')) + ".s";
            PrintWriter out = new PrintWriter(new FileWriter(outname));
            
            out.println("# Compilation output for " + filename);
            out.println();
            
            out.close();
            System.out.println("Compiled successfully to " + outname);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
