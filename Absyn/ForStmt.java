package Absyn;
public class ForStmt extends Stmt {
  public Exp init, test, increment;
  public Stmt body;
  public ForStmt(int pos, Exp init, Exp test, Exp increment, Stmt body) {
    super(pos);
    this.init = init;
    this.test = test;
    this.increment = increment;
    this.body = body;
  }
  public String toString() {
    return "ForStmt(" + init + "; " + test + "; " + increment + ", " + body + ")";
  }
}
