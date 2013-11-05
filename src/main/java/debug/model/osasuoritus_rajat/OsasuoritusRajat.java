/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.osasuoritus_rajat;

import debug.model.osasuoritukset.Muotoilija;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author esaaksvu
 */
public class OsasuoritusRajat implements Iterable<OsasuoritusRaja> {

    private List<OsasuoritusRaja> osasuoritusrajat = new ArrayList();
    private int aktiivisia;

    @Override
    public Iterator<OsasuoritusRaja> iterator() {
        return new OsasuoritusRajaIterator(osasuoritusrajat, aktiivisia);
    }

    public String maxArvotTietokantamuodossa() {
        int[] maxs = Muotoilija.tyhjaTaulu();
        int i = 0;
        for (OsasuoritusRaja osasuoritusRaja : osasuoritusrajat) {
            maxs[i] = osasuoritusRaja.getMax();
            i++;
        }
        return Muotoilija.intArrayToString(maxs);
    }

    public String minArvotTietokantamuodossa() {
        int[] mins = Muotoilija.tyhjaTaulu();
        int i = 0;
        for (OsasuoritusRaja osasuoritusRaja : osasuoritusrajat) {
            mins[i] = osasuoritusRaja.getMin();
            i++;
        }
        return Muotoilija.intArrayToString(mins);
    }

    public void setAktiivisia(int aktiivisia) {
        this.aktiivisia = aktiivisia;
    }

    //TODO: Is prepared for nulls?
    public OsasuoritusRajat(String minstring, String maxstring, int aktiivisia) {
        this.aktiivisia = aktiivisia;
        int[] mins = Muotoilija.stringToIntArray(minstring);
        int[] max = Muotoilija.stringToIntArray(maxstring);
        for (int i = 0; i < Muotoilija.MAX_KOKO; i++) {
            try {
                osasuoritusrajat.add(new OsasuoritusRaja(mins[i], max[i]));
            } catch (IndexOutOfBoundsException ie) {
                osasuoritusrajat.add(new OsasuoritusRaja(Muotoilija.EMPTY, Muotoilija.EMPTY));
            }
        }
    }

    /*
     * Osien indeksit alkavat nollasta, eli kun haetaan esimerkiksi ensimmäisen
     * osan pisteitä, niin täytyykin hakea nollannen osan pisteitä jne.
     * Siksi miinustetaan yksi numerosta.
     */
    public OsasuoritusRaja osa(int numero) {
        numero--;
        if (numero >= osasuoritusrajat.size() || numero < 0) {
            return null;
        } else {
            return osasuoritusrajat.get(numero);
        }
    }

    public OsasuoritusRaja osa(String numero) {
        int nro = 0;
        try {
            nro = Integer.parseInt(numero);
        } catch (NumberFormatException nume) {
            return null;
        } catch (NullPointerException nulle) {
            return null;
        }

        return osa(nro);
    }

    public int getMaxPisteetYhteensa() {
        int ret = 0;
        for (OsasuoritusRaja raja : this) {
            if (raja.getMax() > 0) {
                ret += raja.getMax();
            }
        }
        return ret;
    }

    public int getMinPisteetYhteensa() {
        int ret = 0;
        for (OsasuoritusRaja raja : this) {
            if (raja.getMin() > 0) {
                ret += raja.getMin();
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        String ret = "";
        for (OsasuoritusRaja raja : this) {
            ret += raja + "<br>";
        }
        return ret;
    }
}