package Frame;

import Temp.Temp;
import Temp.Label;

public abstract class Frame {
    public Label name;
    public abstract Frame newFrame(Label name, Util.BoolList formals);
    public abstract Access allocLocal(boolean escape);
    public abstract Access[] formals();
    public abstract Temp FP();
    public abstract Temp RV();
    public abstract int wordSize();
}
