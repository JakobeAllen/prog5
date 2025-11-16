package Types;

public class POINTER extends Type {
    public Type target;
    
    public POINTER(Type t) {
        target = t;
    }
    
    public boolean coerceTo(Type t) {
        if (t instanceof POINTER) {
            POINTER pt = (POINTER)t;
            // Void pointer is compatible with all pointers
            if (target instanceof VOID || pt.target instanceof VOID) {
                return true;
            }
            return target.coerceTo(pt.target);
        }
        return false;
    }
    
    public boolean match(Type t) {
        if (t instanceof POINTER) {
            POINTER pt = (POINTER)t;
            return target.match(pt.target);
        }
        return false;
    }
    
    public String toString() {
        return target.toString() + "*";
    }
}
