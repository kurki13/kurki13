/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mkctammi
 */
public class DatabaseConnection {

    public static Connection makeConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Pipe.class.getName()).log(Level.SEVERE, null, ex);
        }
        return DriverManager.getConnection("jdbc:oracle:thin:@bodbacka.cs.helsinki.fi:1521:test", "tk_testi", "tapaus2");
    }

    public static Connection createTestDatabaseConnection()
            throws SQLException, ClassNotFoundException, IOException {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        Class.forName(driver);
        File dbFolder = new File(new File("./testDB").getAbsolutePath());
        String url = "jdbc:derby:" + dbFolder.getCanonicalPath();
        Connection c = DriverManager.getConnection(url);
        return c;
    }

    public static void main(String[] args) throws IOException {
            try {
                Connection c = createTestDatabaseConnection();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}
