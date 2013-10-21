/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import debug.dbconnection.DatabaseConnection;
import debug.model.Kurssi;
import debug.model.Opiskelija;
import debug.model.util.Filter;
import debug.model.util.SQLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author mkctammi
 */
public class OpiskelijaKyselyt {
    

    public static Opiskelija opiskelijaHetulla(String hetu) throws SQLException {
        Filter f = new Filter(Opiskelija.hetu, hetu);
        return SQLoader.loadTable(new Opiskelija(), f).get(0);
    }
    
    public static List<Opiskelija> kurssinOpiskelijat(Kurssi kurssi) throws SQLException {
        String query = "SELECT op.* FROM osallistuminen os, opiskelija op \n"
                + "WHERE op.hetu = os.hetu \n"
                + "AND os.kurssikoodi = ? \n"
                + "AND os.lukukausi = ? \n"
                + "AND os.lukuvuosi = ? \n"
                + "AND os.tyyppi = ? \n"
                + "AND os.kurssi_nro = ?";
        Connection connection = DatabaseConnection.makeConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, kurssi.getKurssikoodi());
        ps.setString(2, kurssi.getLukukausi());
        ps.setInt(3, kurssi.getLukuvuosi());
        ps.setString(4, kurssi.getTyyppi());
        ps.setInt(5, kurssi.getKurssi_nro());
        return SQLoader.loadTablesFromPreparedStatement(new Opiskelija(), ps, connection);
    }
}
