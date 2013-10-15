package debug.model.osasuoritukset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Osasuoritukset implements Iterable<Osasuoritus> {

    private List<Osasuoritus> laskarit;
    private int koko;
    


    @Override
    public Iterator<Osasuoritus> iterator() {
        return new OsasuoritusIterator(laskarit, koko);
    }

    public Osasuoritukset(String dbmax_pisteet, String dbpisteet, int aktiivisia) {
        koko = aktiivisia;
        laskarit = new ArrayList();
        int[] maxPisteet = Muotoilija.stringToIntArray(dbmax_pisteet);
        int[] pisteet = Muotoilija.stringToIntArray(dbpisteet);

        //Luodaan jokaiselle mahdolliselle laskarille yksi olio
        for (int i = 0; i < Muotoilija.MAX_KOKO; i++) {
            laskarit.add(new Osasuoritus(pisteet[i], maxPisteet[i]));
        }
    }
    
    
}
