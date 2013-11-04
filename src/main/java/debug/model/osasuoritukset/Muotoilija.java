package debug.model.osasuoritukset;

import java.util.Arrays;
import java.util.List;

/**
 * Tämä luokka muotoilee tietokannassa käytettävän formaatin numerotaulukoiksi.
 * Kannan muoto sisältää 18 pilkulla tai väliviivalla erotettua arvoa.
 * Esimerkiksi: 1,2,3,4,5-6,??,8,9,10-11,12,13,14,15,16,17,99 Asettamaton arvo
 * voidaan esittää kysymysmerkein.
 *
 * @author ahathoor
 */
public class Muotoilija {

    public final static int MAX_KOKO = 18;
    public final static int MAX_PISTE = 99;
    public final static int EMPTY = -1;
    public final static int LASNA = -2;

    public static int[] stringToIntArray(String s) {
        if (s == null) {
            int[] ints = new int[MAX_KOKO];
            Arrays.fill(ints, EMPTY);
            return ints;
        }
        String[] intAr = s.replace('-', ',').split(",");
        int[] ints = new int[intAr.length];
        int i = 0;
        for (String string : intAr) {
            try {
                ints[i] = Integer.valueOf(string.trim());
                if (ints[i] > 99) {
                    ints[i] = 99;
                }
            } catch (NumberFormatException e) {
                if (string.trim().equals("+")) {
                    ints[i] = LASNA;
                } else {
                    ints[i] = EMPTY;
                }
            }
            i++;
        }
        return ints;
    }

    public static String intArrayToString(int[] ints) {
        String ret = "";
        for (int i = 0; i < ints.length; i++) {
            int piste = ints[i];
            if (piste == EMPTY) {
                ret += "??";
            }
            else if (piste == LASNA) {
                ret += " +";
            } else {
                if (piste < 10) {
                    ret += " ";
                }
                ret += piste;
            }
            if (i == 4 || i == 9 || i == 14) {
                ret += "-";
            } else if (i != ints.length - 1) { //vikaan ei pilkkua
                ret += ",";
            }
        }
        return ret;
    }

    public static int[] tyhjaTaulu() {
        int[] ints = new int[MAX_KOKO];
        Arrays.fill(ints, EMPTY);
        return ints;
    }
}
