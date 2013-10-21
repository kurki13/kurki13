/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 *
 * @author mkctammi
 */
public class Testikanta {

    public static Connection makeConnection()
            throws SQLException, ClassNotFoundException, IOException {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        Class.forName(driver);
        File dbFolder = new File(new File("./testikanta/testikanta").getAbsolutePath());
        String url = "jdbc:derby:" + dbFolder.getCanonicalPath() + ";create=true";
        System.out.println(url);
        Connection c = DriverManager.getConnection(url);
        return c;
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        Connection c = makeConnection();

        //taulujen luonti
        try {
            for (String string : scriptToStringArray("./testikanta/taulut.SQL")) {
                c.prepareStatement(string).execute();
            }
        } catch (SQLException ex) {
            //taulut oli jo luotu
        }
        c.close();
    }

    private static String[] scriptToStringArray(String filename) throws IOException {
        String[] strings = scriptToString(new File(filename)).split(";");
        if (strings[strings.length - 1].trim().equals("")) {
            strings = Arrays.copyOfRange(strings, 0, strings.length - 2);
        }
        return strings;
    }

    private static String scriptToString(File file) throws IOException {
        String ret = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line + "\n ");
            }
            in.close();
            ret = sb.toString();
        } catch (Exception e) {
            System.err.println("Failed to Execute" + file + ". The error is" + e.getMessage());
        }
        return ret;
    }
}
