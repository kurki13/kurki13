package debug.model.osasuoritukset;

import debug.ApplicationException;
import debug.model.Kurssi;
import debug.model.osasuoritukset.Laskarit.Laskari;
import debug.model.Osallistuminen;
import debug.model.SQLkyselyt.OsallistuminenKyselyt;
import debug.model.util.Filter;
import debug.model.util.SQLoader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Laskarit implements Iterable<Laskari> {

    private List<Laskari> laskarit;
    private int koko;

    @Override
    public Iterator<Laskari> iterator() {
        class LaskariIterator implements Iterator<Laskari> {

            int index = 0;

            @Override
            public boolean hasNext() {
                //Ei anneta käyttäjälle kuin aktiivinen osa laskareista käsiteltäväksi
                return index < koko;
            }

            @Override
            public Laskari next() {
                index++;
                return laskarit.get(index - 1);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Osasuorituksia ei voi poistaa, vain muuttaa tyhjiksi");
            }
        }
        return new LaskariIterator();
    }

    String tietokantaString() {
        return Muotoilija.tietokantaString(this);
    }

    public class Laskari {

        public int pisteet;
        public int maxPisteet;

        public Laskari(int pisteet, int maxPisteet) {
            this.pisteet = pisteet;
            this.maxPisteet = maxPisteet;
            if (pisteet > maxPisteet) {
                pisteet = maxPisteet;
            }
            if (pisteet < 0) {
                pisteet = Muotoilija.EMPTY;
            }
        }

        public int getPisteet() {
            return pisteet;
        }

        public void setPisteet(int pisteet) {
            if (pisteet > maxPisteet || pisteet < 0) {
                throw new ApplicationException("Pisteiden täytyy olla välillä 0-" + maxPisteet
                        + " saatiin " + pisteet);
            }
            this.pisteet = pisteet;
        }

        public void poistaPisteet() {
            this.pisteet = Muotoilija.EMPTY;
        }

        public int getMaxPisteet() {
            return maxPisteet;
        }
    }

    public Laskarit(Kurssi kurssi, Osallistuminen osallistuminen) {
        koko = kurssi.getLaskarikerta_lkm();
        String max = kurssi.getLaskaritehtava_lkm();
        String pi = osallistuminen.getLaskarisuoritukset();
        laskarit = new ArrayList();
        int[] maxPisteet = Muotoilija.stringToIntArray(max);
        int[] pisteet = Muotoilija.stringToIntArray(pi);

        //Luodaan jokaiselle mahdolliselle laskarille yksi olio
        for (int i = 0; i < Muotoilija.MAX_KOKO; i++) {
            laskarit.add(new Laskari(pisteet[i], maxPisteet[i]));
        }
    }

    public static void main(String[] args) {
        try {
            List<Filter> filters = new ArrayList();
            filters.add(new Filter(Kurssi.nimi, "Data Mining"));
            List<Kurssi> test = SQLoader.loadTable(new Kurssi(), filters);
            Kurssi kurssi = test.get(0);
            System.out.println(kurssi.getNimi());
            List<Osallistuminen> oss = OsallistuminenKyselyt.osallistumisetKurssilla(kurssi);
            for (Osallistuminen osallistuminen : oss) {
                System.out.print(osallistuminen.getHetu() + ":  ");
                for (Laskari laskari : osallistuminen.getLaskarit()) {
                    System.out.print(laskari.pisteet + "\t");
                }
                System.out.println("");
                System.out.println(Muotoilija.tietokantaString(osallistuminen.getLaskarit()));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Laskarit.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
