package debug;

import debug.model.Kurssi;
import debug.model.Opiskelija;
import debug.model.Henkilo;
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

    public static String kurssiTest() throws SQLException{
        String ret = "kurssiTest<br>";
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
            
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        }
        return ret;
    }
    
    public static String opiskelijaTest() throws SQLException{
        String ret = "opiskelijaTest<br>";
        
        try {
            
            ArrayList<Filter> filters = new ArrayList();
            Filter f1 = new Filter(Opiskelija.aloitusvuosi, 1991);
            Filter f2 = new Filter(Opiskelija.paa_aine, "TKT");
            filters.add(f1);
            filters.add(f2);
            
            List<Opiskelija> o = SQLoader.loadTable(new Opiskelija(), filters);
            for (Opiskelija opiskelijag : o) {
                ret += opiskelijag.get(Opiskelija.aloitusvuosi) + ", ";
                ret += opiskelijag.get(Opiskelija.paa_aine) + ", ";
                ret += opiskelijag.get(Opiskelija.hetu) + "<br>";
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        }
        return ret;
    }
    
    public static String henkiloTest() throws SQLException{
        String ret = "henkiloTest<br>";
        
        try {
            
            ArrayList<Filter> filters = new ArrayList();
            Filter f1 = new Filter(Henkilo.postinro, "00500");
            filters.add(f1);
            
            List<Henkilo> h = SQLoader.loadTable(new Henkilo(), filters);
            for (Henkilo hlo : h) {
                ret += hlo.get(Henkilo.hetu) + ", ";
                ret += hlo.get(Henkilo.postinro) + "<br>";
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        }
        return ret;
    }
    
}
