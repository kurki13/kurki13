package debug.model;

import java.util.Arrays;

public class Laskarit {

    final static int MAX_KOKO = 18;
    final static int MAX_PISTE = 99;
    Kurssi kurssi;
    Osallistuminen osallistuminen;
    int[] pisteet;
    int[] maxPisteet;
    int koko;

    public Laskarit(Kurssi kur, Osallistuminen os) {
        String max = kur.getLaskaritehtava_lkm();
        String pi = os.getLaskarisuoritukset();
        maxPisteet = parseString(max);
        pisteet = parseString(pi);
    }

    private static int[] parseString(String s) {
        String[] intAr = s.replace('-', ',').split(",");
        int[] ints = new int[intAr.length];
        int i = 0;
        for (String string : intAr) {
            try {
                ints[i] = Integer.valueOf(string.trim());
            } catch (NumberFormatException e) {
                ints[i] = -1;
            }
            i++;
        }
        return ints;
    }

    public boolean setPiste(int piste, int index) {
        if (index < 0 || index > koko - 1
                || piste < maxPisteet[index]) {
            return false;
        }
        pisteet[index] = piste;
        return true;
    }

    public void setMaxPiste(int max, int index) {
        if (index < koko - 1 && index >= 0 && max <= MAX_PISTE) {
            pisteet[index] = max;
        }
    }

    public int[] getPisteet() {
        return pisteet;
    }

    public int[] getMaxPisteet() {
        return maxPisteet;
    }

    public static void main(String[] args) {
        String i = "7, 6,??,??,??-??,??,??,??,??-??,??,??,??,??-??,??,??";
        System.out.println(Arrays.toString(parseString(i)));

    }
}
