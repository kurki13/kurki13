/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import debug.DatabaseConnection;
import debug.model.Kurssi;
import debug.model.Osallistuminen;
import debug.model.util.Filter;
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
        String query = "SELECT os.*, op.etunimi, op.sukunimi \n"
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
        }
        
        for (Osallistuminen osallistuminen : osallistumiset) {
            osallistuminen.setKurssi(kurssi);
        }
        
        conn.close();
        return osallistumiset;
    }
}
