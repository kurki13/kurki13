/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model;

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
    
    public OsasuoritusRajat(String minstring, String maxstring, int aktiivisia) {
        this.aktiivisia = aktiivisia;
        int[] mins = Muotoilija.stringToIntArray(minstring);
        int[] max = Muotoilija.stringToIntArray(maxstring);
        for (int i = 0; i < Muotoilija.MAX_KOKO; i++) {
            osasuoritusrajat.add(new OsasuoritusRaja(mins[i], max[i]));
        }
    }

}