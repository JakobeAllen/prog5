package Tree;

public class MOVE extends Stm {
    public Exp dst, src;
    public int size; // Size in bytes for C (1=byte, 2=short, 4=int, 8=long long)
    
    public MOVE(Exp d, Exp s) {
        this(d, s, 4); // Default to word size
    }
    
    public MOVE(Exp d, Exp s, int sz) {
        dst = d;
        src = s;
        size = sz;
    }
    
    public ExpList kids() {
        if (dst instanceof MEM)
            return new ExpList(((MEM)dst).exp, new ExpList(src, null));
        else
            return new ExpList(src, null);
    }
    
    public Stm build(ExpList kids) {
        if (dst instanceof MEM)
            return new MOVE(new MEM(kids.head, ((MEM)dst).size), kids.tail.head, size);
        else
            return new MOVE(dst, kids.head, size);
    }
}
