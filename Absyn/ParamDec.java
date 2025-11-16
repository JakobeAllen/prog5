package Absyn;
import Symbol.Symbol;
public class ParamDec extends Absyn {
  public Symbol name;
  public TypeSpec type;
  public boolean escape = false;  // Added for escape analysis
  public ParamDec(int pos, Symbol name, TypeSpec type) {
    this.pos = pos;
    this.name = name;
    this.type = type;
  }
  public String toString() {
    return type + " " + name + (escape ? " [escape]" : "");
  }
}
