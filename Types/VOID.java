package Types;

public class VOID extends Type {
    private static VOID instance = null;
    
    private VOID() {}
    
    public static VOID getInstance() {
        if (instance == null) {
            instance = new VOID();
        }
        return instance;
    }
    
    public boolean coerceTo(Type t) {
        return t instanceof VOID;
    }
    
    public String toString() {
        return "void";
    }
}
