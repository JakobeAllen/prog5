package Frame;

import Temp.Temp;

public class InReg extends Access {
    public Temp temp;
    
    public InReg(Temp t) {
        temp = t;
    }
    
    public String toString() {
        return temp.toString();
    }
}
