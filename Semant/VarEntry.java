package Semant;

import Types.Type;
import Frame.Access;
import Translate.Exp;

// Symbol table entry for variables
public class VarEntry {
    public Type type;
    public Access access;  // Activation record access
    public Exp exp;        // Placeholder for translation
    
    public VarEntry(Type t, Access a) {
        type = t;
        access = a;
    }
    
    public String toString() {
        return "VarEntry(" + type + ", " + access + ")";
    }
}
