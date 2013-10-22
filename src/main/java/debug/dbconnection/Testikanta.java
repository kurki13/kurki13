/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.dbconnection;

import debug.model.Kurssi;
import debug.model.SQLkyselyt.KurssiKyselyt;
import debug.model.column.Column;
import debug.model.util.Table;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mkctammi
 */
public class Testikanta {

    private static boolean initialized = false;
    //Lisää tähän oma projektikansiosi
    public static String[] project_folder_locations = {
        "/cs/fs2/home/mkctammi/ohtuprog/kurki13",
        "/cs/fs2/home/tkairola/KURKI13/kurki13",
        "/cs/fs2/home/esaaksvu/koulu/kurki13",
        "/cs/fs2/home/heikkiha/NetBeansProjects/kurki13",
        "/cs/fs2/home/topisark/Documents/kurki13",
        "C:/Users/ahathoor/Documents/GitHub/kurki13"
    };
    public static String project_folder_location;

    public static Connection makeConnection()
            throws SQLException, IOException, ClassNotFoundException {

        for (String string : project_folder_locations) {
            if (new File(string).exists() && new File(string).canWrite()) {
                project_folder_location = string;
                System.out.println("Testikannan poluksi valittiin " + string);
            }
        }

        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        Class.forName(driver);
        File dbFolder = new File(project_folder_location + "/testikanta/testikanta");
        String url = "jdbc:derby:" + dbFolder.getCanonicalPath() + ";create=true";
        Connection c = DriverManager.getConnection(url);
        if (!initialized) {
            initialize(c);
            initialized = true;
        }
        return c;
    }

    private static void initialize(Connection c) throws IOException, SQLException {
        runScript(c, project_folder_location + "/testikanta/taulut.sql");
        runScript(c, project_folder_location + "/testikanta/testidata.sql");
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        File f = new File(".").getCanonicalFile();
        System.out.println(f.getParent());
        Connection c = DatabaseConnection.makeConnection();
        Kurssi k = KurssiKyselyt.kurssiIDlla("582634.K.2010.K.1");
        System.out.println(taulunLisaysLause(k));
        List<Kurssi> ks = KurssiKyselyt.kurssitYllapitajalle();
        for (Kurssi kurssi : ks) {
            System.out.println(kurssi);
        }
        c.close();
    }

    private static void runScript(Connection c, String filename) throws IOException, SQLException {
        for (String string : scriptToStringArray(filename)) {
            try {
                c.prepareStatement(string).execute();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * Tällä metodilla voi tulostaa jonkun table olion, ja sitten sen lisätä
     * scriptiin jolla syötetään testidataa testikantaan.
     *
     * @param taulu
     */
    public static String taulunLisaysLause(Table taulu) {
        String str = "";
        str += "Insert into " + taulu.getTableName() + "\n";
        str += tulostaTaulunSarakkeet(taulu) + "\n";
        str += "values" + "\n";
        str += tulostaTaulunArvot(taulu) + ";\n";
        return str;
    }

    private static String tulostaTaulunSarakkeet(Table taulu) {
        String str = "";
        str += "(" + "\n";
        int i = 0;
        for (Column column : taulu.getColumns()) {
            str += column.getColumnName();

            if (i != taulu.getColumns().size() - 1) {
                str+= ",";
            }
            i++;
        }
        str += ")";
        return str;
    }

    private static String tulostaTaulunArvot(Table taulu) {
        String str = "";
        str += "(" + "\n";
        int i = 0;
        for (Column column : taulu.getColumns()) {
            Object value = taulu.getValue(column);
            if (value == null) {
                str += "null";
            } else if (value.getClass().isInstance("") || value.getClass() == Date.class) {
                str += "'" + value + "'";
            } else {
                str += value;
            }
            str += " -- " + column.getColumnName() + "\n";
            if (i != taulu.getColumns().size() - 1) {
                str += ",";
            }
            i++;
        }
        str += ")";
        return str;
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
