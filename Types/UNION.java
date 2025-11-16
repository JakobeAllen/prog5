package Types;

public class UNION extends Type {
    public Symbol.Symbol name;
    public Type[] memberTypes;
    
    public UNION(Symbol.Symbol name) {
        this.name = name;
        this.memberTypes = null;
    }
    
    public UNION(Symbol.Symbol name, Type[] members) {
        this.name = name;
        this.memberTypes = members;
    }
    
    public boolean coerceTo(Type t) {
        if (!(t instanceof UNION)) return false;
        UNION ut = (UNION)t;
        if (name == null || ut.name == null) return false;
        return name.toString().equals(ut.name.toString());
    }
    
    public String toString() {
        return "union " + (name != null ? name.toString() : "");
    }
}
