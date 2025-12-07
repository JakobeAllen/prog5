fun int testArrays() {
    var int[100] bigArray;
    var int x;
    var char[20] charArray;
    
    x = 5;
    bigArray[0] = x;
    charArray[0] = 'A';
    
    return bigArray[0];
}

fun int main() {
    return testArrays();
}
