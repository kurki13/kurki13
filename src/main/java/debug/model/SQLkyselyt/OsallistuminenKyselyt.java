/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import debug.SessioApuri;
import debug.dbconnection.DatabaseConnection;
import debug.model.Kurssi;
import debug.model.Osallistuminen;
import debug.model.osasuoritukset.Muotoilija;
import debug.model.util.SQLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * K
 *
 * @author mkctammi
 */
public class OsallistuminenKyselyt {

    public static final String KURSSINOSALLISTUMISET
            = "SELECT os.*, op.etunimi, op.sukunimi, op.sahkopostiosoite \n"
            + "FROM osallistuminen os, opiskelija op \n"
            + "WHERE os.hetu = op.hetu \n"
            + "AND os.kurssikoodi = ? \n"
            + "AND os.lukukausi = ? \n"
            + "AND os.lukuvuosi = ? \n"
            + "AND os.tyyppi = ? \n"
            + "AND os.kurssi_nro = ? \n";

    public static List<Osallistuminen> osallistumisetKurssilla(Kurssi kurssi) throws SQLException {
        String query = KURSSINOSALLISTUMISET
                + "ORDER BY op.sukunimi ASC, op.etunimi ASC, os.hetu ASC";
        Connection conn = DatabaseConnection.makeConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, kurssi.getKurssikoodi());
        ps.setString(2, kurssi.getLukukausi());
        ps.setInt(3, kurssi.getLukuvuosi());
        ps.setString(4, kurssi.getTyyppi());
        ps.setInt(5, kurssi.getKurssi_nro());

        ResultSet rs = ps.executeQuery();

        List<Osallistuminen> osallistumiset = new ArrayList();

        while (rs.next()) {
            Osallistuminen add = new Osallistuminen();
            SQLoader.resultRowToTable(rs, add);
            osallistumiset.add(add);
            String etunimi = rs.getString("etunimi");
            String sukunimi = rs.getString("sukunimi");
            add.setNimi(etunimi + " " + sukunimi);
            add.setEtunimi(etunimi);
            add.setSukunimi(sukunimi);
            add.setEmail(rs.getString("sahkopostiosoite"));
        }

        for (Osallistuminen osallistuminen : osallistumiset) {
            osallistuminen.setKurssi(kurssi);
        }

        conn.close();
        return osallistumiset;
    }
    
    public static List<Osallistuminen> voimassaKurssilla(Kurssi kurssi) throws SQLException {
        String query = KURSSINOSALLISTUMISET
                + " AND os.voimassa = 'K' \n"
                + "ORDER BY op.etunimi";
        Connection conn = DatabaseConnection.makeConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, kurssi.getKurssikoodi());
        ps.setString(2, kurssi.getLukukausi());
        ps.setInt(3, kurssi.getLukuvuosi());
        ps.setString(4, kurssi.getTyyppi());
        ps.setInt(5, kurssi.getKurssi_nro());
        return SQLoader.loadTablesFromPreparedStatement(new Osallistuminen(), ps, conn);
    }

    public static List<Osallistuminen> poistetutKurssilta(Kurssi kurssi) throws SQLException {
        String query = KURSSINOSALLISTUMISET 
                + "AND os.voimassa = 'P' \n"
                + "ORDER BY os.hetu";
        Connection conn = DatabaseConnection.makeConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, kurssi.getKurssikoodi());
        ps.setString(2, kurssi.getLukukausi());
        ps.setInt(3, kurssi.getLukuvuosi());
        ps.setString(4, kurssi.getTyyppi());
        ps.setInt(5, kurssi.getKurssi_nro());
        return SQLoader.loadTablesFromPreparedStatement(new Osallistuminen(), ps, conn);
    }

    public static Osallistuminen osallistuminenKurssilla(Kurssi kurssi,
            String hetu) throws SQLException {
        String query = KURSSINOSALLISTUMISET + " AND os.hetu = ?";

        Connection conn = DatabaseConnection.makeConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(6, hetu);
        ps.setString(1, kurssi.getKurssikoodi());
        ps.setString(2, kurssi.getLukukausi());
        ps.setInt(3, kurssi.getLukuvuosi());
        ps.setString(4, kurssi.getTyyppi());
        ps.setInt(5, kurssi.getKurssi_nro());

        List<Osallistuminen> osallistumiset = SQLoader.loadTablesFromPreparedStatement(new Osallistuminen(), ps, conn);
        if (osallistumiset.isEmpty()) {
            return null;
        } else {
            osallistumiset.get(0).setKurssi(kurssi);
            return osallistumiset.get(0);
        }
    }

    public static void tallennaKantaan(Osallistuminen os) throws SQLException {
        SQLoader.tallennaKantaan(os);
    }

    public static void luoUusiOsallistuminen(String hetu_ryhma,
            Kurssi kurssi, HttpServletRequest request) {
        SimpleEntry<String, Integer> info = pilkoParametrit(hetu_ryhma);
        String hetu = info.getKey();
        int ryhma = info.getValue();
        if (tarkistaParametrit(kurssi, ryhma, hetu, request)) {
            SQLProseduurit.lisaaOpiskelija(kurssi, ryhma, hetu, request);
        }
    }

    public static void palautaKurssille(String hetu_ryhma,
            Kurssi kurssi, HttpServletRequest request) {
        SimpleEntry<String, Integer> info = pilkoParametrit(hetu_ryhma);
        String hetu = info.getKey();
        int ryhma = info.getValue();
        if (tarkistaParametrit(kurssi, ryhma, hetu, request)) {
            SQLProseduurit.palautaOpiskelija(kurssi, ryhma, hetu, request);
        }
    }
    
    public static void vaihdaRyhmaa(String hetu_ryhma,
            Kurssi kurssi, HttpServletRequest request) {
        SimpleEntry<String, Integer> info = pilkoParametrit(hetu_ryhma);
        String hetu = info.getKey();
        int ryhma = info.getValue();
        if (tarkistaParametrit(kurssi, ryhma, hetu, request)) {
            //99 on vain testausta varten
            SQLProseduurit.vaihdaOpiskelijanRyhmaa(99 ,kurssi, ryhma, hetu, request);
        }
    }

    public static void poistaKurssilta(String hetu_ryhma,
            Kurssi kurssi, HttpServletRequest request) {
        SimpleEntry<String, Integer> info = pilkoParametrit(hetu_ryhma);
        String hetu = info.getKey();
        int ryhma = info.getValue();
        if (tarkistaParametrit(kurssi, ryhma, hetu, request)) {
            SQLProseduurit.poistaOpiskelija(kurssi, ryhma, hetu, request);
        }
    }

    /**
     * Pilkkoo hetu_ryhmä String parametrin Stringiksi ja intiksi
     *
     * @param hetu_ryhma String 014020003_99
     * @return yksi alkioinene mappi joka sisältää hetun ja ryhmän
     */
    public static SimpleEntry pilkoParametrit(String hetu_ryhma) {
        try {
            
            String[] info = hetu_ryhma.split("_");
            String hetu = info[0];
            int ryhma = Integer.parseInt(info[1]);
            return new SimpleEntry<String, Integer>(hetu, ryhma);
        } catch (NumberFormatException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    public static boolean tarkistaParametrit(Kurssi kurssi, int ryhma,
            String hetu, HttpServletRequest request) {

        HttpSession session = request.getSession();
        if (kurssi == null) {
            SessioApuri.annaVirhe(session, "Kurssia ei ole valittu");
            return false;
        }
        if (!Muotoilija.hetuTarkastus(hetu)) {
            SessioApuri.annaVirhe(session, "Anna kelvollinen opiskelijanumero");
            return false;
        }
        if (ryhma > 99 && ryhma < 1) {
            SessioApuri.annaVirhe(session, "Anna kunnollinen ryhmä");
            return false;
        }
        return true;
    }
}
