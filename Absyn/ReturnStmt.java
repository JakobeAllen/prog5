package Absyn;
public class ReturnStmt extends Stmt {
  public Exp exp;
  public ReturnStmt(int pos, Exp exp) {
    super(pos);
    this.exp = exp;
  }
  public String toString() {
    return "ReturnStmt(" + (exp != null ? exp.toString() : "void") + ")";
  }
}
