fun int main() {
    var int[10] arr;
    var int i;
    var int j;
    var int temp;
    var int n;
    
    n = 10;
    arr[0] = 5;
    arr[1] = 2;
    arr[2] = 9;
    arr[3] = 1;
    arr[4] = 7;
    arr[5] = 6;
    arr[6] = 3;
    arr[7] = 8;
    arr[8] = 4;
    arr[9] = 0;
    
    i = 0;
    while (i < n - 1) {
        j = 0;
        while (j < n - i - 1) {
            if (arr[j] > arr[j + 1]) {
                temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
            j = j + 1;
        }
        i = i + 1;
    }
    
    return 0;
}
