/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug;

import java.util.Properties;

/**
 *
 * @author mkctammi
 */
public class Konfiguraatio {

    private static Properties prop = new Properties();

    static {
        prop.setProperty("use_local_test_database", "true");
        prop.setProperty("webmaster", "taru.airola@helsinki.fi");
        prop.setProperty("oodi", "taru.airola@helsinki.fi");
        prop.setProperty("dbDriver", "oracle.jdbc.OracleDriver");
        prop.setProperty("dbServer", "jdbc:oracle:thin:@bodbacka.cs.helsinki.fi:1521:test");
        prop.setProperty("dbUser", "tk_testi");
        prop.setProperty("dbPassword", "tapaus2");
        prop.setProperty("loginManagers", "laine, siven, mkctammi, admin");
        prop.setProperty("superUsers", "siven, laine, mkctammi, admin");
    }
    
    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
    
    public static boolean testing() {
        return getProperty("use_local_test_database").equals("true");
    }
}
