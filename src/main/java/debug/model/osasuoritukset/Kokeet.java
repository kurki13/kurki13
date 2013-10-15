
package debug.model.osasuoritukset;

import debug.model.Kurssi;
import debug.model.Osallistuminen;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Kokeet implements Iterable<Osasuoritus> {

    private List<Osasuoritus> kokeet;
    private int koko;

    @Override
    public Iterator<Osasuoritus> iterator() {
        return new OsasuoritusIterator(kokeet, koko);
    }

    public Kokeet(Kurssi kurssi, Osallistuminen osallistuminen) {
        koko = kurssi.getValikokeet_lkm();
        String dbMax = kurssi.getMax_koepisteet();
        String dbPisteet = osallistuminen.getKoepisteet();
        kokeet = new ArrayList();
        int[] maxPisteet = Muotoilija.stringToIntArray(dbMax);
        int[] pisteet = Muotoilija.stringToIntArray(dbPisteet);

        //Luodaan jokaiselle mahdolliselle laskarille yksi olio
        for (int i = 0; i < Muotoilija.MAX_KOKO; i++) {
            kokeet.add(new Osasuoritus(pisteet[i], maxPisteet[i]));
        }
    }
}
