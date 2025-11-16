package Absyn;
public class BinOp extends Exp {
  public Exp left, right;
  public int oper;
  public final static int PLUS=0, MINUS=1, MUL=2, DIV=3, MOD=4;
  public final static int EQ=5, NE=6, LT=7, LE=8, GT=9, GE=10;
  public final static int AND=11, OR=12;
  public final static int BITAND=13, BITOR=14, BITXOR=15;
  public final static int LSHIFT=16, RSHIFT=17;
  public BinOp(int pos, Exp left, int oper, Exp right) {
    super(pos);
    this.left = left;
    this.oper = oper;
    this.right = right;
  }
  public String toString() {
    String[] ops = {"+", "-", "*", "/", "%", "==", "!=", "<", "<=", ">", ">=", 
                    "&&", "||", "&", "|", "^", "<<", ">>"};
    return "BinOp(" + left + " " + ops[oper] + " " + right + ")";
  }
}
