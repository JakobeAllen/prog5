package Translate;

import Tree.*;
import Temp.*;

public abstract class Exp {
    public abstract Tree.Exp unEx();
    public abstract Tree.Stm unNx();
    public abstract Tree.Stm unCx(Label t, Label f);
}
