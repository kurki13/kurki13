package debug;

import debug.model.Kurssi;
import debug.model.Opiskelija;
import debug.model.util.Filter;
import debug.model.util.SQLoader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mkctammi
 */
public class Pipe {

    public static Connection makeConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Pipe.class.getName()).log(Level.SEVERE, null, ex);
        }
        return DriverManager.getConnection("jdbc:oracle:thin:@bodbacka.cs.helsinki.fi:1521:test", "tk_testi", "tapaus2");
    }

    public static String test() throws SQLException{
        String ret = "";
        try {
            ArrayList<Filter> filters = new ArrayList();
            Filter f1 = new Filter(Kurssi.nimi, "Java-ohjelmointi");
            Filter f2 = new Filter(Kurssi.lukuvuosi, 2000);
            filters.add(f1);
            filters.add(f2);
            
            List<Kurssi> k = SQLoader.loadTable(new Kurssi(), filters);
            for (Kurssi kurssig : k) {
                ret += kurssig.get(Kurssi.nimi) + "<br>";
                ret += kurssig.get(Kurssi.lukuvuosi) + "<br>";
            }
            List<Opiskelija> o = SQLoader.loadTable(new Opiskelija());
            for (Opiskelija opiskelijag : o) {
                ret += opiskelijag.get(Opiskelija.hetu) + "<br>";
            }
//            List<Osallistuminen> p = SQLoader.loadTable(new Osallistuminen());
//            for (Osallistuminen osallistuminen : p) {
//                ret += osallistuminen.get(Osallistuminen.Sarake.hetu, String.class) + "<br>";
//            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        }
        return ret;
    }
    
}
