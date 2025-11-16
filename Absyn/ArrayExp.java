package Absyn;
public class ArrayExp extends Exp {
  public Exp array, index;
  public ArrayExp(int pos, Exp array, Exp index) {
    super(pos);
    this.array = array;
    this.index = index;
  }
  public String toString() {
    return "ArrayExp(" + array + "[" + index + "])";
  }
}
