package Absyn;
public class ArrayDimList {
  public ArrayDim head;
  public ArrayDimList tail;
  public ArrayDimList(ArrayDim head, ArrayDimList tail) {
    this.head = head;
    this.tail = tail;
  }
  public String toString() {
    if (tail == null) return head.toString();
    return head.toString() + tail;
  }
}
