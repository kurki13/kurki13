/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.util;

import debug.SessioApuri;
import debug.model.Osallistuminen;
import debug.model.osasuoritukset.Osasuoritus;
import debug.model.util.SQLoader;
import static debug.util.Tarkistus.lisaaMuutettu;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author tkairola
 */
public class Suoritukset {
    
    private static ArrayList<String> muutetutOsallistumiset = new ArrayList<String>();

    public static void muutosTarkistus(HttpServletRequest request, Valitsin valitsin, List<Osallistuminen> osallistumiset) {
        muutetutOsallistumiset.clear();
        SessioApuri.annaViesti(request.getSession(), "Tarkistus toimii");
        String tyyppi = request.getParameter("type");
        String kerta = request.getParameter("kerta");

        Map<String, String[]> paramMap = request.getParameterMap();
        for (String paramName : paramMap.keySet()) {
            if (paramName.contains("pisteet")) {
                String hetu = paramName.substring(7);
                String arvo = paramMap.get(paramName)[0];

                Osallistuminen os = etsiOsallistuminenHetulla(hetu, osallistumiset);

                if(isInteger(arvo)) {
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
                            lisaaMuutettu(hetu+"arvosana"+arvo);
                        }
                    } else if (tyyppi.equals("op")) {
                        if (os.getLaajuus_op() != Integer.parseInt(arvo)) {
                            lisaaMuutettu(hetu);
                        }
                    }     
                } else {
                    if (tyyppi.equals("laskarit")) {
                        if (arvo.isEmpty() && (os.getLaskarit().osa(kerta).getPisteet() != -1)) { //muutetaan numerosta tyhjään
                            lisaaMuutettu(hetu);
                        } else if ("+".equals(arvo) && (os.getLaskarit().osa(kerta).getPisteet() != -2)) {  //muutetaan numerosta plussaan // muista myös "-" !!
                            lisaaMuutettu(hetu);
                        }
                    } else if (tyyppi.equals("ht")) {
                        if (arvo.isEmpty() && (os.getHarjoitustyot().osa(kerta).getPisteet() != -1)) { //muutetaan numerosta tyhjään
                            lisaaMuutettu(hetu);
                        } else if ("+".equals(arvo) && (os.getHarjoitustyot().osa(kerta).getPisteet() != -2)) {  //muutetaan numerosta plussaan // muista myös "-" !!
                            lisaaMuutettu(hetu);
                        }
                    } else if (tyyppi.equals("koe")) {
                        if (arvo.isEmpty() && (os.getKokeet().osa(kerta).getPisteet() != -1)) { //muutetaan numerosta tyhjään
                            lisaaMuutettu(hetu);
                        } else if ("+".equals(arvo) && (os.getKokeet().osa(kerta).getPisteet() != -2)) {  //muutetaan numerosta plussaan // muista myös "-" !!
                            lisaaMuutettu(hetu);
                        }
                    } 
//                    else if (tyyppi.equals("arvosana")) {
//                        if (!os.getArvosana().equals(arvo)) {
//                            lisaaMuutettu(hetu+"arvosana"+arvo);
//                        }
//                    } else if (tyyppi.equals("kieli")) {
//                        if (!os.getKielikoodi().equals(arvo)) {
//                            lisaaMuutettu(hetu+"kieli"+arvo);
//                        }
//                    } 
                }
                
                
            }
        }
    }
    
    public static boolean isInteger(String s) {
    try { 
        Integer.parseInt(s); 
    } catch(NumberFormatException e) { 
        return false; 
    }
    return true;
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
    
    //tarkista hetu osallistumisista ja arvo valitsimella
    public static void kasitteleLomake(HttpServletRequest request, Valitsin valitsin, List<Osallistuminen> osallistumiset) {
        SessioApuri.annaViesti(request.getSession(), "Lomakkeen käsittely kutsuttu");
        String tyyppi = request.getParameter("type");
        String kerta = request.getParameter("kerta");

        Map<String, String[]> paramMap = request.getParameterMap();
        for (String paramName : paramMap.keySet()) {
            if (paramName.contains("pisteet")) {
                String hetu = paramName.substring(7);
                String arvo = paramMap.get(paramName)[0];

                Osallistuminen os = etsiOsallistuminenHetulla(hetu, osallistumiset);

                if (valitsin.loytyykoArvo(arvo)) {

                    if (os != null) {
                        if (tyyppi.equals("laskarit")) {
                            if (tarkistaKerta(kerta, os.getLaskarit().getLukumaara())) {
                                int k = Integer.parseInt(kerta);
                                pisteetOsasuoritukselle(os.getLaskarit().osa(k), arvo);
                            }
                        } else if (tyyppi.equals("ht")) {
                            if (tarkistaKerta(kerta, os.getHarjoitustyot().getLukumaara())) {
                                int k = Integer.parseInt(kerta);
                                pisteetOsasuoritukselle(os.getHarjoitustyot().osa(k), arvo);
                            }
                        } else if (tyyppi.equals("koe")) {
                            if (tarkistaKerta(kerta, os.getKokeet().getLukumaara())) {
                                int k = Integer.parseInt(kerta);
                                pisteetOsasuoritukselle(os.getKokeet().osa(k), arvo);
                            }
                        } else if (tyyppi.equals("arvosana")) {
                            os.setArvosana(arvo);
                        } else if (tyyppi.equals("op")) {
                            if (arvo.equals("")) {
                                os.setValue(Osallistuminen.laajuus_op, 0);
                            } else {
                                os.setValue(Osallistuminen.laajuus_op, Integer.parseInt(arvo));
                            }
                        } else if (tyyppi.equals("kieli")) {
                            os.setValue(Osallistuminen.kielikoodi, arvo);
                        } else {
                            SessioApuri.annaVirhe(request.getSession(), "Väärä toiminto " + tyyppi);
                            return;
                        }
                        
                        //tarkista muuttuuko arvo ja vasta sitten tallenna kantaan
                        
                        try {
                            SQLoader.tallennaKantaan(os);
                            SessioApuri.annaViesti(request.getSession(), "Osasuoritus tallennettu kantaan " + hetu + "  -  " + arvo);
                        } catch (SQLException ex) {
                            SessioApuri.annaVirhe(request.getSession(), "Osasuorituksen kantaan tallennuksessa tapahtui virhe: " + ex.getMessage());
                        }
                        
                        
                    } else {
                        SessioApuri.annaVirhe(request.getSession(), "hetu " + hetu + " ei ole kurssilla");
                    }
                } else {
                    SessioApuri.annaVirhe(request.getSession(), "virheellinen arvo " + arvo + " annettu hetulle " + hetu);
                }
            }
        }
    }

    public static Osallistuminen etsiOsallistuminenHetulla(String hetu, List<Osallistuminen> osallistumiset) {
        for (Osallistuminen os : osallistumiset) {
            if (os.getHetu().equals(hetu)) {
                return os;
            }
        }
        return null;
    }

    private static boolean tarkistaKerta(String k, int lukumaara) {
        try {
            int kerta = Integer.parseInt(k);
            if (kerta < 0 || kerta > lukumaara) {
                throw new NumberFormatException();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //lisää käsittely myös "-" !!
    private static void pisteetOsasuoritukselle(Osasuoritus osas, String pisteet) {
        if (pisteet.equals("+")) {
            osas.opiskelijaLasna();
        }
        else if (pisteet.equals("")) {
            osas.poistaPisteet();
        } else {
            osas.setPisteet(Integer.parseInt(pisteet));
        }
    }
}