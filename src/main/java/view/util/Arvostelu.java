/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.util;

import model.Kurssi;
import model.osasuoritukset.Muotoilija;
import model.util.SQLoader;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mkctammi
 */
public class Arvostelu {

    public static void kasitteleLomake(Kurssi kurssi, HttpServletRequest request) {
        HttpSession session = request.getSession();
        
        if (kurssi.isJaassa()) {
            SessioApuri.annaVirhe(session, Lokalisaatio.bundle(request).getString("kurssiJaassa"));
            return;
        }

        try {
            String suoritusPvm = request.getParameter("suoritusPvm");
            
            int lh_pak = Integer.parseInt(request.getParameter("lh_pak"));
            int ht_pak = Integer.parseInt(request.getParameter("ht_pak"));
            int koe_pak = Integer.parseInt(request.getParameter("koe_pak"));

            int lh_oshyv = Integer.parseInt(request.getParameter("lh_oshyv"));
            int ht_oshyv = Integer.parseInt(request.getParameter("ht_oshyv"));
            int koe_oshyv = Integer.parseInt(request.getParameter("koe_oshyv"));

            int lh_lisapistemaksimi = Integer.parseInt(request.getParameter("lh_lisapistemaksimi"));
            int lh_lisapistealaraja = Integer.parseInt(request.getParameter("lh_lisapistealaraja"));
            float lh_kartuntavali = Float.parseFloat(request.getParameter("lh_kartuntavali"));

            int ht_lisapistemaksimi = Integer.parseInt(request.getParameter("ht_lisapistemaksimi"));
            int ht_lisapistealaraja = Integer.parseInt(request.getParameter("ht_lisapistealaraja"));
            float ht_kartuntavali = Float.parseFloat(request.getParameter("ht_kartuntavali"));

            int hyvalaraja = Integer.parseInt(request.getParameter("hyvalaraja"));
            float arvosanavali = Float.parseFloat(request.getParameter("arvosanavali"));

            String arvosteluasteikko = request.getParameter("arvosteluasteikko");
            int arvostelutapa = Integer.parseInt(request.getParameter("arvostelutapa"));
            
            /*
             * Suorituspäivämäärän asetus
             */
            if (!suoritusPvm.equals("")) {
                suoritusPvm = Suorituspaivamaara.tarkastaSuorituspvm(kurssi, suoritusPvm, request.getSession(), Lokalisaatio.bundle(request));
                if (suoritusPvm != null) {
                    kurssi.setValue(Kurssi.suoritus_pvm, java.sql.Date.valueOf(suoritusPvm));
                } else {
                    return;
                }
            }
            
            /*
             * Pakolliset osasuoritukset
             */
            if (lh_pak >= 0 && lh_pak <= Muotoilija.MAX_KOKO) {
                kurssi.setValue(Kurssi.pakolliset_laskarikerta_lkm, lh_pak);
            } else {
                SessioApuri.annaVirhe(session, Lokalisaatio.bundle(request).getString("paklaskharjvali") + "0-18");
            }

            if (ht_pak >= 0 && ht_pak <= Muotoilija.MAX_KOKO) {
                kurssi.setValue(Kurssi.pakolliset_harjoitustyo_lkm, ht_pak);
            } else {
                SessioApuri.annaVirhe(session,  Lokalisaatio.bundle(request).getString("pakhtvali") + "0-18");
            }

            if (koe_pak >= 0 && koe_pak <= Muotoilija.MAX_KOKO) {
                kurssi.setValue(Kurssi.pakolliset_koe_lkm, koe_pak);
            } else {
                SessioApuri.annaVirhe(session,  Lokalisaatio.bundle(request).getString("pakkoevali") + "0-18");
            }

            /*
             * Suorituspisteitä osasuorituksista
             */
            if (lh_oshyv >= 0 && lh_oshyv <= kurssi.getLaskariRajat().getMaxPisteetYhteensa()) {
                kurssi.setValue(Kurssi.pakolliset_laskaritehtava_lkm, lh_oshyv);
            } else {
                SessioApuri.annaVirhe(session, Lokalisaatio.bundle(request).getString("laskarihyvrajavali")  + kurssi.getLaskariRajat().getMaxPisteetYhteensa());
            }

            if (ht_oshyv >= 0 && ht_oshyv <= kurssi.getHarjoitustyoRajat().getMaxPisteetYhteensa()) {
                kurssi.setValue(Kurssi.min_harjoitustyopisteet_summa, ht_oshyv);
            } else {
                SessioApuri.annaVirhe(session, Lokalisaatio.bundle(request).getString("hthyvrajavali") + kurssi.getHarjoitustyoRajat().getMaxPisteetYhteensa());
            }

            if (koe_oshyv >= 0 && koe_oshyv <= kurssi.getKoeRajat().getMaxPisteetYhteensa()) {
                kurssi.setValue(Kurssi.min_koepisteet_summa, koe_oshyv);
            } else {
                SessioApuri.annaVirhe(session, Lokalisaatio.bundle(request).getString("koehyvrajavali")  + kurssi.getKoeRajat().getMaxPisteetYhteensa());
            }

            /**
             * Arvosteluun vaikuttavat lisäpisteet
             */
            kurssi.setValue(Kurssi.max_laskaripisteet, lh_lisapistemaksimi);
            kurssi.setValue(Kurssi.lisapistealaraja, lh_lisapistealaraja);
            kurssi.setValue(Kurssi.lisapisteiden_askelkoko, lh_kartuntavali);

            kurssi.setValue(Kurssi.harjoitustyopisteet, ht_lisapistemaksimi);
            kurssi.setValue(Kurssi.ht_lisapistealaraja, ht_lisapistealaraja);
            kurssi.setValue(Kurssi.harjoitustoiden_askelkoko, ht_kartuntavali);

            int pisteet_max = kurssi.getValue(Kurssi.max_laskaripisteet) + kurssi.getValue(Kurssi.harjoitustyopisteet) + kurssi.getKoeRajat().getMaxPisteetYhteensa();
            if (0 <= hyvalaraja && hyvalaraja <= pisteet_max) {
                kurssi.setValue(Kurssi.min_yhteispisteet, hyvalaraja);
            } else {
                SessioApuri.annaVirhe(session, Lokalisaatio.bundle(request).getString("hyvrajavali") + " 0-" + pisteet_max);
            }
            kurssi.setValue(Kurssi.arvostelun_askelkoko, arvosanavali);

            if (arvosteluasteikko.equals("K") || arvosteluasteikko.equals("E")) {
                kurssi.setValue(Kurssi.arvostellaanko, arvosteluasteikko);
            } else {
                SessioApuri.annaVirhe(session, Lokalisaatio.bundle(request).getString("arvasteikkovoiolla"));
            }

            if (arvostelutapa >= 0 && arvostelutapa <= 9) {
                kurssi.setValue(Kurssi.laskentakaava, arvostelutapa);
            } else {
                SessioApuri.annaVirhe(session, Lokalisaatio.bundle(request).getString("arvtavaksitaytyyantaa"));
            }
            try {
                SQLoader.tallennaKantaan(kurssi);
                SessioApuri.annaViesti(session, Lokalisaatio.bundle(request).getString("arvmuuttallennettu"));
            } catch (SQLException se) {
                SessioApuri.annaVirhe(session, Lokalisaatio.bundle(request).getString("kurssinkantaantallennusvirhe") + ": " + se.getLocalizedMessage());                
            }
        } catch (NumberFormatException e) {
            SessioApuri.annaVirhe(session, Lokalisaatio.bundle(request).getString("numeroaeivoitujasentaa"));
        } catch (Exception e) {
            SessioApuri.annaVirhe(session, e.getMessage());
        }
    }
}
