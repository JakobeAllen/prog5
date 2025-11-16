package Types;

public class STRUCT extends Type {
    public Symbol.Symbol name;
    public Type[] memberTypes;
    
    public STRUCT(Symbol.Symbol name) {
        this.name = name;
        this.memberTypes = null;
    }
    
    public STRUCT(Symbol.Symbol name, Type[] members) {
        this.name = name;
        this.memberTypes = members;
    }
    
    public boolean coerceTo(Type t) {
        if (!(t instanceof STRUCT)) return false;
        STRUCT st = (STRUCT)t;
        if (name == null || st.name == null) return false;
        return name.toString().equals(st.name.toString());
    }
    
    public String toString() {
        return "struct " + (name != null ? name.toString() : "");
    }
}
