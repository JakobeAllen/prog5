/*Pass*/

fun int main() {
   var int a;
   var int* p;
   var char c;
   
   a = 10;
   c = 'a';
   
   // int to pointer is allowed
   p = 0;
   p = a;
   
   a = a + 5;
   a = a * 2;
   
   return a;
}
