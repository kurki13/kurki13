package debug.model.osasuoritukset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Osasuoritukset implements Iterable<Osasuoritus> {

    private List<Osasuoritus> osasuoritukset;
    private int koko;
    


    @Override
    public Iterator<Osasuoritus> iterator() {
        return new OsasuoritusIterator(osasuoritukset, koko);
    }
    
    public String pisteetTietokantamuodossa() {
        int[] suoritukset = Muotoilija.tyhjaTaulu();
        int i=0;
        for (Osasuoritus osasuoritus : osasuoritukset) {
            suoritukset[i] = osasuoritus.getPisteet();
            i++;
        }
        return Muotoilija.intArrayToString(suoritukset);
    }

    public Osasuoritukset(String dbmax_pisteet, String dbpisteet, int aktiivisia) {
        koko = aktiivisia;
        osasuoritukset = new ArrayList();
        int[] maxPisteet = Muotoilija.stringToIntArray(dbmax_pisteet);
        int[] pisteet = Muotoilija.stringToIntArray(dbpisteet);

        //Luodaan jokaiselle mahdolliselle laskarille yksi olio
        for (int i = 0; i < Muotoilija.MAX_KOKO; i++) {
            osasuoritukset.add(new Osasuoritus(pisteet[i], maxPisteet[i]));
        }
    }
    
    
}
