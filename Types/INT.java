package Types;

public class INT extends Type {
    private static INT instance = null;
    
    private INT() {}
    
    public static INT getInstance() {
        if (instance == null) {
            instance = new INT();
        }
        return instance;
    }
    
    public boolean coerceTo(Type t) {
        // Rule 1: INT and CHAR are different types - no coercion
        // Rule 2: Any number can be assigned to any pointer
        return t instanceof INT || t instanceof POINTER;
    }
    
    public String toString() {
        return "int";
    }
}
