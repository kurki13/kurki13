/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.util;

import static view.util.SessioApuri.annaVirhe;
import model.Kurssi;
import model.Osallistuminen;
import static model.SQLkyselyt.OsallistuminenKyselyt.osallistuminenKurssilla;
import model.SQLkyselyt.SQLProseduurit;
import model.util.Muotoilija;
import model.util.SQLoader;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author esaaksvu
 */
public class OsallistujaMuutokset {

    private static class Osallistumistiedot {

        String hetu;
        int ryhmänro;
        int uusiRyhma;

        public Osallistumistiedot(String hetu, int ryhma) {
            this.hetu = hetu;
            this.ryhmänro = ryhma;
        }

        public Osallistumistiedot(String hetu, int ryhma, int uusiRyhma) {
            this.hetu = hetu;
            this.ryhmänro = ryhma;
            this.uusiRyhma = uusiRyhma;
        }
    }

 
    public void suoritusTiedotUpdate(Osallistuminen os, String kieli, String arv,
            String op, HttpServletRequest rqst) {

        HttpSession session = rqst.getSession();
        if (kieli != null && !kieli.equals("")) {
            if (kieli.equals("S") || kieli.equals("E") || kieli.equals("R")) {
                os.setKielikoodi(kieli);
            } else {
                annaVirhe(session, Lokalisaatio.bundle(rqst).getString("kielikoodiVirheellinen"));
            }
        }
        if (op != null && !op.equals("")) {
            try {
                int opInt = Integer.parseInt(op);
                if (opInt < 100) {
                    os.setLaajuus_op(opInt);
                }
            } catch (NumberFormatException e) {
                annaVirhe(session, Lokalisaatio.bundle(rqst).getString("opVirheellinen"));
            }
        }
        if (arv != null) {
            try {
                int arvInt = Integer.parseInt(arv);
                if (arvInt < 6 && arvInt >= 0) {
                    os.setArvosana(arv);
                } else {
                    annaVirhe(session, Lokalisaatio.bundle(rqst).getString("arvosanaVirheellinen"));
                }
            } catch (NumberFormatException e) {
                if (arv.equals("+") || arv.equals("-") || arv.equals("")) {
                    os.setArvosana(arv);
                } else {
                    annaVirhe(session, Lokalisaatio.bundle(rqst).getString("arvosanaVirheellinen"));
                }
            }
        }
    }

    public static String seuraavaOsallistuminen(List<Osallistuminen> l, String hetu) {
        try {
            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getHetu().equals(hetu)) {
                    return l.get(i + 1).getHetu();
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }
        return hetu;
    }

    public static String edellinenOsallistuminen(List<Osallistuminen> l, String hetu) {
        try {
            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getHetu().equals(hetu)) {
                    return l.get(i - 1).getHetu();
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }
        return hetu;
    }

    /*
     * parametrit tulevat muodosssa hetu_ilmoryhma tai hetu_ilmoryhma_ryhmanro
     */
    private static Osallistumistiedot pilkoParametrit(String parametrit) {
        try {
            String[] info = parametrit.split("_");
            String hetu = info[0];
            int ryhma = Integer.parseInt(info[1]);
            if (info.length == 3) {
                int uusi = Integer.parseInt(info[2]);
                return new Osallistumistiedot(hetu, ryhma, uusi);
            }
            return new Osallistumistiedot(hetu, ryhma);
        } catch (NumberFormatException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    private static boolean tarkistaParametrit(Kurssi kurssi, Osallistumistiedot tiedot,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        Lokalisaatio bundle = Lokalisaatio.bundle(request);
        if (kurssi == null) {
            SessioApuri.annaVirhe(session, bundle.getString("kurssiaEiValittu"));
            return false;
        }
        if (!Muotoilija.hetuTarkastus(tiedot.hetu)) {
            SessioApuri.annaVirhe(session, bundle.getString("kelvotonOpNro"));
            return false;
        }
        if ((tiedot.ryhmänro > 99 && tiedot.ryhmänro < 1)
                || (tiedot.uusiRyhma != 0 && tiedot.uusiRyhma > 99 && tiedot.ryhmänro < 1)) {
            SessioApuri.annaVirhe(session, bundle.getString("ryhmaViallinen"));
            return false;
        }
        return true;
    }

    public static void luoUusiOsallistuminen(String parametrit,
            Kurssi kurssi, HttpServletRequest request) {
        Osallistumistiedot tiedot = pilkoParametrit(parametrit);
        if (tarkistaParametrit(kurssi, tiedot, request)) {
            SQLProseduurit.lisaaOpiskelija(kurssi, tiedot.ryhmänro, tiedot.hetu, request);
        }
    }

    public static void palautaKurssille(String parametrit,
            Kurssi kurssi, HttpServletRequest request) {
        Osallistumistiedot tiedot = pilkoParametrit(parametrit);
        if (tarkistaParametrit(kurssi, tiedot, request)) {
            SQLProseduurit.palautaOpiskelija(kurssi, tiedot.ryhmänro, tiedot.hetu, request);
        }
    }

    public static void vaihdaRyhmaa(String parametrit,
            Kurssi kurssi, HttpServletRequest request) {
        if (parametrit.isEmpty()) {
            return;
        }
        Osallistumistiedot tiedot = pilkoParametrit(parametrit);
        if (tarkistaParametrit(kurssi, tiedot, request) && tiedot.uusiRyhma != 0) {
            SQLProseduurit.vaihdaOpiskelijanRyhmaa(tiedot.uusiRyhma, kurssi,
                    tiedot.ryhmänro, tiedot.hetu, request);
        } else {
            Lokalisaatio bundle = Lokalisaatio.bundle(request);
            SessioApuri.annaVirhe(request.getSession(), bundle.getString("ryhmaNolla"));
        }
    }

    public static void poistaKurssilta(String parametrit,
            Kurssi kurssi, HttpServletRequest request) {
        Osallistumistiedot tiedot = pilkoParametrit(parametrit);
        if (tarkistaParametrit(kurssi, tiedot, request)) {
            SQLProseduurit.poistaOpiskelija(kurssi, tiedot.ryhmänro, tiedot.hetu, request);
        }
    }

    public static void sulataOpiskelija(Kurssi kurssi, String hetu,
            HttpServletRequest request) {
        Lokalisaatio bundle = Lokalisaatio.bundle(request);
        try {
            Osallistuminen os = osallistuminenKurssilla(kurssi, hetu);
            if (os.getJaassa().equals("S")) {
                SessioApuri.annaVirhe(request.getSession(), bundle.getString("opiskelija") + hetu + bundle.getString("onJoSula"));
            }
            os.setJaassa("S");
            SQLoader.tallennaKantaan(os);
            SessioApuri.annaViesti(request.getSession(), bundle.getString("sulatusOnnistuiHetulla") + hetu);
        } catch (Exception e) {
            SessioApuri.annaVirhe(request.getSession(), bundle.getString("sulatusEpäonnistuiHetulla") + hetu);
        }
    }

    public static String nimiFormaatti(String nimi) {
        return nimi.substring(0, 1).toUpperCase().concat(nimi.substring(1, nimi.length()).toLowerCase());
    }
}
