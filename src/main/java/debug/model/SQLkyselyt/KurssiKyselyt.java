/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import debug.DatabaseConnection;
import debug.model.Kurssi;
import debug.model.util.Filter;
import debug.model.util.SQLoader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mkctammi
 */
public class KurssiKyselyt {

    private static final int MONTHS_OPEN = 12;
    private static final int SUPER_OPEN = 48;
    private static final String asetaSuorituspvm = //<editor-fold defaultstate="collapsed" desc="asetaSuorituspvm">
            "UPDATE kurssi SET suoritus_pvm = ?\n"
                + " WHERE kurssikoodi = ?\n"
                + " AND lukukausi = ?\n"
                + " AND lukuvuosi = ?\n"
                + " AND tyyppi = ?\n"
                + " AND kurssi_nro = ?\n";
            //</editor-fold>
    private static final String COURSE_INFOS = //<editor-fold defaultstate="collapsed" desc="courseInfos">

            // LUENTO- (JA LABORATORIOKURSSIT)
            "SELECT DISTINCT "
            + "     k.*, \n"
            + "     k.nimi AS nimiOrder, \n"
            + "     k.alkamis_pvm AS alkamis_pvmOrder,\n"
            + "     decode(k.tila, 'J', 'X', 'A') AS orderBy\n"
            + "  FROM kurssi k, opetus o, opetustehtavan_hoito oh, henkilo h\n"
            + "  WHERE k.paattymis_pvm > to_date('31.12.2001','DD.MM.YYYY')\n"
            + "    AND ADD_MONTHS( k.paattymis_pvm, " + MONTHS_OPEN + " ) >= SYSDATE\n"
            + "    AND k.tila not in ('S', 'O')\n"
            + "    AND k.tyyppi in ('K', 'S', 'A')\n" // luento- ja laboratoriokurssit sekä seminaarit
            + "    AND o.kurssikoodi = k.kurssikoodi AND oh.kurssikoodi = k.kurssikoodi\n"
            + "    AND o.lukukausi = k.lukukausi AND oh.lukukausi = k.lukukausi\n"
            + "    AND o.lukuvuosi = k.lukuvuosi AND oh.lukuvuosi = k.lukuvuosi\n"
            + "    AND o.tyyppi = k.tyyppi AND oh.tyyppi = k.tyyppi\n"
            + "    AND o.kurssi_nro = k.kurssi_nro AND oh.kurssi_nro = k.kurssi_nro\n"
            + "    AND oh.htunnus = h.htunnus\n"
            + "    AND h.ktunnus = ?\n"
            // KOKEET
            + "UNION\n"
            + "(SELECT DISTINCT "
            + "     ku.*, \n"
            + "     ku.nimi AS nimiOrder2, \n"
            + "     ku.alkamis_pvm as alkamis_pvmOrder2, \n"
            + "     decode(ku.tila, 'J', 'X', 'A') AS orderBy\n"
            + "  FROM kurssi ku, koe ko, henkilo h\n"
            + "  WHERE ku.paattymis_pvm > to_date('31.12.2001','DD.MM.YYYY')\n"
            + "    AND ADD_MONTHS( ku.paattymis_pvm, " + MONTHS_OPEN + " ) >= SYSDATE\n"
            + "    AND ku.tyyppi = 'L'\n" // vain kokeet
            + "    AND ku.tila not in ('S', 'O')\n"
            + "    AND ko.kurssikoodi = ku.kurssikoodi\n"
            + "    AND ko.lukukausi = ku.lukukausi\n"
            + "    AND ko.lukuvuosi = ku.lukuvuosi\n"
            + "    AND ko.tyyppi = ku.tyyppi\n"
            + "    AND ko.kurssi_nro = ku.kurssi_nro\n"
            + "    AND h.htunnus = ko.htunnus\n"
            + "    AND h.ktunnus = ?)\n"
            + "ORDER BY orderBy ASC, nimiOrder ASC, alkamis_pvmOrder ASC";
    //</editor-fold>
    private static final String SUPER_INFOS =
            //<editor-fold defaultstate="collapsed" desc="superInfos">
            "SELECT DISTINCT "
            + "     k.*, "
            + "     k.nimi AS nimiOrder, "
            + "     k.alkamis_pvm AS alkamis_pvmOrder,"
            + "     decode(k.tila, 'J', 'X', 'A') AS orderBy"
            + "  FROM kurssi k"
            + "  WHERE k.paattymis_pvm > to_date('31.12.2001','DD.MM.YYYY')"
            + "    AND ADD_MONTHS( k.paattymis_pvm, " + SUPER_OPEN + " ) >= SYSDATE"
            + "    AND k.tila not in ('S', 'O')"
            // KOKEET
            + "UNION\n"
            + "(SELECT DISTINCT "
            + "     ku.*, ku.nimi AS nimiOrder2, "
            + "     ku.alkamis_pvm AS alkamis_pvmOrder2,"
            + "     decode(ku.tila, 'J', 'X', 'A') AS orderBy"
            + "  FROM kurssi ku"
            + "  WHERE ku.paattymis_pvm > to_date('31.12.2001','DD.MM.YYYY')"
            + "     AND ADD_MONTHS( ku.paattymis_pvm, " + SUPER_OPEN + " ) >= SYSDATE"
            + "     AND ku.tila not in ('S', 'O')"
            + "     AND ku.tyyppi = 'L')" // vain kokeet
            + "ORDER BY orderBy ASC, nimiOrder ASC, alkamis_pvmOrder ASC";
    //</editor-fold>

    public static List<Kurssi> kurssitYllapitajalle() throws SQLException {
        Connection databaseConnection = DatabaseConnection.makeConnection();
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(SUPER_INFOS);
        return SQLoader.loadTablesFromPreparedStatement(new Kurssi(), preparedStatement, databaseConnection);
    }

    public static List<Kurssi> kurssitKayttajalle(String ruser) throws SQLException {
        Connection databaseConnection = DatabaseConnection.makeConnection();
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(COURSE_INFOS);
        preparedStatement.setString(1, ruser);
        preparedStatement.setString(2, ruser);
        return SQLoader.loadTablesFromPreparedStatement(new Kurssi(), preparedStatement, databaseConnection);
    }

    public static Kurssi kurssiIDlla(String s) throws SQLException {
        String[] sd = s.split("\\.");
        return kurssitIDlla(sd[0], sd[1], sd[2], sd[3], sd[4]).get(0);
    }

    public static List<Kurssi> kurssitIDlla(String kKoodi, String lKausi,
            String lVuosi, String tyyppi, String kNro) throws SQLException {

        List<Filter> f = new ArrayList();
        if (!kKoodi.equals("")) {
            f.add(new Filter(Kurssi.kurssikoodi, kKoodi));
        }
        if (!lKausi.equals("")) {
            f.add(new Filter(Kurssi.lukukausi, lKausi));
        }
        if (!lVuosi.equals("")) {
            try {
                f.add(new Filter(Kurssi.lukuvuosi, Integer.parseInt(lVuosi)));
            } catch (NumberFormatException e) {
            }
        }
        if (!tyyppi.equals("")) {
            f.add(new Filter(Kurssi.tyyppi, tyyppi));
        }
        if (!kNro.equals("")) {
            try {
                f.add(new Filter(Kurssi.kurssi_nro, Integer.parseInt(kNro)));
            } catch (NumberFormatException e) {
            }
        }
        return SQLoader.loadTable(new Kurssi(), f);
    }
    
    public static void asetaSuorituspvm(Kurssi kurssi, String suorituspvm) throws SQLException {
        Connection tietokantayhteys = DatabaseConnection.makeConnection();
        PreparedStatement valmisteltuLause = tietokantayhteys.prepareStatement(asetaSuorituspvm);
        valmisteltuLause.setDate(1, Date.valueOf(suorituspvm));
        valmisteltuLause.setString(2, kurssi.getKurssikoodi());
        valmisteltuLause.setString(3, kurssi.getLukukausi());
        valmisteltuLause.setInt(4, kurssi.getLukuvuosi());
        valmisteltuLause.setString(5, kurssi.getTyyppi());
        valmisteltuLause.setInt(6, kurssi.getKurssi_nro());
        valmisteltuLause.executeUpdate();
        valmisteltuLause.close();
        tietokantayhteys.close();
    }
}
