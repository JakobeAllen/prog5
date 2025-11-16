package Absyn;
public class IntExp extends Exp {
  public int value;
  public IntExp(int pos, int value) {
    super(pos);
    this.value = value;
  }
  public String toString() {
    return "IntExp(" + value + ")";
  }
}
