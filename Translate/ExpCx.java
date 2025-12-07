package Translate;

import Tree.*;
import Temp.*;

public abstract class ExpCx extends Exp {
    public Tree.Exp unEx() {
        Temp r = new Temp();
        Label t = new Label();
        Label f = new Label();
        return new ESEQ(new SEQ(new MOVE(new TEMP(r), new CONST(1)),
                        new SEQ(unCx(t,f),
                        new SEQ(new LABEL(f),
                        new SEQ(new MOVE(new TEMP(r), new CONST(0)),
                                new LABEL(t))))),
                        new TEMP(r));
    }
    
    public Tree.Stm unNx() {
        Label l = new Label();
        return new SEQ(unCx(l,l), new LABEL(l));
    }
}
