package Tree;

public class Print {
    private java.io.PrintWriter out;
    
    public Print(java.io.PrintWriter o) {
        out = o;
    }
    
    public void prStm(Stm s) {
        prStm(s, 0);
    }
    
    void prStm(Stm s, int d) {
        if (s instanceof SEQ) prStm((SEQ)s, d);
        else if (s instanceof LABEL) prStm((LABEL)s, d);
        else if (s instanceof JUMP) prStm((JUMP)s, d);
        else if (s instanceof CJUMP) prStm((CJUMP)s, d);
        else if (s instanceof MOVE) prStm((MOVE)s, d);
        else if (s instanceof UEXP) prStm((UEXP)s, d);
        else throw new Error("Print.prStm");
    }
    
    void prStm(SEQ s, int d) {
        indent(d);
        say("SEQ(");
        out.println();
        prStm(s.left, d+1);
        say(",");
        out.println();
        prStm(s.right, d+1);
        say(")");
    }
    
    void prStm(LABEL s, int d) {
        indent(d);
        say("LABEL ");
        say(s.label.toString());
    }
    
    void prStm(JUMP s, int d) {
        indent(d);
        say("JUMP(");
        prExp(s.exp);
        say(")");
    }
    
    void prStm(CJUMP s, int d) {
        indent(d);
        say("CJUMP(");
        say(relString(s.relop));
        say(",");
        out.println();
        prExp(s.left, d+1);
        say(",");
        out.println();
        prExp(s.right, d+1);
        say(",");
        out.println();
        indent(d+1);
        say(s.iftrue.toString());
        say(",");
        say(s.iffalse.toString());
        say(")");
    }
    
    void prStm(MOVE s, int d) {
        indent(d);
        say("MOVE(");
        out.println();
        prExp(s.dst, d+1);
        say(",");
        out.println();
        prExp(s.src, d+1);
        say(")");
    }
    
    void prStm(UEXP s, int d) {
        indent(d);
        say("UEXP(");
        out.println();
        prExp(s.exp, d+1);
        say(")");
    }
    
    void prExp(Exp e) {
        prExp(e, 0);
    }
    
    void prExp(Exp e, int d) {
        if (e instanceof BINOP) prExp((BINOP)e, d);
        else if (e instanceof MEM) prExp((MEM)e, d);
        else if (e instanceof TEMP) prExp((TEMP)e, d);
        else if (e instanceof ESEQ) prExp((ESEQ)e, d);
        else if (e instanceof NAME) prExp((NAME)e, d);
        else if (e instanceof CONST) prExp((CONST)e, d);
        else if (e instanceof CALL) prExp((CALL)e, d);
        else throw new Error("Print.prExp");
    }
    
    void prExp(BINOP e, int d) {
        indent(d);
        say("BINOP(");
        say(binopString(e.binop));
        say(",");
        out.println();
        prExp(e.left, d+1);
        say(",");
        out.println();
        prExp(e.right, d+1);
        say(")");
    }
    
    void prExp(MEM e, int d) {
        indent(d);
        say("MEM(");
        out.println();
        prExp(e.exp, d+1);
        say(")");
    }
    
    void prExp(TEMP e, int d) {
        indent(d);
        say("TEMP ");
        say(e.temp.toString());
    }
    
    void prExp(ESEQ e, int d) {
        indent(d);
        say("ESEQ(");
        out.println();
        prStm(e.stm, d+1);
        say(",");
        out.println();
        prExp(e.exp, d+1);
        say(")");
    }
    
    void prExp(NAME e, int d) {
        indent(d);
        say("NAME ");
        say(e.label.toString());
    }
    
    void prExp(CONST e, int d) {
        indent(d);
        say("CONST ");
        say(String.valueOf(e.value));
    }
    
    void prExp(CALL e, int d) {
        indent(d);
        say("CALL(");
        out.println();
        prExp(e.func, d+1);
        for (ExpList a = e.args; a != null; a = a.tail) {
            say(",");
            out.println();
            prExp(a.head, d+1);
        }
        say(")");
    }
    
    private void indent(int d) {
        for (int i = 0; i < d; i++)
            say(" ");
    }
    
    private void say(String s) {
        out.print(s);
    }
    
    private String binopString(int binop) {
        switch (binop) {
            case BINOP.PLUS: return "PLUS";
            case BINOP.MINUS: return "MINUS";
            case BINOP.MUL: return "MUL";
            case BINOP.DIV: return "DIV";
            case BINOP.AND: return "AND";
            case BINOP.OR: return "OR";
            case BINOP.LSHIFT: return "LSHIFT";
            case BINOP.RSHIFT: return "RSHIFT";
            case BINOP.ARSHIFT: return "ARSHIFT";
            case BINOP.XOR: return "XOR";
            default: return "???";
        }
    }
    
    private String relString(int relop) {
        switch (relop) {
            case CJUMP.EQ: return "EQ";
            case CJUMP.NE: return "NE";
            case CJUMP.LT: return "LT";
            case CJUMP.GT: return "GT";
            case CJUMP.LE: return "LE";
            case CJUMP.GE: return "GE";
            case CJUMP.ULT: return "ULT";
            case CJUMP.ULE: return "ULE";
            case CJUMP.UGT: return "UGT";
            case CJUMP.UGE: return "UGE";
            default: return "???";
        }
    }
}
