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

    Kurssi kurssi;
    Osallistuminen osallistuminen;
    List<Laskari> laskarit;
    int koko;

    @Override
    public Iterator<Laskari> iterator() {
        return laskarit.iterator();
    }

    String tietokantaString() {
        return Muotoilija.tietokantaString(laskarit);
    }

    public class Laskari {

        public int pisteet;
        public int maxPisteet;

        public Laskari(int pisteet, int maxPisteet) {
            this.pisteet = pisteet;
            this.maxPisteet = maxPisteet;
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
        for (int i = 0; i < koko; i++) {
            laskarit.add(new Laskari(pisteet[i], maxPisteet[i]));
        }
    }

    /**
     * Laskarikerran laskariNumero laskarit
     *
     * @param laskariNumero
     * @return
     */
    public Laskari laskari(int laskariNumero) {
        return laskarit.get(laskariNumero);
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
                System.out.println(Muotoilija.tietokantaString(osallistuminen.getLaskarit().laskarit));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Laskarit.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
