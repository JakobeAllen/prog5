package Translate;

import Tree.*;
import Temp.*;

public class IfThenElseExp extends ExpCx {
    Exp cond, a, b;
    
    public IfThenElseExp(Exp cc, Exp aa, Exp bb) {
        cond = cc;
        a = aa;
        b = bb;
    }
    
    public Tree.Stm unCx(Label t, Label f) {
        Label t1 = new Label();
        Label f1 = new Label();
        return new SEQ(cond.unCx(t1, f1),
               new SEQ(new LABEL(t1),
               new SEQ(a.unCx(t, f),
               new SEQ(new LABEL(f1),
                       b.unCx(t, f)))));
    }
    
    public Tree.Exp unEx() {
        Temp r = new Temp();
        Label t = new Label();
        Label f = new Label();
        Label join = new Label();
        return new ESEQ(new SEQ(cond.unCx(t, f),
               new SEQ(new LABEL(t),
               new SEQ(new MOVE(new TEMP(r), a.unEx()),
               new SEQ(new JUMP(join),
               new SEQ(new LABEL(f),
               new SEQ(new MOVE(new TEMP(r), b.unEx()),
                       new LABEL(join))))))),
                       new TEMP(r));
    }
}
