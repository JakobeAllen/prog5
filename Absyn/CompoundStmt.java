package Absyn;
public class CompoundStmt extends Stmt {
  public DecList decs;
  public StmtList stmts;
  public CompoundStmt(int pos, DecList decs, StmtList stmts) {
    super(pos);
    this.decs = decs;
    this.stmts = stmts;
  }
  public String toString() {
    String result = "CompoundStmt(";
    if (decs != null) result += "decs=" + decs;
    if (stmts != null) result += (decs != null ? ", " : "") + "stmts=" + stmts;
    return result + ")";
  }
}
