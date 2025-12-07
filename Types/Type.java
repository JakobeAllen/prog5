package Types;

public abstract class Type {
    public abstract boolean coerceTo(Type t);
    public boolean match(Type t) {
        return this == t;
    }
    
    public int size() {
        if (this instanceof INT) return 4;
        if (this instanceof CHAR) return 1;
        if (this instanceof POINTER) return 4;
        if (this instanceof ARRAY) {
            ARRAY a = (ARRAY)this;
            return a.size * a.element.size();
        }
        if (this instanceof STRUCT) {
            STRUCT s = (STRUCT)this;
            if (s.memberTypes == null) return 4;
            int total = 0;
            for (Type t : s.memberTypes) {
                total += t.size();
            }
            return total;
        }
        return 4;
    }
}
