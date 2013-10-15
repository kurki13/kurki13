package debug.model;

public class OsasuoritusRaja {

    int max;
    int min;

    public OsasuoritusRaja(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "OsasuoritusRaja: min=" + min + ", max=" + max;
    }
    
    
}
