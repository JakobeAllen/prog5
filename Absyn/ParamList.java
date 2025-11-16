package Absyn;
public class ParamList {
  public ParamDec head;
  public ParamList tail;
  public ParamList(ParamDec head, ParamList tail) {
    this.head = head;
    this.tail = tail;
  }
  public String toString() {
    if (tail == null) return head.toString();
    return head + ", " + tail;
  }
}
