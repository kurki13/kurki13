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
 *
 * @author mkctammi
 */
public class OsallistuminenKyselyt {

    private static class Osallistumistiedot {

        String hetu;
        int ryhmänro;
        int uusiRyhma;

        public Osallistumistiedot(String hetu, int ryhma) {
            this.hetu = hetu;
            this.ryhmänro = ryhma;
        }

        public Osallistumistiedot(String hetu, int ryhma, int uusiRyhma) {
            this.hetu = hetu;
            this.ryhmänro = ryhma;
            this.uusiRyhma = uusiRyhma;
        }
    }
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

    public static List<Osallistuminen> osallistumisetKurssilla(Kurssi kurssi) throws SQLException {
        String query = KURSSINOSALLISTUMISET
                + "ORDER BY op.sukunimi ASC, op.etunimi ASC, os.hetu ASC";
        Connection conn = DatabaseConnection.makeConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        valmisteleKurssi(ps, kurssi);
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

    public static List<Osallistuminen> voimassaKurssilla(Kurssi kurssi,
            String sukunimi, String ryhmaFilter, String hetuFilter) throws SQLException {
        String query = KURSSINOSALLISTUMISET
                + "AND os.voimassa = 'K' \n"
                + "AND op.sukunimi like ? \n"
                + "AND op.hetu like ? \n"
                + ((!ryhmaFilter.equals("")) ? "AND os.ilmo_jnro = ? \n" : "")
                + "ORDER BY op.sukunimi ASC, op.etunimi ASC, os.hetu ASC";
        Connection conn = DatabaseConnection.makeConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        valmisteleKurssi(ps, kurssi);
        asetaFiltterit(ps, sukunimi, ryhmaFilter, hetuFilter);
        return SQLoader.loadTablesFromPreparedStatement(new Osallistuminen(), ps, conn);
    }

    public static List<Osallistuminen> poistetutKurssilta(Kurssi kurssi,
            String sukunimi, String ryhmaFilter, String hetuFilter) throws SQLException {
        String query = KURSSINOSALLISTUMISET
                + "AND os.voimassa = 'P' \n"
                + "AND op.sukunimi like ? \n"
                + "AND op.hetu like ? \n"
                + ((!ryhmaFilter.equals("")) ? "AND os.ilmo_jnro = ? \n" : "")
                + "ORDER BY op.sukunimi ASC, op.etunimi ASC, os.hetu ASC";
        Connection conn = DatabaseConnection.makeConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        valmisteleKurssi(ps, kurssi);
        asetaFiltterit(ps, sukunimi, ryhmaFilter, hetuFilter);
        return SQLoader.loadTablesFromPreparedStatement(new Osallistuminen(), ps, conn);
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

    public static void luoUusiOsallistuminen(String parametrit,
            Kurssi kurssi, HttpServletRequest request) {
        Osallistumistiedot tiedot = pilkoParametrit(parametrit);
        if (tarkistaParametrit(kurssi, tiedot, request)) {
            SQLProseduurit.lisaaOpiskelija(kurssi, tiedot.ryhmänro, tiedot.hetu, request);
        }
    }

    public static void palautaKurssille(String parametrit,
            Kurssi kurssi, HttpServletRequest request) {
        Osallistumistiedot tiedot = pilkoParametrit(parametrit);
        if (tarkistaParametrit(kurssi, tiedot, request)) {
            SQLProseduurit.palautaOpiskelija(kurssi, tiedot.ryhmänro, tiedot.hetu, request);
        }
    }

    public static void vaihdaRyhmaa(String parametrit,
            Kurssi kurssi, HttpServletRequest request) {
        Osallistumistiedot tiedot = pilkoParametrit(parametrit);
        if (tarkistaParametrit(kurssi, tiedot, request) && tiedot.uusiRyhma != 0) {
            SQLProseduurit.vaihdaOpiskelijanRyhmaa(tiedot.uusiRyhma, kurssi,
                    tiedot.ryhmänro, tiedot.hetu, request);
        } else {
            SessioApuri.annaVirhe(request.getSession(), "Uusi ryhmä ei saa olla 0");
        }
    }

    public static void poistaKurssilta(String parametrit,
            Kurssi kurssi, HttpServletRequest request) {
        Osallistumistiedot tiedot = pilkoParametrit(parametrit);
        if (tarkistaParametrit(kurssi, tiedot, request)) {
            SQLProseduurit.poistaOpiskelija(kurssi, tiedot.ryhmänro, tiedot.hetu, request);
        }
    }

    public static void sulataOpiskelija(Kurssi kurssi, String hetu,
            HttpServletRequest request) {
        try {
            Osallistuminen os = osallistuminenKurssilla(kurssi, hetu);
            if (os.getJaassa().equals("S")) {
                SessioApuri.annaVirhe(request.getSession(), "Opiskelija on jo sulatettu");
            }
            os.setJaassa("S");
            SQLoader.tallennaKantaan(os);
             SessioApuri.annaViesti(request.getSession(), "Sulatus onnistui");
        } catch (Exception e) {
            SessioApuri.annaVirhe(request.getSession(), "Sulatus epäonnistui");
        }
    }

    /*
     * parametrit tulevat muodosssa hetu_ryhma tai hetu_ryhma_vanharyhma
     */
    private static Osallistumistiedot pilkoParametrit(String parametrit) {
        try {
            String[] info = parametrit.split("_");
            String hetu = info[0];
            int ryhma = Integer.parseInt(info[1]);
            if (info.length == 3) {
                int uusi = Integer.parseInt(info[2]);
                return new Osallistumistiedot(hetu, ryhma, uusi);
            }
            return new Osallistumistiedot(hetu, ryhma);
        } catch (NumberFormatException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    private static boolean tarkistaParametrit(Kurssi kurssi, Osallistumistiedot tiedot,
            HttpServletRequest request) {

        HttpSession session = request.getSession();
        if (kurssi == null) {
            SessioApuri.annaVirhe(session, "Kurssia ei ole valittu");
            return false;
        }
        if (!Muotoilija.hetuTarkastus(tiedot.hetu)) {
            SessioApuri.annaVirhe(session, "Anna kelvollinen opiskelijanumero");
            return false;
        }
        if ((tiedot.ryhmänro > 99 && tiedot.ryhmänro < 1)
                || (tiedot.uusiRyhma != 0 && tiedot.uusiRyhma > 99 && tiedot.ryhmänro < 1)) {
            SessioApuri.annaVirhe(session, "Ryhmän valinnassa on vikaa");
            return false;
        }
        return true;
    }
}
