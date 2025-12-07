package Assem;

import Temp.*;

public class LABEL extends Instr {
    public Label lab;
    
    public LABEL(String a, Label l) {
        assem = a;
        lab = l;
    }
    
    public String format(TempMap m) {
        return assem;
    }
}
