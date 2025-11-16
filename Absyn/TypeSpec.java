package Absyn;
public class TypeSpec extends Absyn {
  public int type;
  public int pointers;
  public ArrayDimList arrays;
  public Symbol.Symbol name;  // For struct/union types
  public final static int INT=0, CHAR=1, VOID=2, STRUCT=3, UNION=4;
  
  public TypeSpec(int pos, int type, int pointers, ArrayDimList arrays) {
    this.pos = pos;
    this.type = type;
    this.pointers = pointers;
    this.arrays = arrays;
    this.name = null;
  }
  
  public TypeSpec(int pos, int type, Symbol.Symbol name, int pointers, ArrayDimList arrays) {
    this.pos = pos;
    this.type = type;
    this.pointers = pointers;
    this.arrays = arrays;
    this.name = name;
  }
  
  public String toString() {
    String result;
    if (type == STRUCT || type == UNION) {
      result = (type == STRUCT ? "struct " : "union ") + (name != null ? name.toString() : "");
    } else {
      String[] types = {"int", "char", "void"};
      result = types[type];
    }
    for (int i = 0; i < pointers; i++) result += "*";
    if (arrays != null) result += arrays;
    return result;
  }
}
