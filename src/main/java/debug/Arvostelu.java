/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug;

import debug.model.Kurssi;
import debug.model.osasuoritukset.Muotoilija;
import debug.model.util.SQLoader;
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

        try {
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
             * Pakolliset osasuoritukset
             */
            if (lh_pak >= 0 && lh_pak <= Muotoilija.MAX_KOKO) {
                kurssi.setValue(Kurssi.pakolliset_laskarikerta_lkm, lh_pak);
            } else {
                SessioApuri.annaVirhe(session, "Pakollisten laskuharjoitusten tulee olla välillä 0-18");
            }

            if (ht_pak >= 0 && ht_pak <= Muotoilija.MAX_KOKO) {
                kurssi.setValue(Kurssi.pakolliset_harjoitustyo_lkm, ht_pak);
            } else {
                SessioApuri.annaVirhe(session, "Pakollisten harjoitustöiden tulee olla välillä 0-18");
            }

            if (koe_pak >= 0 && koe_pak <= Muotoilija.MAX_KOKO) {
                kurssi.setValue(Kurssi.pakolliset_koe_lkm, koe_pak);
            } else {
                SessioApuri.annaVirhe(session, "Pakollisten kokeiden tulee olla välillä 0-18");
            }

            /*
             * Suorituspisteitä osasuorituksista
             */
            if (lh_oshyv >= 0 && lh_oshyv <= kurssi.getLaskariRajat().getMaxPisteetYhteensa()) {
                kurssi.setValue(Kurssi.pakolliset_laskaritehtava_lkm, lh_oshyv);
            } else {
                SessioApuri.annaVirhe(session, "Laskariosuuden hyväksymisrajan tulee olla väliltä 0-" + kurssi.getLaskariRajat().getMaxPisteetYhteensa());
            }

            if (ht_oshyv >= 0 && ht_oshyv <= kurssi.getHarjoitustyoRajat().getMaxPisteetYhteensa()) {
                kurssi.setValue(Kurssi.min_harjoitustyopisteet_summa, ht_oshyv);
            } else {
                SessioApuri.annaVirhe(session, "Harjoitustyöosuuden hyväksymisrajan tulee olla väliltä 0-" + kurssi.getHarjoitustyoRajat().getMaxPisteetYhteensa());
            }

            if (koe_oshyv >= 0 && koe_oshyv <= kurssi.getKoeRajat().getMaxPisteetYhteensa()) {
                kurssi.setValue(Kurssi.min_koepisteet_summa, koe_oshyv);
            } else {
                SessioApuri.annaVirhe(session, "Koeosuuden hyväksymisrajan tulee olla väliltä 0-" + kurssi.getKoeRajat().getMaxPisteetYhteensa());
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
                SessioApuri.annaVirhe(session, "Hyväksymisalarajan täytyy olla väliltä 0-" + pisteet_max);
            }
            kurssi.setValue(Kurssi.arvostelun_askelkoko, arvosanavali);

            if (arvosteluasteikko.equals("K") || arvosteluasteikko.equals("E")) {
                kurssi.setValue(Kurssi.arvostellaanko, arvosteluasteikko);
            } else {
                SessioApuri.annaVirhe(session, "Arvosteluasteikko voi olla vain K tai E");
            }

            if (arvostelutapa >= 0 && arvostelutapa <= 9) {
                kurssi.setValue(Kurssi.laskentakaava, arvostelutapa);
            } else {
                SessioApuri.annaVirhe(session, "Arvostelutavaksi täytyy antaa luku 1-9");
            }
            try {
                SQLoader.tallennaKantaan(kurssi);
                SessioApuri.annaViesti(session, "Arvostelun muutokset tallennettu");
            } catch (SQLException se) {
                SessioApuri.annaVirhe(session, "Kurssin kantaan tallennuksessa tapahtui virhe: " + se.getLocalizedMessage());                
            }
        } catch (NumberFormatException e) {
            SessioApuri.annaVirhe(session, "Virhe: Numeroa ei voitu jäsentää");
        } catch (Exception e) {
            SessioApuri.annaVirhe(session, e.getMessage());
        }
    }
}
