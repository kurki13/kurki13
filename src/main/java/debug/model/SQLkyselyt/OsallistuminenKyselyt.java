/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import debug.dbconnection.DatabaseConnection;
import debug.model.Kurssi;
import debug.model.Osallistuminen;
import debug.model.util.SQLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mkctammi
 */
public class OsallistuminenKyselyt {

    public static final String KURSSINOSALLISTUMISET =
            "SELECT os.*, op.etunimi, op.sukunimi, op.sahkopostiosoite \n"
            + "FROM osallistuminen os, opiskelija op \n"
            + "WHERE os.hetu = op.hetu \n"
            + "AND os.kurssikoodi = ? \n"
            + "AND os.lukukausi = ? \n"
            + "AND os.lukuvuosi = ? \n"
            + "AND os.tyyppi = ? \n"
            + "AND os.kurssi_nro = ? \n";

    private static void valmisteleKurssi(PreparedStatement ps,
            Kurssi kurssi) throws SQLException {
        ps.setString(1, kurssi.getKurssikoodi());
        ps.setString(2, kurssi.getLukukausi());
        ps.setInt(3, kurssi.getLukuvuosi());
        ps.setString(4, kurssi.getTyyppi());
        ps.setInt(5, kurssi.getKurssi_nro());
    }

    private static void asetaFiltterit(PreparedStatement ps, String sukunimi,
            String ryhmaFilter, String hetuFilter) throws SQLException {

        ps.setString(6, "%" + sukunimi + "%");
        ps.setString(7, "%" + hetuFilter + "%");
        try {
            ps.setInt(8, Integer.parseInt(ryhmaFilter));
        } catch (NumberFormatException e) {
        }
    }

    private static List<Osallistuminen> lisaaOpiskelijaTiedot(ResultSet rs, Kurssi kurssi) throws SQLException {
        List<Osallistuminen> osallistumiset = new ArrayList();

        while (rs.next()) {
            Osallistuminen uusi = new Osallistuminen();
            SQLoader.resultRowToTable(rs, uusi);
            osallistumiset.add(uusi);
            String etunimi = rs.getString("etunimi");
            String sukunimi = rs.getString("sukunimi");
            uusi.setNimi(etunimi + " " + sukunimi);
            uusi.setEtunimi(etunimi);
            uusi.setSukunimi(sukunimi);
            uusi.setEmail(rs.getString("sahkopostiosoite"));
        }

        for (Osallistuminen osallistuminen : osallistumiset) {
            osallistuminen.setKurssi(kurssi);
        }
        return osallistumiset;
    }

    private static List<Osallistuminen> luoYhteys(Kurssi kurssi, String query,
            String... filterit) throws SQLException {
        Connection conn = DatabaseConnection.makeConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        valmisteleKurssi(ps, kurssi);
        if (filterit.length == 3) {
            asetaFiltterit(ps, filterit[0], filterit[1], filterit[2]);
        }
        ResultSet rs = ps.executeQuery();
        List osallistumiset = lisaaOpiskelijaTiedot(rs, kurssi);
        conn.close();
        return osallistumiset;
    }

    public static List<Osallistuminen> osallistumisetKurssilla(Kurssi kurssi) throws SQLException {
        String query = KURSSINOSALLISTUMISET
                + "ORDER BY op.sukunimi ASC, op.etunimi ASC, os.hetu ASC";
        return luoYhteys(kurssi, query);
    }

    public static List<Osallistuminen> voimassaKurssilla(Kurssi kurssi,
            String sukunimi, String ryhmaFilter, String hetuFilter) throws SQLException {
        String query = KURSSINOSALLISTUMISET
                + "AND os.voimassa = 'K' \n"
                + "AND op.sukunimi like ? \n"
                + "AND op.hetu like ? \n"
                + ((!ryhmaFilter.equals("")) ? "AND os.ilmo_jnro = ? \n" : "")
                + "ORDER BY op.sukunimi ASC, op.etunimi ASC, os.hetu ASC";
        return luoYhteys(kurssi, query, sukunimi, ryhmaFilter, hetuFilter);
    }

    public static List<Osallistuminen> poistetutKurssilta(Kurssi kurssi,
            String sukunimi, String ryhmaFilter, String hetuFilter) throws SQLException {
        String query = KURSSINOSALLISTUMISET
                + "AND os.voimassa = 'P' \n"
                + "AND op.sukunimi like ? \n"
                + "AND op.hetu like ? \n"
                + ((!ryhmaFilter.equals("")) ? "AND os.ilmo_jnro = ? \n" : "")
                + "ORDER BY op.sukunimi ASC, op.etunimi ASC, os.hetu ASC";
        return luoYhteys(kurssi, query, sukunimi, ryhmaFilter, hetuFilter);
    }

    public static Osallistuminen osallistuminenKurssilla(Kurssi kurssi,
            String hetu) throws SQLException {
        String query = KURSSINOSALLISTUMISET
                + " AND os.hetu = ?";

        Connection conn = DatabaseConnection.makeConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        valmisteleKurssi(ps, kurssi);
        ps.setString(6, hetu);

        List<Osallistuminen> osallistumiset = SQLoader.loadTablesFromPreparedStatement(new Osallistuminen(), ps, conn);
        if (osallistumiset.isEmpty() || osallistumiset.size() > 1) {
            return null;
        } else {
            osallistumiset.get(0).setKurssi(kurssi);
            return osallistumiset.get(0);
        }
    }

    public static void tallennaKantaan(Osallistuminen os) throws SQLException {
        SQLoader.tallennaKantaan(os);
    }

}
