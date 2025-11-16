package Absyn;

public class InitList extends Exp {
    public ExpList exps;
    
    public InitList(int p, ExpList e) {
        super(p);
        exps = e;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        ExpList el = exps;
        while (el != null) {
            if (el.head != null) {
                sb.append(el.head.toString());
            }
            if (el.tail != null) {
                sb.append(", ");
            }
            el = el.tail;
        }
        sb.append("}");
        return sb.toString();
    }
}
