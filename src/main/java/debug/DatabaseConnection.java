/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        File dbFolder = new File(new File("./testikanta").getAbsolutePath());
        String url = "jdbc:derby:" + dbFolder.getCanonicalPath() + ";create=true";
        System.out.println(url);
        Connection c = DriverManager.getConnection(url);
        return c;
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        Connection c = createTestDatabaseConnection();
        String query = "CREATE TABLE Testi\n"
                + "(\n"
                + "Numero int,\n"
                + "Teksti varchar(255)\n"
                + ")";
        c.prepareStatement(query).execute();
        
        
        query = "INSERT INTO Testi (Numero,Teksti)\n"
                + "VALUES (123, 'Iik')\n";
        c.prepareStatement(query).execute();
        
        query = "select * from testi";
        PreparedStatement ps = c.prepareStatement(query);
        ps.execute();
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            System.out.println(rs.getInt("Numero"));
            System.out.println(rs.getString("Teksti"));
        }
        c.close();
    }
}
