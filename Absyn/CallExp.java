package Absyn;
import Symbol.Symbol;
public class CallExp extends Exp {
  public Symbol func;
  public ExpList args;
  public CallExp(int pos, Symbol func, ExpList args) {
    super(pos);
    this.func = func;
    this.args = args;
  }
  public String toString() {
    return "CallExp(" + func + "(" + (args != null ? args : "") + "))";
  }
}
