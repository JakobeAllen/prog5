package Absyn;
public class AssignExp extends Exp {
  public Exp var, exp;
  public AssignExp(int pos, Exp var, Exp exp) {
    super(pos);
    this.var = var;
    this.exp = exp;
  }
  public String toString() {
    return "AssignExp(" + var + " = " + exp + ")";
  }
}
