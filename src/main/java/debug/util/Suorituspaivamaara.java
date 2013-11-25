/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.util;

import debug.SessioApuri;
import debug.dbconnection.DatabaseConnection;
import debug.model.Kurssi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Luokka sisältää suorituspäivämäärän asettamisessa tarvittavia metodeja.
 */
public class Suorituspaivamaara {
    /**
     * SQL valmisteltu-lause kurssin suorituspäivämäärän asettamiseksi.
     */
    private static final String asetaSuorituspvm = //<editor-fold defaultstate="collapsed" desc="asetaSuorituspvm">
		"UPDATE kurssi SET suoritus_pvm = ?\n"
		+ " WHERE kurssikoodi = ?\n"
		+ " AND lukukausi = ?\n"
		+ " AND lukuvuosi = ?\n"
		+ " AND tyyppi = ?\n"
		+ " AND kurssi_nro = ?\n";
	//</editor-fold>

    /**
     * Metodi tarkastaa onko parametrina annetun kurssin suorituspäivämäärä null.
     * 
     * @param kurssi Kurssi, jonka suorituspäivämäärä tarkastetaan
     * @return True, jos suorituspäivämäärä on null
     */
    public static boolean onkoSuorituspvmNull(Kurssi kurssi) {
        if (kurssi.getSuoritus_pvm() == null) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
    * Metodi tarkastaa, että parametrina annetun kurssin suorituspäivämäärä on kelvollinen 
    * ja muuntaa suorituspäivämäärän java.sql.Date.valueOf() metodille sopivaan muotoon.
    * 
    * @param kurssi Kurssi, jonka suorituspäivämäärän kelvollisuutta tarkastetaan
    * @param suoritusPvm Kurssin suorituspäivämäärä
    * @param request 
    * @return Kurssin suorituspäivämäärä muodossa (vvvv-kk-pp)
    */
    public static String tarkastaSuorituspvm(Kurssi kurssi, String suoritusPvm, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        LocalisationBundle bundle = SessioApuri.bundle(request);
        
        try {
            if (!tarkastaSuorituspvmnMuoto(suoritusPvm, kurssi, istunto, bundle)) {
                return null;
            }
            suoritusPvm = vaihdaPvmnMuotoa(suoritusPvm);
        } catch (IndexOutOfBoundsException poikkeus1) {
            SessioApuri.annaVirhe(istunto, bundle.getString("virheVirheellinenMuoto") + ": " + poikkeus1.getLocalizedMessage());
            return null;
        } catch (NullPointerException poikkeus2) {
            SessioApuri.annaVirhe(istunto, bundle.getString("virheVirheellinenMuoto") + ": " + poikkeus2.getLocalizedMessage());
            return null;
        } catch (NumberFormatException poikkeus3) {
            SessioApuri.annaVirhe(istunto, bundle.getString("virheVirheellinenMuoto") + ": " + poikkeus3.getLocalizedMessage());
            return null;
        }
        return suoritusPvm;
    }
    
    /**
     * Metodi tarkastaa, että parametrina annetun kurssin suorituspäivämäärä on syötetty kelvollisessa muodossa.
     * 
     * @param suoritusPvm Kurssin suorituspäivämäärä
     * @param kurssi Kurssi, jonka suorituspäivämäärän kelvollisuutta tarkastetaan 
     * @param istunto Käyttäjän istunto
     * @param bundle Lokalisaatio työkalu
     * @return Onko suorituspäivämäärä syötetty kelvollisessa muodossa?
     * @throws IndexOutOfBoundsException Suorituspäivämäärä syötetty väärässä muodossa
     * @throws NullPointerException Suorituspäivämäärä null
     * @throws NumberFormatException Suorituspäivämäärä syötetty väärässä muodossa 
     */
    private static boolean tarkastaSuorituspvmnMuoto(String suoritusPvm, Kurssi kurssi, HttpSession istunto, LocalisationBundle bundle) 
            throws IndexOutOfBoundsException, NullPointerException, NumberFormatException {
        if (suoritusPvm.length() > 10 || !suoritusPvm.substring(2, 3).equals(".") || !suoritusPvm.substring(5, 6).equals(".")) {
            SessioApuri.annaVirhe(istunto, bundle.getString("virheVirheellinenMuoto") + ".");
            return false;
        }

        if (!tarkastaSuoritusPvmnAjankohta(suoritusPvm, kurssi, istunto, bundle)) {
            return false;
        }
        return true;  
    }
    
    /**
     * Metodi tarkastaa, että parametrina annetun kurssin suorituspäivämäärän ajankohta on kelvollinen.
     * 
     * @param suoritusPvm Kurssin suorituspäivämäärä
     * @param kurssi Kurssi, jonka suorituspäivämäärän ajankohdan kelvollisuutta tarkastetaan
     * @param istunto Käyttäjän istunto
     * @param bundle Lokalisaatio työkalu
     * @return Onko suorituspäivämäärän ajankohta kelvollinen?
     * @throws IndexOutOfBoundsException Suorituspäivämäärä syötetty väärässä muodossa
     * @throws NullPointerException Suorituspäivämäärä null
     * @throws NumberFormatException Suorituspäivämäärä syötetty väärässä muodossa 
     */
    private static boolean tarkastaSuoritusPvmnAjankohta(String suoritusPvm, Kurssi kurssi, HttpSession istunto, LocalisationBundle bundle) 
            throws IndexOutOfBoundsException, NullPointerException, NumberFormatException {
        int[] paivamaara = parsiPaivamaaraSyote(suoritusPvm);
        if (paivamaara[1] < 1 || paivamaara[1] > 12) {
            SessioApuri.annaVirhe(istunto, bundle.getString("virheKuukausi"));
            return false;
        }
        if (paivamaara[1] == 2) {
            if (!kasitteleHelmikuu(paivamaara[0], paivamaara[1], paivamaara[2], istunto, bundle)){
                return false;
            }
        }
        if (!kasittelePaiva(paivamaara[0], paivamaara[1], istunto, bundle)) {
            return false;
        }
        if (!tarkastaSuorituspvmnRajat(paivamaara, kurssi, istunto, bundle)) {
            return false;
        }
        return true;
    }
    
    /**
     * Metodi parsii (pp.kk.vvvv) muodossa annetusta päivämäärä syötteestä 
     * päivän, kuukauden ja vuoden taulukkoon.
     * 
     * @param syote Parsittava päivämäärä
     * @return Taulukko, joka sisältää päivän indeksissä 0, kuukauden indeksissä 1 ja vuoden indeksissä 2
     * @throws IndexOutOfBoundsException Syöte väärässä muodossa
     * @throws NullPointerException Syöte null
     * @throws NumberFormatException Syöte väärässä muodossa
     */
    private static int[] parsiPaivamaaraSyote(String syote) 
            throws IndexOutOfBoundsException, NullPointerException, NumberFormatException {
        int[] palautus = new int[3];
        String paivaString = syote.substring(0, 2);
        String kuukausiString = syote.substring(3, 5);
        String vuosiString = syote.substring(6, 10);
        palautus[0] = Integer.parseInt(paivaString);
        palautus[1] = Integer.parseInt(kuukausiString);
        palautus[2] = Integer.parseInt(vuosiString);
        return palautus;
    }
    
    /**
     * Metodi tarkastaa että parametrina annettu päivä on kelvollinen Helmikuun päivä.
     * 
     * @param paiva Päivämäärän päivä
     * @param kuukausi Päivämäärän kuukausi
     * @param vuosi Päivämäärän vuosi
     * @param istunto Käyttäjän istunto
     * @param bundle Lokalisaatio työkalu
     * @return True, jos päivä on kelvollinen helmikuun päivä
     */
    private static boolean kasitteleHelmikuu(int paiva, int kuukausi, int vuosi, HttpSession istunto, LocalisationBundle bundle) {
        if(onkoKarkausvuosi(vuosi)) {
            if (paiva < 1 || paiva > 29) {
                SessioApuri.annaVirhe(istunto, bundle.getString("virheKarkausvuodenHelmikuu"));
                return false;
            }
        } else {
            if (paiva < 1 || paiva > 28) {
                SessioApuri.annaVirhe(istunto, bundle.getString("virheKarkausvuodettomanHelmikuun"));
                return false;
            }
        }
        return true;
    }
    
    /**
     * Metodi tarkastaa onko parametrina annettu vuosi karkausvuosi
     * @param vuosi Tarkastettava vuosi
     * @return True, jos vuosi on karkausvuosi
     */
    private static boolean onkoKarkausvuosi(int vuosi) {
        if (vuosi % 400 == 0)
            return true;
        if (vuosi % 100 == 0)
            return false;
        if (vuosi % 4 == 0)
            return true;
        return false;
    }
    
    /**
     * Metodi tarkastaa, että parametrina annettu päivä on kelvollinen parametrina annetun kuukauden päivä.
     * 
     * @param paiva Päivämäärän päivä
     * @param kuukausi Päivämäärän kuukausi
     * @param istunto Käyttäjän istunto
     * @param bundle Lokalisaatio työkalu
     * @return True, jos päivä on kelvollinen annetun kuukauden päivä
     */
    private static boolean kasittelePaiva(int paiva, int kuukausi, HttpSession istunto, LocalisationBundle bundle) {
        int[] kuukaudetJoissa31Paivaa = {1, 3, 5, 7, 8, 10, 12};
        for (int alkio : kuukaudetJoissa31Paivaa) {
            if (kuukausi == alkio) {
                if (paiva < 1 || paiva > 31) {
                    SessioApuri.annaVirhe(istunto, bundle.getString("virhePaiva1"));
                    return false;
                }
            } 
        }
        if (paiva < 1 || paiva > 30) {
            SessioApuri.annaVirhe(istunto, bundle.getString("virhePaiva2"));
            return false;
        }
        return true;
    }
    
    /**
     * Metodi tarkastaa, että parametrina annetun kurssin suorituspäivämäärä 
     * sopii suorituspäivämäärälle asetetetun ylä- ja alarajan väliin.
     * @param pvm Kurssin suorituspäivämäärä
     * @param kurssi Kurssi, jonka suorituspäivämäärää tarkastetaan
     * @param istunto Käyttäjän istunto
     * @param bundle Lokalisaatio työkalu
     * @return True, jos suorituspäivämäärä sopii rajojen väliin
     */
    private static boolean tarkastaSuorituspvmnRajat(int[] pvm, Kurssi kurssi, HttpSession istunto, LocalisationBundle bundle) {
        Calendar alaraja = Calendar.getInstance();
        alaraja.add(Calendar.MONTH, -6);
        Calendar ylaraja;
        try {
            ylaraja = asetaSuorituspvmlleYlaraja(kurssi);
        } catch (NullPointerException poikkeus) {
            SessioApuri.annaVirhe(istunto, bundle.getString("kurssinPaattymispvmEiAsetettu"));
            return false;
        }
        Calendar suoritusPvm = Calendar.getInstance();
        suoritusPvm.set(pvm[2], pvm[1]-1, pvm[0]);
        if (suoritusPvm.before(alaraja) || suoritusPvm.after(ylaraja)) {
            String virhe = bundle.getString("virheellinenSuorituspvm") + bundle.getString("suorituspvmValilla") 
                    + alaraja.get(Calendar.DAY_OF_MONTH) + "." + (alaraja.get(Calendar.MONTH)+1) + "." + alaraja.get(Calendar.YEAR) + "-"
                    + ylaraja.get(Calendar.DAY_OF_MONTH) + "." + (ylaraja.get(Calendar.MONTH)+1) + "." + ylaraja.get(Calendar.YEAR) 
                    + bundle.getString("ennenJaadyttamista");
            SessioApuri.annaVirhe(istunto, virhe);
            return false;
        }
        return true;
    }
    
    /**
     * Metodi asettaa parametrina annetun kurssin suorituspäivämäärälle ylärajan.
     * 
     * @param kurssi Kurssi, jonka suorituspäivämäärälle yläraja asetetaan
     * @return Asetettu yläraja
     * @throws NullPointerException Kurssille ei ole asetettu päättymispäivämäärää tietokantaan
     */
    private static Calendar asetaSuorituspvmlleYlaraja(Kurssi kurssi) throws NullPointerException {
        Calendar paattymisPaiva = Calendar.getInstance();
        paattymisPaiva.setTime(kurssi.getPaattymis_pvm());
        Calendar ylaraja;
        Calendar jarjestelmanPaivays = Calendar.getInstance();
        
        if (jarjestelmanPaivays.before(paattymisPaiva)) {
            ylaraja = paattymisPaiva;
        } else {
            ylaraja = jarjestelmanPaivays;
        }
        ylaraja.add(Calendar.MONTH, 2);
        return ylaraja;
    }
    
    /**
     * Metodi vaihtaa muodossa (pp.kk.vvvv) annetun syötteen muodon muotoon (vvvv-kk-pp).
     * 
     * @param pvm Päivämäärä, jonka muoto vaihdetaan
     * @return Päivämäärä uudessa muodossa
     * @throws IndexOutOfBoundsException Syöte väärässä muodossa
     * @throws NullPointerException Syöte null
     * @throws NumberFormatException Syöte väärässä muodossa
     */
    public static String vaihdaPvmnMuotoa(String pvm) throws IndexOutOfBoundsException, NullPointerException, NumberFormatException {
        int[] paivamaara = parsiPaivamaaraSyote(pvm);
        String palautus = paivamaara[2] + "-" + paivamaara[1] + "-" + paivamaara[0];
        return palautus;
    }
    
    /**
     * Metodi kirjaa tietokantaan parametrina annetulle kurssille parametrina annetun suorituspäivämäärän.
     * 
     * @param suoritusPvm Kirjattava suorituspäivämäärä
     * @param kurssi Kurssi, jolle suorituspäivämäärä kirjataan
     * @param istunto Käyttäjän istunto
     * @param bundle Lokalisaatio työkalu
     */
    public static void suoritaAsetus(String suoritusPvm, Kurssi kurssi, HttpSession istunto, LocalisationBundle bundle) {
        try {
            Connection tietokantayhteys = DatabaseConnection.makeConnection();
            PreparedStatement valmisteltuLause = tietokantayhteys.prepareStatement(asetaSuorituspvm);
            valmisteltuLause.setDate(1, java.sql.Date.valueOf(suoritusPvm));
            valmisteltuLause.setString(2, kurssi.getKurssikoodi());
            valmisteltuLause.setString(3, kurssi.getLukukausi());
            valmisteltuLause.setInt(4, kurssi.getLukuvuosi());
            valmisteltuLause.setString(5, kurssi.getTyyppi());
            valmisteltuLause.setInt(6, kurssi.getKurssi_nro());
            valmisteltuLause.executeUpdate();
            valmisteltuLause.close();
            tietokantayhteys.close();
        } catch (SQLException poikkeus2) {
            String virhe = bundle.getString("tkvirhe") + ": " + poikkeus2.getLocalizedMessage();
            SessioApuri.annaVirhe(istunto, virhe);
        }	
    }
    
    /**
     * Metodi palauttaa parametrina annetun kurssin suorituspäivämäärälle ylärajan.
     * 
     * @param kurssi Kurssi, jonka suorituspäivämäärän yläraja palautetaan
     * @return Suorituspäivämäärän yläraja muodossa (pp.kk.vvvv)
     */
    public static String palautaSuorituspvmnYlaraja(Kurssi kurssi) {
        Calendar ylaraja;
        try {
            ylaraja = asetaSuorituspvmlleYlaraja(kurssi);
        } catch (NullPointerException poikkeus) {
            return null;
        }
        SimpleDateFormat muotoilu = new SimpleDateFormat("dd.MM.yyyy");
        return muotoilu.format(ylaraja.getTime());
    }
    
    /**
     * Metodi palauttaa parametrina annetun kurssin suorituspäivämäärän muodossa (pp.kk.vvvv).
     * 
     * @param kurssi Kurssi, jonka päivämäärä palautetaan
     * @return Kurssin suorituspäivämäärä tai tyhjä String, jos kurssille ei ole asetettu suorituspäivämäärää tietokantaan
     */
    public static String palautaSuorituspvm(Kurssi kurssi) {
        Date suoritusPvm = kurssi.getSuoritus_pvm();
        SimpleDateFormat muotoilu = new SimpleDateFormat("dd.MM.yyyy");
        String palautus;
        try {
            palautus = muotoilu.format(suoritusPvm.getTime());
        } catch (NullPointerException poikkeus) {
            palautus = "";
        }
        return palautus;
    }
    
    /**
     * Metodi tarkastaa onko parametrina annettu olio null
     * 
     * @param suoritusPvm Tarkastettava parametri
     * @return Onko parametri null?
     */
    public static boolean onkoNull(Object suoritusPvm) {
        if (suoritusPvm == null) {
            return true;
        } else {
            return false;
        }
    }
}
