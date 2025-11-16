package Temp;

public class Temp {
    private static int count = 0;
    private int num;
    
    public Temp() {
        num = count++;
    }
    
    public String toString() {
        return "t" + num;
    }
}
