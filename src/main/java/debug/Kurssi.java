/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author mkctammi
 */
public class Kurssi {

    String kurssikoodi;
    String lukukausi;
    int lukuvuosi;
    String tyyppi;
    int kurssi_nro;
    String nimi;

    ArrayList<Osallistuminen> osallistumiset;

    public Kurssi(String kurssikoodi, String lukukausi, int lukuvuosi, String tyyppi, int kurssi_nro, String nimi) throws ClassNotFoundException, SQLException {
        this.kurssikoodi = kurssikoodi;
        this.lukukausi = lukukausi;
        this.lukuvuosi = lukuvuosi;
        this.tyyppi = tyyppi;
        this.kurssi_nro = kurssi_nro;
        this.nimi = nimi;
        osallistumiset = Pipe.osallistumisetKurssilla(this);
    }
}
