package Absyn;

public class StructDec extends Dec {
    public Symbol.Symbol name;
    public DecList members;
    
    public StructDec(int p, Symbol.Symbol n, DecList m) {
        super(p);
        name = n;
        members = m;
    }
    
    public String toString() {
        return "struct " + name + " { ... }";
    }
}
