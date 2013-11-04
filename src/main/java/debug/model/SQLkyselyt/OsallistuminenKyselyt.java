/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import debug.dbconnection.DatabaseConnection;
import debug.model.Kurssi;
import debug.model.Opiskelija;
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

    public static List<Osallistuminen> osallistumisetKurssilla(Kurssi kurssi) throws SQLException {
        String query = "SELECT os.*, op.etunimi, op.sukunimi, op.sahkopostiosoite \n"
                + "FROM osallistuminen os, opiskelija op \n"
                + "WHERE os.hetu = op.hetu \n"
                + "AND os.kurssikoodi = ? \n"
                + "AND os.lukukausi = ? \n"
                + "AND os.lukuvuosi = ? \n"
                + "AND os.tyyppi = ? \n"
                + "AND os.kurssi_nro = ?";
        
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
    
    
    public static Osallistuminen osallistuminenKurssilla(Kurssi kurssi,
            String hetu) throws SQLException {
        String query = "SELECT os.* \n"
                + "FROM osallistuminen os \n"
                + "WHERE os.hetu = ? \n"
                + "AND os.kurssikoodi = ? \n"
                + "AND os.lukukausi = ? \n"
                + "AND os.lukuvuosi = ? \n"
                + "AND os.tyyppi = ? \n"
                + "AND os.kurssi_nro = ?";
        
        Connection conn = DatabaseConnection.makeConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, hetu);
        ps.setString(2, kurssi.getKurssikoodi());
        ps.setString(3, kurssi.getLukukausi());
        ps.setInt(4, kurssi.getLukuvuosi());
        ps.setString(5, kurssi.getTyyppi());
        ps.setInt(6, kurssi.getKurssi_nro());
        
        List<Osallistuminen> osallistumiset = SQLoader.loadTablesFromPreparedStatement(new Osallistuminen(), ps, conn);
        if(osallistumiset.isEmpty()) return null;
        else {
            osallistumiset.get(0).setKurssi(kurssi);
            return osallistumiset.get(0);
        }
    }
    
    
    public static void tallennaKantaan(Osallistuminen os) throws SQLException {
	    SQLoader.tallennaKantaan(os);
    }
}
