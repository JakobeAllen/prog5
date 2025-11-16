package Absyn;
public class ExpList {
  public Exp head;
  public ExpList tail;
  public ExpList(Exp head, ExpList tail) {
    this.head = head;
    this.tail = tail;
  }
  public String toString() {
    if (tail == null) return head.toString();
    return head + ", " + tail;
  }
}
