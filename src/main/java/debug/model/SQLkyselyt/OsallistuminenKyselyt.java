/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import debug.model.Kurssi;
import debug.model.Osallistuminen;
import debug.model.util.Filter;
import debug.model.util.SQLoader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mkctammi
 */
public class OsallistuminenKyselyt {

    public static List<Osallistuminen> listaKurssinOsallistumisista(Kurssi kurssi) throws SQLException {
        Filter f1 = new Filter(Osallistuminen.kurssikoodi, kurssi.getValue(Kurssi.kurssikoodi));
        Filter f2 = new Filter(Osallistuminen.lukukausi, kurssi.getValue(Kurssi.lukukausi));
        Filter f3 = new Filter(Osallistuminen.lukuvuosi, kurssi.getValue(Kurssi.lukuvuosi));
        Filter f4 = new Filter(Osallistuminen.tyyppi, kurssi.getValue(Kurssi.tyyppi));
        Filter f5 = new Filter(Osallistuminen.kurssi_nro, kurssi.getValue(Kurssi.kurssi_nro));
        ArrayList<Filter> filters = new ArrayList<Filter>();
        filters.add(f1);
        filters.add(f2);
        filters.add(f3);
        filters.add(f4);
        filters.add(f5);
        List<Osallistuminen> osallistumiset = SQLoader.loadTable(new Osallistuminen(), filters);
        for (Osallistuminen osallistuminen : osallistumiset) {
            osallistuminen.setKurssi(kurssi);
        }
        return osallistumiset;
    }
}
