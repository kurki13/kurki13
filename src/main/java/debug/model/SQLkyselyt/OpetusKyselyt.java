/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import debug.dbconnection.DatabaseConnection;
import debug.model.Kurssi;
import debug.model.Opetus;
import debug.model.util.SQLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OpetusKyselyt {
    // Tätä ei välttämättä tarvita hh
    public static final String LATAA_RYHMAT = "SELECT * FROM opetus WHERE \n" 
            + "kurssikoodi = ? \n"
            + "AND lukukausi = ? \n"
            + "AND lukuvuosi = ? \n"
            + "AND tyyppi = ? \n"
            + "AND kurssi_nro = ? \n";

    public static List<Opetus> opetuksetKurssilla(Kurssi kurssi) throws SQLException {
        Connection databaseConnection = DatabaseConnection.makeConnection();
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(LATAA_RYHMAT);
        preparedStatement.setString(1, kurssi.getKurssikoodi());
        preparedStatement.setString(2, kurssi.getLukukausi());
        preparedStatement.setInt(3, kurssi.getLukuvuosi());
        preparedStatement.setString(4, kurssi.getTyyppi());
        preparedStatement.setInt(5, kurssi.getKurssi_nro());
        return SQLoader.loadTablesFromPreparedStatement(new Opetus(), preparedStatement, databaseConnection);
    }

}
