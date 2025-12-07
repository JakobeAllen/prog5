package Canon;

import Tree.*;
import Temp.Label;

public class TraceSchedule {
    public StmList stms;
    
    private StmListList theBlocks;
    private java.util.Hashtable<Label, StmList> table = new java.util.Hashtable<Label, StmList>();
    
    private void enter(Label l, StmList b) {
        table.put(l, b);
    }
    
    private StmList getLast(StmList block) {
        StmList l = block;
        while (l.tail.tail != null) l = l.tail;
        return l;
    }
    
    private void trace(StmList l) {
        while (true) {
            Label lab = ((LABEL)l.head).label;
            table.remove(lab);
            
            StmList last = getLast(l);
            Stm s = last.tail.head;
            if (s instanceof JUMP) {
                JUMP j = (JUMP)s;
                StmList target = table.get(j.targets.head);
                if (j.targets.tail == null && target != null) {
                    last.tail = target;
                    l = target;
                } else {
                    last.tail.tail = getNext();
                    return;
                }
            } else if (s instanceof CJUMP) {
                CJUMP j = (CJUMP)s;
                StmList t = table.get(j.iftrue);
                StmList f = table.get(j.iffalse);
                if (f != null) {
                    last.tail.tail = f;
                    l = f;
                } else if (t != null) {
                    last.tail.head = new CJUMP(CJUMP.notRel(j.relop),
                                               j.left, j.right, j.iffalse, j.iftrue);
                    last.tail.tail = t;
                    l = t;
                } else {
                    Label ff = new Label();
                    last.tail.head = new CJUMP(j.relop, j.left, j.right,
                                              j.iftrue, ff);
                    last.tail.tail = new StmList(new LABEL(ff),
                                                 new StmList(new JUMP(j.iffalse),
                                                            getNext()));
                    return;
                }
            } else {
                throw new Error("Bad basic block in TraceSchedule");
            }
        }
    }
    
    private StmList getNext() {
        if (theBlocks == null)
            return new StmList(new LABEL(new Label()), null);
        else {
            StmList s = theBlocks.head;
            Label lab = ((LABEL)s.head).label;
            if (table.get(lab) != null) {
                trace(s);
                return s;
            } else {
                theBlocks = theBlocks.tail;
                return getNext();
            }
        }
    }
    
    public TraceSchedule(BasicBlocks b) {
        StmListList blocks;
        
        for (blocks = b.blocks; blocks != null; blocks = blocks.tail) {
            enter(((LABEL)blocks.head.head).label, blocks.head);
        }
        
        theBlocks = b.blocks;
        stms = getNext();
        
        table = null;
    }
}
