package Temp;

import Symbol.Symbol;

public class Label {
    private String name;
    private static int count = 0;
    
    public Label() {
        this("L" + count++);
    }
    
    public Label(String n) {
        name = n;
    }
    
    public Label(Symbol s) {
        this(s.toString());
    }
    
    public String toString() {
        return name;
    }
}
