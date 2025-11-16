package Mips;

import Frame.Frame;
import Frame.Access;
import Frame.InFrame;
import Frame.InReg;
import Temp.Label;
import Temp.Temp;
import Util.BoolList;

public class MipsFrame extends Frame {
    private static final int WORD_SIZE = 4;
    private int frameOffset = 0;
    private int formalCount = 0;
    private java.util.ArrayList<Access> formalList = new java.util.ArrayList<Access>();
    
    public MipsFrame(Label n, BoolList formals) {
        name = n;
        
        // Process formal parameters
        // First 4 parameters go in $a0-$a3, rest on stack
        int regCount = 0;
        for (BoolList fl = formals; fl != null; fl = fl.tail) {
            if (fl.head) {
                // Escapes - must be in frame
                // Even if passed in register, allocate space in outgoing arg area
                formalList.add(new InFrame(formalCount * WORD_SIZE));
            } else {
                // Doesn't escape - can use register or temporary
                if (regCount < 4) {
                    // Use register temporary
                    formalList.add(new InReg(new Temp()));
                } else {
                    // Beyond 4 args, put in frame
                    formalList.add(new InFrame(formalCount * WORD_SIZE));
                }
            }
            formalCount++;
            if (!fl.head) regCount++;
        }
        
        // Initialize frame offset for locals (grows downward from FP)
        frameOffset = 0;
    }
    
    public Frame newFrame(Label n, BoolList formals) {
        return new MipsFrame(n, formals);
    }
    
    public Access allocLocal(boolean escape) {
        if (escape) {
            // Allocate in frame (grows downward)
            frameOffset -= WORD_SIZE;
            return new InFrame(frameOffset);
        } else {
            // Allocate in temporary register
            return new InReg(new Temp());
        }
    }
    
    public Access[] formals() {
        return formalList.toArray(new Access[formalList.size()]);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Frame: " + name + "\n");
        sb.append("Formals:\n");
        for (int i = 0; i < formalList.size(); i++) {
            sb.append("  " + i + ": " + formalList.get(i) + "\n");
        }
        sb.append("Frame offset: " + frameOffset + "\n");
        return sb.toString();
    }
}
