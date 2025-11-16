package Frame;

public class InFrame extends Access {
    public int offset;
    
    public InFrame(int off) {
        offset = off;
    }
    
    public String toString() {
        return "[fp" + (offset >= 0 ? "+" : "") + offset + "]";
    }
}
