package FindEscape;

import Absyn.*;
import Symbol.Symbol;
import Symbol.Table;

// Simplified escape analysis for C
// In C, a variable escapes if its address is taken (but we don't have & operator in this parser)
// For now, we'll mark variables as not escaping unless they're passed to functions or stored
public class FindEscape {
    private Table escapeEnv = new Table();
    
    static class EscapeEntry {
        public VarDec varDec;
        public ParamDec paramDec;
        
        public EscapeEntry(VarDec v) {
            varDec = v;
        }
        
        public EscapeEntry(ParamDec p) {
            paramDec = p;
        }
        
        public void setEscape() {
            if (varDec != null) varDec.escape = true;
            if (paramDec != null) paramDec.escape = true;
        }
    }
    
    public void findEscape(Program p) {
        if (p instanceof Program) {
            traverseDecList(p.decs);
        }
    }
    
    private void traverseDecList(DecList decs) {
        for (; decs != null; decs = decs.tail) {
            traverseDec(decs.head);
        }
    }
    
    private void traverseDec(Dec dec) {
        if (dec instanceof VarDec) {
            traverseDec((VarDec)dec);
        } else if (dec instanceof FunctionDec) {
            traverseDec((FunctionDec)dec);
        }
    }
    
    private void traverseDec(VarDec dec) {
        escapeEnv.put(dec.name, new EscapeEntry(dec));
        if (dec.init != null) {
            traverseExp(dec.init);
        }
    }
    
    private void traverseDec(FunctionDec dec) {
        escapeEnv.beginScope();
        traverseParamList(dec.params);
        if (dec.body != null) {
            traverseStmt(dec.body);
        }
        escapeEnv.endScope();
    }
    
    private void traverseParamList(ParamList params) {
        for (; params != null; params = params.tail) {
            if (params.head != null) {
                escapeEnv.put(params.head.name, new EscapeEntry(params.head));
            }
        }
    }
    
    private void traverseStmt(Stmt stmt) {
        if (stmt instanceof CompoundStmt) {
            CompoundStmt cs = (CompoundStmt)stmt;
            escapeEnv.beginScope();
            traverseDecList(cs.decs);
            traverseStmtList(cs.stmts);
            escapeEnv.endScope();
        } else if (stmt instanceof ExpStmt) {
            ExpStmt es = (ExpStmt)stmt;
            if (es.exp != null) traverseExp(es.exp);
        } else if (stmt instanceof IfStmt) {
            IfStmt is = (IfStmt)stmt;
            if (is.test != null) traverseExp(is.test);
            if (is.thenclause != null) traverseStmt(is.thenclause);
            if (is.elseclause != null) traverseStmt(is.elseclause);
        } else if (stmt instanceof WhileStmt) {
            WhileStmt ws = (WhileStmt)stmt;
            if (ws.test != null) traverseExp(ws.test);
            if (ws.body != null) traverseStmt(ws.body);
        } else if (stmt instanceof ForStmt) {
            ForStmt fs = (ForStmt)stmt;
            escapeEnv.beginScope();
            if (fs.init != null) traverseExp(fs.init);
            if (fs.test != null) traverseExp(fs.test);
            if (fs.increment != null) traverseExp(fs.increment);
            if (fs.body != null) traverseStmt(fs.body);
            escapeEnv.endScope();
        } else if (stmt instanceof ReturnStmt) {
            ReturnStmt rs = (ReturnStmt)stmt;
            if (rs.exp != null) traverseExp(rs.exp);
        }
    }
    
    private void traverseStmtList(StmtList stmts) {
        for (; stmts != null; stmts = stmts.tail) {
            if (stmts.head != null) traverseStmt(stmts.head);
        }
    }
    
    private void traverseExp(Exp exp) {
        if (exp instanceof BinOp) {
            BinOp bo = (BinOp)exp;
            if (bo.left != null) traverseExp(bo.left);
            if (bo.right != null) traverseExp(bo.right);
        } else if (exp instanceof UnaryOp) {
            UnaryOp uo = (UnaryOp)exp;
            if (uo.exp != null) traverseExp(uo.exp);
        } else if (exp instanceof AssignExp) {
            AssignExp ae = (AssignExp)exp;
            if (ae.var != null) traverseExp(ae.var);
            if (ae.exp != null) traverseExp(ae.exp);
        } else if (exp instanceof CallExp) {
            CallExp ce = (CallExp)exp;
            traverseExpList(ce.args);
        } else if (exp instanceof ArrayExp) {
            ArrayExp ae = (ArrayExp)exp;
            if (ae.array != null) traverseExp(ae.array);
            if (ae.index != null) traverseExp(ae.index);
        }
    }
    
    private void traverseExpList(ExpList exps) {
        for (; exps != null; exps = exps.tail) {
            if (exps.head != null) traverseExp(exps.head);
        }
    }
}
