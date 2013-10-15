/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.osasuoritukset;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author esaaksvu
 */
public class Muotoilija {

    final static int MAX_KOKO = 18;
    final static int MAX_PISTE = 99;
    final static int EMPTY = -1;

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
            } catch (NumberFormatException e) {
                ints[i] = EMPTY;
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
            } else {
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

    public static String tietokantaString(Iterable<Osasuoritus> osasuoritukset) {
        int[] ints = Muotoilija.tyhjaTaulu();
        int i=0;
        for (Osasuoritus osasuoritus : osasuoritukset) {
            ints[i] = osasuoritus.getPisteet();
            i++;
        }
        return Muotoilija.intArrayToString(ints);
    }

    public static int[] tyhjaTaulu() {
        int[] ints = new int[MAX_KOKO];
        Arrays.fill(ints, EMPTY);
        return ints;
    }
}
