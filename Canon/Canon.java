package Canon;

import Tree.*;
import Temp.Label;

class StmExpList {
    Stm stm;
    ExpList exps;
    StmExpList(Stm s, ExpList e) {stm=s; exps=e;}
}

public class Canon {
    private static boolean isNop(Stm s) {
        return s instanceof UEXP && ((UEXP)s).exp instanceof CONST;
    }
    
    private static boolean commute(Stm s, Exp e) {
        return isNop(s) 
            || e instanceof NAME 
            || e instanceof CONST;
    }
    
    static Stm seq(Stm x, Stm y) {
        if (isNop(x)) return y;
        else if (isNop(y)) return x;
        else return new SEQ(x,y);
    }
    
    static ESEQ eseq(Stm x, Exp y) {
        if (isNop(x)) return new ESEQ(new UEXP(new CONST(0)),y);
        else return new ESEQ(x,y);
    }
    
    private static Stm do_stm(Stm s) {
        if (s instanceof SEQ) return do_stm((SEQ)s);
        else if (s instanceof MOVE) return do_stm((MOVE)s);
        else if (s instanceof UEXP) return do_stm((UEXP)s);
        else return reorder_stm(s, s.kids());
    }
    
    private static Stm do_stm(SEQ s) {
        return seq(do_stm(s.left), do_stm(s.right));
    }
    
    private static Stm do_stm(MOVE s) {
        if (s.dst instanceof TEMP && s.src instanceof CALL)
            return reorder_stm(s, new ExpList(s.src, null));
        else if (s.dst instanceof ESEQ) {
            Stm ss = ((ESEQ)s.dst).stm;
            return do_stm(new SEQ(ss, new MOVE(((ESEQ)s.dst).exp, s.src)));
        } else 
            return reorder_stm(s, s.kids());
    }
    
    private static Stm do_stm(UEXP s) {
        if (s.exp instanceof CALL)
            return reorder_stm(s, new ExpList(s.exp, null));
        else
            return reorder_stm(s, s.kids());
    }
    
    private static Stm reorder_stm(Stm s, ExpList e) {
        StmExpList x = reorder(e);
        return seq(x.stm, s.build(x.exps));
    }
    
    private static ESEQ do_exp(ESEQ e) {
        Stm ss = do_stm(e.stm);
        ESEQ ee = do_exp(e.exp);
        return eseq(seq(ss, ee.stm), ee.exp);
    }
    
    private static ESEQ do_exp(Exp e) {
        if (e instanceof ESEQ) return do_exp((ESEQ)e);
        else return reorder_exp(e, e.kids());
    }
    
    private static ESEQ reorder_exp(Exp e, ExpList el) {
        StmExpList x = reorder(el);
        return eseq(x.stm, e.build(x.exps));
    }
    
    private static StmExpList nopNull = new StmExpList(new UEXP(new CONST(0)), null);
    
    private static StmExpList reorder(ExpList el) {
        if (el == null) return nopNull;
        else {
            Exp head = el.head;
            if (head instanceof CALL) {
                Temp.Temp t = new Temp.Temp();
                Exp ee = new ESEQ(new MOVE(new TEMP(t), head), new TEMP(t));
                return reorder(new ExpList(ee, el.tail));
            } else {
                ESEQ ee = do_exp(head);
                StmExpList hh = reorder(el.tail);
                if (commute(hh.stm, ee.exp)) {
                    return new StmExpList(seq(ee.stm, hh.stm),
                                         new ExpList(ee.exp, hh.exps));
                } else {
                    Temp.Temp t = new Temp.Temp();
                    return new StmExpList(seq(ee.stm,
                                             seq(new MOVE(new TEMP(t), ee.exp),
                                                 hh.stm)),
                                         new ExpList(new TEMP(t), hh.exps));
                }
            }
        }
    }
    
    private static StmList linear(SEQ s, StmList l) {
        return linear(s.left, linear(s.right, l));
    }
    
    private static StmList linear(Stm s, StmList l) {
        if (s instanceof SEQ) return linear((SEQ)s, l);
        else return new StmList(s, l);
    }
    
    public static StmList linearize(Stm s) {
        return linear(do_stm(s), null);
    }
}
