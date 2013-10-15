
package debug.model.osasuoritukset;

import debug.model.Kurssi;
import debug.model.Osallistuminen;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Harjoitustyot implements Iterable<Osasuoritus> {

    private List<Osasuoritus> harkat;
    private int koko;

    @Override
    public Iterator<Osasuoritus> iterator() {
        return new OsasuoritusIterator(harkat, koko);
    }

    public Harjoitustyot(Kurssi kurssi, Osallistuminen osallistuminen) {
        koko = kurssi.getHarjoitustyo_lkm();
        String dbMax = kurssi.getMax_harjoitustyopisteet();
        String dbPisteet = osallistuminen.getHarjoitustyopisteet();
        harkat = new ArrayList();
        int[] maxPisteet = Muotoilija.stringToIntArray(dbMax);
        int[] pisteet = Muotoilija.stringToIntArray(dbPisteet);

        //Luodaan jokaiselle mahdolliselle laskarille yksi olio
        for (int i = 0; i < Muotoilija.MAX_KOKO; i++) {
            harkat.add(new Osasuoritus(pisteet[i], maxPisteet[i]));
        }
    }
}
