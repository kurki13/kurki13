/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.dbconnection;

import debug.Konfiguraatio;
import debug.Pipe;
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

    public static boolean testing = false;

    public static Connection makeConnection() throws SQLException {
        if (testing) {
            try {
                return Testikanta.makeConnection();
            } catch (IOException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                Class.forName(Konfiguraatio.getString("dbDriver"));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Pipe.class.getName()).log(Level.SEVERE, null, ex);
            }
            return DriverManager.getConnection(Konfiguraatio.getString("dbServer"), Konfiguraatio.getString("dbUser"), Konfiguraatio.getString("dbPassword"));
        }
        return null;
    }
}
