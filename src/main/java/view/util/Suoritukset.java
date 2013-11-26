/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.util;

import model.Osallistuminen;
import model.osasuoritukset.Osasuoritus;
import model.util.SQLoader;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author tkairola
 */
public class Suoritukset {

    private static Map<Osallistuminen, String> muutetutArvot = new HashMap<Osallistuminen, String>();

    public static void muutosTarkistus(HttpServletRequest request, Valitsin valitsin, List<Osallistuminen> osallistumiset) {
        if (muutetutArvot != null) {
            muutetutArvot.clear();
        }
//        SessioApuri.annaViesti(request.getSession(), "Tarkistus toimii");
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

                        if (isInteger(arvo)) {
                            if (tyyppi.equals("laskarit")) {
                                if (os.getLaskarit().osa(kerta).getPisteet() != Integer.parseInt(arvo)) {
                                    lisaaMuutettu(os, arvo);
                                }
                            } else if (tyyppi.equals("ht")) {
                                if (os.getHarjoitustyot().osa(kerta).getPisteet() != Integer.parseInt(arvo)) {
                                    lisaaMuutettu(os, arvo);
                                }
                            } else if (tyyppi.equals("koe")) {
                                if (os.getKokeet().osa(kerta).getPisteet() != Integer.parseInt(arvo)) {
                                    lisaaMuutettu(os, arvo);
                                }
                            } else if (tyyppi.equals("arvosana")) {
                                //kun asetetaan jostakin("",-,+,numero) toiseksi numeroksi
                                if (os.getArvosana() != null && arvo != null) {
                                    if (!os.getArvosana().equals(arvo)) {
                                        lisaaMuutettu(os, arvo);
                                    }
                                    //astetaan tyhjästä numeroksi
                                } else if (os.getArvosana() == null && arvo != null) {
                                    lisaaMuutettu(os, arvo);
                                }
                            } else if (tyyppi.equals("op")) {
                                if (os.getLaajuus_op() != Integer.parseInt(arvo)) {
                                    lisaaMuutettu(os, arvo);
                                }
                            } else {
                                SessioApuri.annaVirhe(request.getSession(), "Väärä toiminto " + tyyppi);
                                return;
                            }
                        } else {
                            if (tyyppi.equals("laskarit")) {
                                //muutetaan numerosta tyhjään
                                if (arvo.isEmpty() && (os.getLaskarit().osa(kerta).getPisteet() != -1)) {
                                    lisaaMuutettu(os, arvo);
                                //muutetaan numerosta plussaan // muista myös "-" !!
                                } else if ("+".equals(arvo) && (os.getLaskarit().osa(kerta).getPisteet() != -2)) {
                                    lisaaMuutettu(os, arvo);
                                }
                            } else if (tyyppi.equals("ht")) {
                                //muutetaan numerosta tyhjään
                                if (arvo.isEmpty() && (os.getHarjoitustyot().osa(kerta).getPisteet() != -1)) {
                                    lisaaMuutettu(os, arvo);
                                    //muutetaan numerosta plussaan // muista myös "-" !!
                                } else if ("+".equals(arvo) && (os.getHarjoitustyot().osa(kerta).getPisteet() != -2)) { 
                                    lisaaMuutettu(os, arvo);
                                }
                            } else if (tyyppi.equals("koe")) {
                                //muutetaan numerosta tyhjään
                                if (arvo.isEmpty() && (os.getKokeet().osa(kerta).getPisteet() != -1)) {
                                    lisaaMuutettu(os, arvo);
                                    //muutetaan numerosta plussaan // muista myös "-" !!
                                } else if ("+".equals(arvo) && (os.getKokeet().osa(kerta).getPisteet() != -2)) { 
                                    lisaaMuutettu(os, arvo);
                                }
                            } else if (tyyppi.equals("arvosana")) {
                                //kun asetetaan kirjaimesta toiseksi kirjaimeksi
                                if (os.getArvosana() != null && !arvo.isEmpty()) {
                                    if (!os.getArvosana().equals(arvo)) {
                                        lisaaMuutettu(os, arvo);
                                    }
                                    //kun asetetaan kirjaimesta tyhjäksi
                                } else if (os.getArvosana() != null && arvo.isEmpty()) {
                                    lisaaMuutettu(os, arvo);
                                    //kun astetaan tyhjästä kirjaimeksi
                                } else if (os.getArvosana() == null && !arvo.isEmpty()) {
                                    lisaaMuutettu(os, arvo);
                                }
                            } else if (tyyppi.equals("kieli")) {
                                //kielestä toiseksi kieleksi
                                if (os.getKielikoodi() != null && !arvo.isEmpty()) {
                                    if (!os.getKielikoodi().equals(arvo)) {
                                        lisaaMuutettu(os, arvo);
                                    }
                                    //kielestä tyhjäksi
                                } else if (os.getKielikoodi() != null && arvo.isEmpty()) {
                                    lisaaMuutettu(os, arvo);
                                    //tyhjästä kieleksi
                                } else if (os.getKielikoodi() == null && !arvo.isEmpty()) {
                                    lisaaMuutettu(os, arvo);
                                }
                            } else {
                                SessioApuri.annaVirhe(request.getSession(), "Väärä toiminto " + tyyppi);
                                return;
                            }
                        }
                    } else {
                        SessioApuri.annaVirhe(request.getSession(), "hetu " + hetu + " ei ole kurssilla");
                    }
                } else {
                    SessioApuri.annaVirhe(request.getSession(), "virheellinen arvo " + arvo + " annettu hetulle " + hetu);
                }
            }
        }
        //kutsutaan lopuksi lomakkeen käsittelyä kantaan tallennusta varten
        if (muutetutArvot != null) {
            kasitteleLomake(request, valitsin, muutetutArvot, tyyppi, kerta);
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static void lisaaMuutettu(Osallistuminen os, String arvo) {
        muutetutArvot.put(os, arvo);
    }

    public Map<Osallistuminen, String> getMuutetut() {
        return muutetutArvot;
    }

    public static void kasitteleLomake(HttpServletRequest request, Valitsin valitsin, Map<Osallistuminen, String> muutetut, String tyyppi, String kerta) {
//        SessioApuri.annaViesti(request.getSession(), "Lomakkeen käsittely kutsuttu");
        if (muutetut != null) {
            for (Map.Entry<Osallistuminen, String> map : muutetut.entrySet()) {
                String arvo = map.getValue();
                Osallistuminen os = map.getKey();

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
                            os.setKommentti2("MANUAL");
                            //tenttija-kentän max koko on 20 joten siihen ei mahdu tallentamaan jokaista muutoksen tehnyttä tenttijää
                            //os.setTenttija(os.getTenttija()+request.getRemoteUser());
                            os.setTenttija(request.getRemoteUser());
                            os.setKypsyyspvm(getTimestamp());
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
                            
                        //tallennetaan kantaan
                        try {
                            SQLoader.tallennaKantaan(os);
                            SessioApuri.annaViesti(request.getSession(), "Osasuoritus tallennettu kantaan " + os.getHetu() + "  -  " + arvo);
                        } catch (SQLException ex) {
                            SessioApuri.annaVirhe(request.getSession(), "Osasuorituksen kantaan tallennuksessa tapahtui virhe: " + ex.getMessage());
                        }

                    } else {
                        SessioApuri.annaVirhe(request.getSession(), "hetu " + os.getHetu() + " ei ole kurssilla");
                    }
                } else {
                    SessioApuri.annaVirhe(request.getSession(), "virheellinen arvo " + arvo + " annettu hetulle " + os.getHetu());
                }
            }
        }
    }

    private static Timestamp getTimestamp() {
	 java.util.Date date= new java.util.Date();
	 Timestamp time = new Timestamp(date.getTime());
         
         return time;
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
        } else if (pisteet.equals("")) {
            osas.poistaPisteet();
        } else {
            osas.setPisteet(Integer.parseInt(pisteet));
        }
    }
 
}
