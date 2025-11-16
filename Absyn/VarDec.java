package Absyn;
import Symbol.Symbol;
public class VarDec extends Dec {
  public Symbol name;
  public TypeSpec type;
  public Exp init;
  public boolean escape = false;  // Added for escape analysis
  public VarDec(int pos, Symbol name, TypeSpec type, Exp init) {
    super(pos);
    this.name = name;
    this.type = type;
    this.init = init;
  }
  public String toString() {
    return "VarDec(" + type + " " + name + (init != null ? " = " + init : "") + 
           (escape ? " [escape]" : "") + ")";
  }
}
