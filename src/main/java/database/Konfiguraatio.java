/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import view.util.Lokalisaatio;
import view.util.SessioApuri;

/**
 * Luokka konfiguraatioiden käsittelemiseen muistissa.
 */
public class Konfiguraatio {
    /**
     * Muuttujaan ladataan konfiguraatio.
     */
    private static Properties konfiguraatio = new Properties();
    
    /**
     * Metodi lataa WEB-INF kansiossa sijaitsevan kurki.cnf tiedoston avain-arvo parit muistiin.
     * 
     * @param request 
     */
    public static void lataaKonfiguraatio(HttpServletRequest request) {
        if (konfiguraatio.isEmpty()) {
            HttpSession istunto = request.getSession();
            Lokalisaatio bundle = Lokalisaatio.bundle(request);
            URL url = Thread.currentThread().getContextClassLoader().getResource("../kurki.cnf");
            File konfiguraatioTiedosto = new File(url.getPath());
            try {
                FileInputStream fis = new FileInputStream(konfiguraatioTiedosto);
                konfiguraatio.load(fis);
                konfiguraatio.setProperty("use_local_test_database", "false");
            } catch (IOException poikkeus) {
                SessioApuri.annaVirhe(istunto, bundle.getString("konfiguraatioTiedostonLatEO") + ": " + poikkeus.getLocalizedMessage());
            }
        }
    }
    
    /**
     * Metodi tarkastaa, onko parametrina annettu käyttäjä määritelty super-käyttäjäksi konfiguraatiossa.
     * 
     * @param kayttaja Tarkastettava käyttäjä
     * @return True, jos käyttäjä on super-käyttäjä.
     */
    public static boolean onkoSuperKayttaja(String kayttaja) {
        String[] superKayttajat = getProperty("superUsers").split(",");
        for (int i = 0; i < superKayttajat.length; i++) {
            if (kayttaja.equals(superKayttajat[i].trim())) {
                return true;
            }
        }
        return false;
    }
        
    public static String getProperty(String key) {
        return konfiguraatio.getProperty(key);
    }

    public static boolean testing() {
        return getProperty("use_local_test_database").equals("true");
    }
}
