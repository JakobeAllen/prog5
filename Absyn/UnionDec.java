package Absyn;

public class UnionDec extends Dec {
    public Symbol.Symbol name;
    public DecList members;
    
    public UnionDec(int p, Symbol.Symbol n, DecList m) {
        super(p);
        name = n;
        members = m;
    }
    
    public String toString() {
        return "union " + name + " { ... }";
    }
}
