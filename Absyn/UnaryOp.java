package Absyn;
public class UnaryOp extends Exp {
  public int oper;
  public Exp exp;
  public final static int NOT=0, NEG=1, BITNOT=2;
  public UnaryOp(int pos, int oper, Exp exp) {
    super(pos);
    this.oper = oper;
    this.exp = exp;
  }
  public String toString() {
    String[] ops = {"!", "-", "~"};
    return "UnaryOp(" + ops[oper] + exp + ")";
  }
}
