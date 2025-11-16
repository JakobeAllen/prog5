package Absyn;
public class StmtList {
  public Stmt head;
  public StmtList tail;
  public StmtList(Stmt head, StmtList tail) {
    this.head = head;
    this.tail = tail;
  }
  public String toString() {
    if (tail == null) return head.toString();
    return head + "; " + tail;
  }
}
