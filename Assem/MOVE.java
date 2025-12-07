package Assem;

import Temp.*;

public class MOVE extends Instr {
    public TempList src;
    public TempList dst;
    
    public MOVE(String a, TempList d, TempList s) {
        assem = a;
        dst = d;
        src = s;
    }
    
    public String format(TempMap m) {
        return format(assem, src, dst, m);
    }
}
