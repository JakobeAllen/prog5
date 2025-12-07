package Assem;

import Temp.*;

public abstract class Instr {
    public String assem;
    
    public abstract String format(TempMap m);
    
    private String format(String a, TempList l, TempMap m) {
        TempList tl = l;
        String result = "";
        for (int i = 0; i < a.length(); i++) {
            char c = a.charAt(i);
            if (c == '`') {
                i++;
                if (a.charAt(i) == 's') {
                    i++;
                    int n = Character.digit(a.charAt(i), 10);
                    result = result + m.tempMap(tl.head);
                    tl = tl.tail;
                } else if (a.charAt(i) == 'd') {
                    i++;
                    int n = Character.digit(a.charAt(i), 10);
                    result = result + m.tempMap(tl.head);
                    tl = tl.tail;
                } else if (a.charAt(i) == 'j') {
                    i++;
                } else if (a.charAt(i) == '`') {
                    result = result + '`';
                }
            } else {
                result = result + c;
            }
        }
        return result;
    }
    
    protected String format(String a, TempList src, TempList dst, TempMap m) {
        TempList srcList = src;
        TempList dstList = dst;
        String result = "";
        for (int i = 0; i < a.length(); i++) {
            char c = a.charAt(i);
            if (c == '`') {
                i++;
                if (a.charAt(i) == 's') {
                    i++;
                    int n = Character.digit(a.charAt(i), 10);
                    TempList curr = srcList;
                    for (int j = 0; j < n; j++) curr = curr.tail;
                    result = result + m.tempMap(curr.head);
                } else if (a.charAt(i) == 'd') {
                    i++;
                    int n = Character.digit(a.charAt(i), 10);
                    TempList curr = dstList;
                    for (int j = 0; j < n; j++) curr = curr.tail;
                    result = result + m.tempMap(curr.head);
                } else if (a.charAt(i) == 'j') {
                    i++;
                } else if (a.charAt(i) == '`') {
                    result = result + '`';
                }
            } else {
                result = result + c;
            }
        }
        return result;
    }
    
    protected String format(String a, TempList src, TempList dst, LabelList jump, TempMap m) {
        TempList srcList = src;
        TempList dstList = dst;
        LabelList jumpList = jump;
        String result = "";
        for (int i = 0; i < a.length(); i++) {
            char c = a.charAt(i);
            if (c == '`') {
                i++;
                if (a.charAt(i) == 's') {
                    i++;
                    int n = Character.digit(a.charAt(i), 10);
                    TempList curr = srcList;
                    for (int j = 0; j < n; j++) curr = curr.tail;
                    result = result + m.tempMap(curr.head);
                } else if (a.charAt(i) == 'd') {
                    i++;
                    int n = Character.digit(a.charAt(i), 10);
                    TempList curr = dstList;
                    for (int j = 0; j < n; j++) curr = curr.tail;
                    result = result + m.tempMap(curr.head);
                } else if (a.charAt(i) == 'j') {
                    i++;
                    int n = Character.digit(a.charAt(i), 10);
                    LabelList curr = jumpList;
                    for (int j = 0; j < n; j++) curr = curr.tail;
                    result = result + curr.head.toString();
                } else if (a.charAt(i) == '`') {
                    result = result + '`';
                }
            } else {
                result = result + c;
            }
        }
        return result;
    }
}
