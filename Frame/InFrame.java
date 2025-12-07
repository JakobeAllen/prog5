package Frame;

import Tree.*;

public class InFrame extends Access {
    public int offset;
    
    public InFrame(int off) {
        offset = off;
    }
    
    public Tree.Exp exp(Tree.Exp fp) {
        return new MEM(new BINOP(BINOP.PLUS, fp, new CONST(offset)));
    }
    
    public String toString() {
        return "[fp" + (offset >= 0 ? "+" : "") + offset + "]";
    }
}
