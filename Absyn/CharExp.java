package Absyn;
public class CharExp extends Exp {
  public String value;
  public CharExp(int pos, String value) {
    super(pos);
    this.value = value;
  }
  public String toString() {
    return "CharExp('" + value + "')";
  }
}
