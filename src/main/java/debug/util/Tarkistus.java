/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.util;

import debug.SessioApuri;
import debug.model.Osallistuminen;
import static debug.util.Suoritukset.etsiOsallistuminenHetulla;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author tkairola
 */
public class Tarkistus {
    HttpServletRequest request;
    private static ArrayList<String> muutetutOsallistumiset = new ArrayList<String>();
    
    public static void muutosTarkistus(HttpServletRequest request, Valitsin valitsin, List<Osallistuminen> osallistumiset) {
        SessioApuri.annaViesti(request.getSession(), "Tarkistus toimii");
        String tyyppi = request.getParameter("type");
        String kerta = request.getParameter("kerta");

        Map<String, String[]> paramMap = request.getParameterMap();
        for (String paramName : paramMap.keySet()) {
            if (paramName.contains("pisteet")) {
                String hetu = paramName.substring(7);
                String arvo = paramMap.get(paramName)[0];

                Osallistuminen os = etsiOsallistuminenHetulla(hetu, osallistumiset);
                
                if (tyyppi.equals("laskarit")) {
                    if (os.getLaskarit().osa(kerta).getPisteet() != Integer.parseInt(arvo)) {
                        lisaaMuutettu(hetu);
                    }
                } else if (tyyppi.equals("ht")) {
                    if (os.getHarjoitustyot().osa(kerta).getPisteet() != Integer.parseInt(arvo)) {
                        lisaaMuutettu(hetu);
                    }
                } else if (tyyppi.equals("koe")) {
                    if (os.getKokeet().osa(kerta).getPisteet() != Integer.parseInt(arvo)) {
                        lisaaMuutettu(hetu);
                    }
                } else if (tyyppi.equals("arvosana")) {
                    if (!os.getArvosana().equals(arvo)) {
                        lisaaMuutettu(hetu);
                    }
                } else if (tyyppi.equals("op")) {
                    if (!os.laajuus_op.equals(arvo)) {
                        lisaaMuutettu(hetu);
                    }
                } else if (tyyppi.equals("kieli")) {
                    if (!os.getKielikoodi().equals(arvo)) {
                        lisaaMuutettu(hetu);
                    }
                } 
            }
        }
    }

    public static void lisaaMuutettu(String... muutetut) {
        for (String string : muutetut) {
            muutetutOsallistumiset.add(string);
        }
    }
    
    public void poistaMuutettu(String... poistetut) {
        for (String string : poistetut) {
            muutetutOsallistumiset.remove(string);
        }
    }
    
    public ArrayList<String> getMuutetut() {
        return muutetutOsallistumiset;
    }
            
            
}
