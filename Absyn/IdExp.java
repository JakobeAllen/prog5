package Absyn;
import Symbol.Symbol;
public class IdExp extends Exp {
  public Symbol name;
  public IdExp(int pos, Symbol name) {
    super(pos);
    this.name = name;
  }
  public String toString() {
    return "IdExp(" + name + ")";
  }
}
