package Translate;

import Tree.*;
import Temp.*;
import Frame.*;

public class Translate {
    private Frame frame;
    
    public Translate() {
    }
    
    public void setFrame(Frame f) {
        frame = f;
    }
    
    private Tree.Exp framePtr() {
        if (frame != null)
            return new TEMP(frame.FP());
        return new TEMP(new Temp());
    }
    
    public Exp simpleVar(Access acc) {
        return new ExpEx(acc.exp(framePtr()));
    }
    
    public Exp intLit(int value) {
        return new ExpEx(new CONST(value));
    }
    
    public Exp binop(int op, Exp left, Exp right) {
        int treeOp;
        switch (op) {
            case 0: treeOp = BINOP.PLUS; break;
            case 1: treeOp = BINOP.MINUS; break;
            case 2: treeOp = BINOP.MUL; break;
            case 3: treeOp = BINOP.DIV; break;
            default: treeOp = BINOP.PLUS;
        }
        return new ExpEx(new BINOP(treeOp, left.unEx(), right.unEx()));
    }
    
    public Exp relop(int op, Exp left, Exp right) {
        int treeOp;
        switch (op) {
            case 5: treeOp = CJUMP.EQ; break;
            case 6: treeOp = CJUMP.NE; break;
            case 7: treeOp = CJUMP.LT; break;
            case 8: treeOp = CJUMP.LE; break;
            case 9: treeOp = CJUMP.GT; break;
            case 10: treeOp = CJUMP.GE; break;
            default: treeOp = CJUMP.EQ;
        }
        return new RelCx(treeOp, left.unEx(), right.unEx());
    }
    
    public Exp assign(Exp var, Exp val) {
        return new ExpNx(new MOVE(var.unEx(), val.unEx()));
    }
    
    public Exp ifThenElse(Exp test, Exp then, Exp elsee) {
        return new IfThenElseExp(test, then, elsee);
    }
    
    public Exp whileLoop(Exp test, Exp body, Label done) {
        Label testLabel = new Label();
        Label bodyLabel = new Label();
        return new ExpNx(new SEQ(new LABEL(testLabel),
                         new SEQ(test.unCx(bodyLabel, done),
                         new SEQ(new LABEL(bodyLabel),
                         new SEQ(body.unNx(),
                         new SEQ(new JUMP(testLabel),
                                 new LABEL(done)))))));
    }
    
    public Exp NO_RESULT() {
        return new ExpNx(new UEXP(new CONST(0)));
    }
    
    public Tree.Stm procEntryExit(Frame myframe, Exp body) {
        return body.unNx();
    }
}
