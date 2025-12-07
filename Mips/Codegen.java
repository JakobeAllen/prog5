package Mips;

import Assem.Instr;
import Assem.InstrList;
import Assem.OPER;
import Tree.*;
import Temp.*;
import Frame.Frame;

public class Codegen {
    Frame frame;
    
    public Codegen(Frame f) {
        frame = f;
    }
    
    private InstrList ilist = null, last = null;
    
    private void emit(Instr inst) {
        if (last != null)
            last = last.tail = new InstrList(inst, null);
        else
            last = ilist = new InstrList(inst, null);
    }
    
    public InstrList codegen(StmList stms) {
        InstrList l = null;
        for (StmList sl = stms; sl != null; sl = sl.tail)
            munchStm(sl.head);
        return ilist;
    }
    
    private void munchStm(Stm s) {
        if (s instanceof SEQ) throw new Error("SEQ in munchStm");
        else if (s instanceof Tree.MOVE) munchStm((Tree.MOVE)s);
        else if (s instanceof Tree.LABEL) munchStm((Tree.LABEL)s);
        else if (s instanceof JUMP) munchStm((JUMP)s);
        else if (s instanceof CJUMP) munchStm((CJUMP)s);
        else if (s instanceof UEXP) munchStm((UEXP)s);
        else throw new Error("unknown Stm in munchStm");
    }
    
    private void munchStm(Tree.MOVE s) {
        Exp dst = s.dst;
        Exp src = s.src;
        
        if (dst instanceof MEM) {
            MEM mem = (MEM)dst;
            if (mem.exp instanceof BINOP) {
                BINOP b = (BINOP)mem.exp;
                if (b.binop == BINOP.PLUS && b.right instanceof CONST) {
                    emit(new OPER("sw `s0, " + ((CONST)b.right).value + "(`s1)\n",
                                 null,
                                 new TempList(munchExp(src),
                                             new TempList(munchExp(b.left), null))));
                    return;
                } else if (b.binop == BINOP.PLUS && b.left instanceof CONST) {
                    emit(new OPER("sw `s0, " + ((CONST)b.left).value + "(`s1)\n",
                                 null,
                                 new TempList(munchExp(src),
                                             new TempList(munchExp(b.right), null))));
                    return;
                }
            }
            emit(new OPER("sw `s0, 0(`s1)\n",
                         null,
                         new TempList(munchExp(src),
                                     new TempList(munchExp(mem.exp), null))));
            return;
        } else if (dst instanceof TEMP) {
            emit(new OPER("move `d0, `s0\n",
                         new TempList(((TEMP)dst).temp, null),
                         new TempList(munchExp(src), null)));
            return;
        }
        throw new Error("unknown MOVE destination");
    }
    
    private void munchStm(Tree.LABEL s) {
        emit(new Assem.LABEL(s.label.toString() + ":\n", s.label));
    }
    
    private void munchStm(JUMP s) {
        if (s.exp instanceof NAME) {
            emit(new OPER("j `j0\n", null, null, s.targets));
        } else {
            emit(new OPER("jr `s0\n", null,
                         new TempList(munchExp(s.exp), null),
                         s.targets));
        }
    }
    
    private void munchStm(CJUMP s) {
        Temp left = munchExp(s.left);
        Temp right = munchExp(s.right);
        
        switch (s.relop) {
            case CJUMP.EQ:
                emit(new OPER("beq `s0, `s1, `j0\n", null,
                             new TempList(left, new TempList(right, null)),
                             new LabelList(s.iftrue, null)));
                break;
            case CJUMP.NE:
                emit(new OPER("bne `s0, `s1, `j0\n", null,
                             new TempList(left, new TempList(right, null)),
                             new LabelList(s.iftrue, null)));
                break;
            case CJUMP.LT:
                emit(new OPER("blt `s0, `s1, `j0\n", null,
                             new TempList(left, new TempList(right, null)),
                             new LabelList(s.iftrue, null)));
                break;
            case CJUMP.GT:
                emit(new OPER("bgt `s0, `s1, `j0\n", null,
                             new TempList(left, new TempList(right, null)),
                             new LabelList(s.iftrue, null)));
                break;
            case CJUMP.LE:
                emit(new OPER("ble `s0, `s1, `j0\n", null,
                             new TempList(left, new TempList(right, null)),
                             new LabelList(s.iftrue, null)));
                break;
            case CJUMP.GE:
                emit(new OPER("bge `s0, `s1, `j0\n", null,
                             new TempList(left, new TempList(right, null)),
                             new LabelList(s.iftrue, null)));
                break;
        }
        emit(new OPER("j `j0\n", null, null,
                     new LabelList(s.iffalse, null)));
    }
    
    private void munchStm(UEXP s) {
        munchExp(s.exp);
    }
    
    private Temp munchExp(Exp e) {
        if (e instanceof MEM) return munchExp((MEM)e);
        else if (e instanceof BINOP) return munchExp((BINOP)e);
        else if (e instanceof CONST) return munchExp((CONST)e);
        else if (e instanceof TEMP) return munchExp((TEMP)e);
        else if (e instanceof NAME) return munchExp((NAME)e);
        else if (e instanceof CALL) return munchExp((CALL)e);
        else throw new Error("unknown Exp in munchExp");
    }
    
    private Temp munchExp(MEM e) {
        Temp r = new Temp();
        if (e.exp instanceof BINOP) {
            BINOP b = (BINOP)e.exp;
            if (b.binop == BINOP.PLUS && b.right instanceof CONST) {
                emit(new OPER("lw `d0, " + ((CONST)b.right).value + "(`s0)\n",
                             new TempList(r, null),
                             new TempList(munchExp(b.left), null)));
                return r;
            } else if (b.binop == BINOP.PLUS && b.left instanceof CONST) {
                emit(new OPER("lw `d0, " + ((CONST)b.left).value + "(`s0)\n",
                             new TempList(r, null),
                             new TempList(munchExp(b.right), null)));
                return r;
            }
        }
        emit(new OPER("lw `d0, 0(`s0)\n",
                     new TempList(r, null),
                     new TempList(munchExp(e.exp), null)));
        return r;
    }
    
    private Temp munchExp(BINOP e) {
        Temp r = new Temp();
        Temp left = munchExp(e.left);
        Temp right = munchExp(e.right);
        
        switch (e.binop) {
            case BINOP.PLUS:
                if (e.right instanceof CONST) {
                    emit(new OPER("addi `d0, `s0, " + ((CONST)e.right).value + "\n",
                                 new TempList(r, null),
                                 new TempList(left, null)));
                } else {
                    emit(new OPER("add `d0, `s0, `s1\n",
                                 new TempList(r, null),
                                 new TempList(left, new TempList(right, null))));
                }
                break;
            case BINOP.MINUS:
                emit(new OPER("sub `d0, `s0, `s1\n",
                             new TempList(r, null),
                             new TempList(left, new TempList(right, null))));
                break;
            case BINOP.MUL:
                emit(new OPER("mul `d0, `s0, `s1\n",
                             new TempList(r, null),
                             new TempList(left, new TempList(right, null))));
                break;
            case BINOP.DIV:
                emit(new OPER("div `d0, `s0, `s1\n",
                             new TempList(r, null),
                             new TempList(left, new TempList(right, null))));
                break;
            case BINOP.AND:
                emit(new OPER("and `d0, `s0, `s1\n",
                             new TempList(r, null),
                             new TempList(left, new TempList(right, null))));
                break;
            case BINOP.OR:
                emit(new OPER("or `d0, `s0, `s1\n",
                             new TempList(r, null),
                             new TempList(left, new TempList(right, null))));
                break;
        }
        return r;
    }
    
    private Temp munchExp(CONST e) {
        Temp r = new Temp();
        emit(new OPER("li `d0, " + e.value + "\n",
                     new TempList(r, null), null));
        return r;
    }
    
    private Temp munchExp(TEMP e) {
        return e.temp;
    }
    
    private Temp munchExp(NAME e) {
        Temp r = new Temp();
        emit(new OPER("la `d0, " + e.label + "\n",
                     new TempList(r, null), null));
        return r;
    }
    
    private Temp munchExp(CALL e) {
        Temp r = new Temp();
        TempList args = munchArgs(0, e.args);
        
        if (e.func instanceof NAME) {
            emit(new OPER("jal " + ((NAME)e.func).label + "\n",
                         calldefs(), args));
        } else {
            emit(new OPER("jalr `s0\n",
                         calldefs(),
                         new TempList(munchExp(e.func), args)));
        }
        
        emit(new OPER("move `d0, $v0\n",
                     new TempList(r, null), null));
        return r;
    }
    
    private TempList munchArgs(int i, ExpList args) {
        if (args == null)
            return null;
        else {
            Temp src = munchExp(args.head);
            if (i < 4) {
                Temp dst = new Temp();
                emit(new OPER("move $a" + i + ", `s0\n",
                             null, new TempList(src, null)));
            } else {
                emit(new OPER("sw `s0, " + (i * frame.wordSize()) + "($sp)\n",
                             null, new TempList(src, null)));
            }
            return new TempList(src, munchArgs(i + 1, args.tail));
        }
    }
    
    private TempList calldefs() {
        return null;
    }
}
