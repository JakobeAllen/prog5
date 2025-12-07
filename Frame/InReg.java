package Frame;

import Temp.Temp;
import Tree.*;

public class InReg extends Access {
    public Temp temp;
    
    public InReg(Temp t) {
        temp = t;
    }
    
    public Tree.Exp exp(Tree.Exp fp) {
        return new TEMP(temp);
    }
    
    public String toString() {
        return temp.toString();
    }
}
