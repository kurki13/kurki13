package debug.model.osasuoritukset;

import debug.model.Kurssi;
import debug.model.Osallistuminen;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Laskarit implements Iterable<Osasuoritus> {

    private List<Osasuoritus> laskarit;
    private int koko;
    


    @Override
    public Iterator<Osasuoritus> iterator() {
        return new OsasuoritusIterator(laskarit, koko);
    }

    public Laskarit(Kurssi kurssi, Osallistuminen osallistuminen) {
        koko = kurssi.getLaskarikerta_lkm();
        String dbMax = kurssi.getLaskaritehtava_lkm();
        String dbPisteet = osallistuminen.getLaskarisuoritukset();
        laskarit = new ArrayList();
        int[] maxPisteet = Muotoilija.stringToIntArray(dbMax);
        int[] pisteet = Muotoilija.stringToIntArray(dbPisteet);

        //Luodaan jokaiselle mahdolliselle laskarille yksi olio
        for (int i = 0; i < Muotoilija.MAX_KOKO; i++) {
            laskarit.add(new Osasuoritus(pisteet[i], maxPisteet[i]));
        }
    }
    
    
}
