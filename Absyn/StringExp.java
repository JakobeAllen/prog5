package Absyn;
public class StringExp extends Exp {
  public String value;
  public StringExp(int pos, String value) {
    super(pos);
    this.value = value;
  }
  public String toString() {
    return "StringExp(\"" + value + "\")";
  }
}
