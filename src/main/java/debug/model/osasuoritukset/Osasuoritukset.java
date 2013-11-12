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
    
    public int getYhteispisteet() {
        int pisteet = 0;
        for (Osasuoritus os: osasuoritukset) {
            if (os.getPisteet() > 0 && os.getPisteet() < 99) {
                pisteet += os.getPisteet();
            }
        }        
        return pisteet;
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
    
    /*
     * Osien indeksit alkavat nollasta, eli kun haetaan esimerkiksi ensimmäisen
     * osan pisteitä, niin täytyykin hakea nollannen osan pisteitä jne.
     * Siksi miinustetaan yksi numerosta.
     */
    public Osasuoritus osa(int numero) {
        numero--;
        if (numero >= osasuoritukset.size() || numero < 0) {
            return null;
        } else {
            return osasuoritukset.get(numero);
        }
    }
    
    public Osasuoritus osa(String numero) {
        int nro = 0;
        try {
        nro = Integer.parseInt(numero);
        } catch (NumberFormatException nume) { return null; }
          catch (NullPointerException nulle) { return null; }
        
        return osa(nro);
    }
    
}
