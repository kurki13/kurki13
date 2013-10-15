package debug.model.osasuoritus_rajat;

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

    public boolean setMin(int min) {
        if (this.min == min) {
            return false;
        }
        this.min = min;
        return true;
    }

    public boolean setMax(int max) {
        if (this.max == max) {
            return false;
        }
        this.max = max;
        return true;
    }

    public boolean setMin(String min) {
        try {
            return setMin(Integer.parseInt(min));
        } catch (NumberFormatException ne) {
            return false;
        }
    }

    public boolean setMax(String max) {
        try {
            return setMax(Integer.parseInt(max));
        } catch (NumberFormatException ne) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "OsasuoritusRaja: min=" + min + ", max=" + max;
    }
}
