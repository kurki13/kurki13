/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.dbconnection;

import debug.Konfiguraatio;
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
    
    public static boolean testing = Konfiguraatio.testing();
    
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
                Class.forName(Konfiguraatio.getProperty("dbDriver"));
            } catch (ClassNotFoundException ex) {
                
            }
            return DriverManager.getConnection(Konfiguraatio.getProperty("dbServer"), Konfiguraatio.getProperty("dbUser"), Konfiguraatio.getProperty("dbPassword"));
        }
        return null;
    }
}
