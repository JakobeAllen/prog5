package Absyn;
public class IfStmt extends Stmt {
  public Exp test;
  public Stmt thenclause, elseclause;
  public IfStmt(int pos, Exp test, Stmt thenclause, Stmt elseclause) {
    super(pos);
    this.test = test;
    this.thenclause = thenclause;
    this.elseclause = elseclause;
  }
  public String toString() {
    return "IfStmt(" + test + ", " + thenclause + 
           (elseclause != null ? ", " + elseclause : "") + ")";
  }
}
