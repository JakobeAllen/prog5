package Absyn;
public class DecList {
  public Dec head;
  public DecList tail;
  public DecList(Dec head, DecList tail) {
    this.head = head;
    this.tail = tail;
  }
  public String toString() {
    if (tail == null) return head.toString();
    return head + "; " + tail;
  }
}
