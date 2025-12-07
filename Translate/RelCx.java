package Translate;

import Tree.*;
import Temp.*;

public class RelCx extends ExpCx {
    int op;
    Tree.Exp left, right;
    
    public RelCx(int o, Tree.Exp l, Tree.Exp r) {
        op = o;
        left = l;
        right = r;
    }
    
    public Tree.Stm unCx(Label t, Label f) {
        return new CJUMP(op, left, right, t, f);
    }
}
