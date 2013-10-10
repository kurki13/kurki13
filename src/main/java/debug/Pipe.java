package debug;

import debug.model.Kurssi;
import debug.model.Opiskelija;
import debug.model.Henkilo;
import debug.model.Opetus;
import debug.model.Osallistuminen;
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
                ret += kurssig.getValue(Kurssi.nimi) + "<br>";
                ret += kurssig.getValue(Kurssi.lukuvuosi) + "<br>";
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
                ret += opiskelijag.getValue(Opiskelija.aloitusvuosi) + ", ";
                ret += opiskelijag.getValue(Opiskelija.paa_aine) + ", ";
                ret += opiskelijag.getValue(Opiskelija.hetu) + "<br>";
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
            for (Henkilo hlog : h) {
                ret += hlog.getHetu() + ", ";
                ret += hlog.getPostinro() + "<br>";
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        }
        return ret;
    }
    
    public static String opetusTest() throws SQLException{
        String ret = "opetusTest<br>";
        
        try {
            
            ArrayList<Filter> filters = new ArrayList();
            Filter f1 = new Filter(Opetus.lukuvuosi, 2001);
            Filter f2 = new Filter(Opetus.lukukausi, "K");
            Filter f3 = new Filter(Opetus.ryhma_nro, 2);
            filters.add(f1);
            filters.add(f2);
            filters.add(f3);
            
            List<Opetus> h = SQLoader.loadTable(new Opetus(), filters);
            for (Opetus opeg : h) {
                ret += opeg.getValue(Opetus.lukuvuosi) + ", ";
                ret += opeg.getValue(Opetus.lukukausi) + ", ";
                ret += opeg.getValue(Opetus.ryhma_nro) + "<br>";
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
