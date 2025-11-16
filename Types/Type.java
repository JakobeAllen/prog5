package Types;

public abstract class Type {
    public abstract boolean coerceTo(Type t);
    public boolean match(Type t) {
        return this == t;
    }
}
