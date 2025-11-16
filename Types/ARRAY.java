package Types;

public class ARRAY extends Type {
    public Type element;
    public int size;
    
    public ARRAY(Type e, int s) {
        element = e;
        size = s;
    }
    
    public boolean coerceTo(Type t) {
        if (t instanceof ARRAY) {
            ARRAY at = (ARRAY)t;
            return element.coerceTo(at.element);
        }
        // Array can decay to pointer
        if (t instanceof POINTER) {
            POINTER pt = (POINTER)t;
            return element.coerceTo(pt.target);
        }
        return false;
    }
    
    public boolean match(Type t) {
        if (t instanceof ARRAY) {
            ARRAY at = (ARRAY)t;
            return element.match(at.element);
        }
        return false;
    }
    
    public String toString() {
        return element.toString() + "[" + size + "]";
    }
}
