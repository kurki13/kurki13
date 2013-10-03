package debug;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mkctammi
 */
public class Pipe {

    public static Connection makeConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Pipe.class.getName()).log(Level.SEVERE, null, ex);
        }
        return DriverManager.getConnection("jdbc:oracle:thin:@bodbacka.cs.helsinki.fi:1521:test", "tk_testi", "tapaus2");
    }

    public static String test() throws SQLException{
        String ret = "";
        try {
            for (Kurssi kurssi : lataaKurssitKannasta()) {
                ret += "Nimi: " + kurssi.nimi + "<br>";
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        }
        return ret;
    }

    private static ArrayList<Kurssi> lataaKurssitKannasta() throws SQLException{
        ArrayList<Kurssi> kaikki = new ArrayList();
        Connection conn = makeConnection();
        PreparedStatement ps = conn.prepareStatement("Select * from kurssi");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            kaikki.add(tulosRiviKurssiksi(rs));
        }
        conn.close();
        return kaikki;
    }

    private static ArrayList<Opiskelija> lataaOpiskelijatKannasta() throws SQLException{
        ArrayList<Opiskelija> kaikki = new ArrayList();
        Connection conn = makeConnection();
        PreparedStatement ps = conn.prepareStatement("Select * from opiskelija");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            kaikki.add(tulosRiviOpiskelijaksi(rs));
        }
        conn.close();
        return kaikki;
    }

    private static Kurssi tulosRiviKurssiksi(ResultSet rs) throws SQLException{
        String kurssikoodi = rs.getString("kurssikoodi");
        String lukukausi = rs.getString("lukukausi");
        int lukuvuosi = rs.getInt("lukuvuosi");
        String tyyppi = rs.getString("tyyppi");
        int kurssi_nro = rs.getInt("kurssi_nro");
        String nimi = rs.getString("nimi");
        return new Kurssi(kurssikoodi, lukukausi, lukuvuosi, tyyppi, kurssi_nro, nimi);
    }

    private static Opiskelija tulosRiviOpiskelijaksi(ResultSet rs) throws SQLException {
        String hetu = rs.getString("hetu"); // ID
        String personid = rs.getString("personid");
        String etunimi = rs.getString("etunimi");
        String sukunimi = rs.getString("sukunimi");
        String entinen_sukunimi = rs.getString("entinen_sukunimi");
        String osoite = rs.getString("osoite");
        String puhelin = rs.getString("puhelin");
        String sahkopostiosoite = rs.getString("sahkopostiosoite");
        String paa_aine = rs.getString("paa_aine");
        int aloitusvuosi = rs.getInt("aloitusvuosi");
        Timestamp kaytto_pvm = rs.getTimestamp("kaytto_pvm");
        String opnro = rs.getString("opnro");
        String lupa = rs.getString("lupa");
        String vinkki = rs.getString("vinkki");
        String varmenne = rs.getString("varmenne");
        return new Opiskelija(hetu, personid, etunimi, sukunimi, entinen_sukunimi, osoite, puhelin, sahkopostiosoite, paa_aine, aloitusvuosi, kaytto_pvm, opnro, lupa, vinkki, varmenne);
    }

    public static ArrayList<Osallistuminen> osallistumisetKurssilla(Kurssi kurssi) throws SQLException {
        ArrayList<Osallistuminen> osallistumiset = new ArrayList();
        Connection conn = Pipe.makeConnection();
        PreparedStatement ps = conn.prepareStatement("Select * from osallistuminen where "
                + "kurssikoodi = ? AND "
                + "lukukausi = ? AND "
                + "lukuvuosi = ? AND "
                + "tyyppi = ?");
        ps.setString(1, kurssi.kurssikoodi);
        ps.setString(2, kurssi.lukukausi);
        ps.setInt(3, kurssi.lukuvuosi);
        ps.setString(4, kurssi.tyyppi);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            osallistumiset.add(tulosRiviOsallistumiseksi(rs));
        }
        conn.close();
        return osallistumiset;
    }

    private static Osallistuminen tulosRiviOsallistumiseksi(ResultSet rs) throws SQLException{
        String hetu = rs.getString("hetu");
        int ilmo_jnro = rs.getInt("ilmo_jnro");
        Date ilmoittautumis_pvm = rs.getDate("ilmoittautumis_pvm");
        int kurssi_nro = rs.getInt("kurssi_nro");
        int ryhma_nro = rs.getInt("ryhma_nro");
        return new Osallistuminen(hetu, ilmo_jnro, ilmoittautumis_pvm, kurssi_nro, ryhma_nro);
    }

    public static Opiskelija opiskelijaHetulla(String hetu) throws SQLException {
        Connection conn = Pipe.makeConnection();
        Opiskelija opiskelija = null;
        PreparedStatement ps = conn.prepareStatement("Select * from opiskelija where hetu = ?");
        ps.setString(1, hetu);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            opiskelija = tulosRiviOpiskelijaksi(rs);
        } else {
            throw new IllegalArgumentException("Hetulla " + hetu + "ei löytynyt yhtään opiskelijaa");
        }
        conn.close();
        return opiskelija;
    }
}
