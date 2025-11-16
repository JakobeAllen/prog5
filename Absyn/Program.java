package Absyn;
public class Program extends Absyn {
  public DecList decs;
  public Program(int pos, DecList decs) {
    this.pos = pos;
    this.decs = decs;
  }
  public String toString() {
    return "Program(\n  " + decs + "\n)";
  }
}
