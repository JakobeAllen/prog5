package Types;

public class CHAR extends Type {
    private static CHAR instance = null;
    
    private CHAR() {}
    
    public static CHAR getInstance() {
        if (instance == null) {
            instance = new CHAR();
        }
        return instance;
    }
    
    public boolean coerceTo(Type t) {
        // Rule 1: INT and CHAR are different types - no coercion
        return t instanceof CHAR;
    }
    
    public String toString() {
        return "char";
    }
}
