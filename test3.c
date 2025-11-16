/*Pass*/

struct Data {
   int a;
   int b;
}

fun int add(int x, int y, int z) {
   var int r;
   var int t;
   
   r = x + y;
   t = r * z;
   
   return t;
}

fun int main() {
   var int[3] arr = {1, 2, 3};
   var int[2][2] mat = {{1, 2}, {3, 4}};
   var Data d;
   var int r;
   
   r = add(10, 20, 30);
   
   return r;
}
