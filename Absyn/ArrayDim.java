package Absyn;
public class ArrayDim extends Absyn {
  public Exp size;
  public ArrayDim(int pos, Exp size) {
    this.pos = pos;
    this.size = size;
  }
  public String toString() {
    return "[" + (size != null ? size : "") + "]";
  }
}
