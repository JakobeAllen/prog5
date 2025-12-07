package Translate;

import Tree.*;
import Temp.*;

public class ExpEx extends Exp {
    Tree.Exp exp;
    
    public ExpEx(Tree.Exp e) {
        exp = e;
    }
    
    public Tree.Exp unEx() {
        return exp;
    }
    
    public Tree.Stm unNx() {
        return new UEXP(exp);
    }
    
    public Tree.Stm unCx(Label t, Label f) {
        Tree.Stm s;
        if (exp instanceof CONST) {
            int val = ((CONST)exp).value;
            if (val == 0)
                s = new JUMP(f);
            else
                s = new JUMP(t);
        } else {
            s = new CJUMP(CJUMP.NE, exp, new CONST(0), t, f);
        }
        return s;
    }
}
