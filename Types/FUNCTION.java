package Types;

public class FUNCTION extends Type {
    public Type returnType;
    public Type[] paramTypes;
    
    public FUNCTION(Type ret, Type[] params) {
        returnType = ret;
        paramTypes = params;
    }
    
    public boolean coerceTo(Type t) {
        return false; // Functions don't coerce
    }
    
    public boolean match(Type t) {
        if (!(t instanceof FUNCTION)) {
            return false;
        }
        FUNCTION ft = (FUNCTION)t;
        if (!returnType.match(ft.returnType)) {
            return false;
        }
        if (paramTypes == null && ft.paramTypes == null) {
            return true;
        }
        if (paramTypes == null || ft.paramTypes == null) {
            return false;
        }
        if (paramTypes.length != ft.paramTypes.length) {
            return false;
        }
        for (int i = 0; i < paramTypes.length; i++) {
            if (!paramTypes[i].match(ft.paramTypes[i])) {
                return false;
            }
        }
        return true;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(returnType.toString());
        sb.append(" (");
        if (paramTypes != null) {
            for (int i = 0; i < paramTypes.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(paramTypes[i].toString());
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
