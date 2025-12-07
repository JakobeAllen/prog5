package Assem;

import Temp.*;

public class OPER extends Instr {
    public TempList src;
    public TempList dst;
    public LabelList jump;
    
    public OPER(String a, TempList d, TempList s, LabelList j) {
        assem = a;
        dst = d;
        src = s;
        jump = j;
    }
    
    public OPER(String a, TempList d, TempList s) {
        this(a, d, s, null);
    }
    
    public String format(TempMap m) {
        return format(assem, src, dst, jump, m);
    }
}
