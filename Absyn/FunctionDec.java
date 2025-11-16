package Absyn;
import Symbol.Symbol;
public class FunctionDec extends Dec {
  public Symbol name;
  public TypeSpec returnType;
  public ParamList params;
  public Stmt body;
  public boolean leaf = true;  // Added for leaf function detection
  public FunctionDec(int pos, Symbol name, TypeSpec returnType, ParamList params, Stmt body) {
    super(pos);
    this.name = name;
    this.returnType = returnType;
    this.params = params;
    this.body = body;
  }
  public String toString() {
    return "FunctionDec(" + returnType + " " + name + "(" + 
           (params != null ? params : "") + ") " + body + 
           (leaf ? " [leaf]" : "") + ")";
  }
}
