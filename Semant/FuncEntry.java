package Semant;

import Types.Type;
import Types.FUNCTION;
import Temp.Label;
import Mips.MipsFrame;

// Symbol table entry for functions
public class FuncEntry {
    public FUNCTION type;
    public Label label;
    public MipsFrame frame;  // Activation record
    
    public FuncEntry(FUNCTION t, Label l, MipsFrame f) {
        type = t;
        label = l;
        frame = f;
    }
    
    public String toString() {
        return "FuncEntry(" + type + ", " + label + ")";
    }
}
