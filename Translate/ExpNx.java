package Translate;

import Tree.*;
import Temp.*;

public class ExpNx extends Exp {
    Tree.Stm stm;
    
    public ExpNx(Tree.Stm s) {
        stm = s;
    }
    
    public Tree.Exp unEx() {
        return new ESEQ(stm, new CONST(0));
    }
    
    public Tree.Stm unNx() {
        return stm;
    }
    
    public Tree.Stm unCx(Label t, Label f) {
        return new SEQ(stm, new JUMP(t));
    }
}
