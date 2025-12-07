package Tree;

public class MEM extends Exp {
    public Exp exp;
    public int size; // Size in bytes (1, 2, 4, 8)
    
    public MEM(Exp e) {
        this(e, 4); // Default to word size
    }
    
    public MEM(Exp e, int s) {
        exp = e;
        size = s;
    }
    
    public ExpList kids() {
        return new ExpList(exp, null);
    }
    
    public Exp build(ExpList kids) {
        return new MEM(kids.head, size);
    }
}
