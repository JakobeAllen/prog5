package Absyn;
public class ExpStmt extends Stmt {
  public Exp exp;
  public ExpStmt(int pos, Exp exp) {
    super(pos);
    this.exp = exp;
  }
  public String toString() {
    return "ExpStmt(" + (exp != null ? exp : "empty") + ")";
  }
}
