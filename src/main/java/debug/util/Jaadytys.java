/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.util;

import debug.Konfiguraatio;
import debug.SessioApuri;
import debug.model.Kurssi;
import debug.model.SQLkyselyt.HenkiloKyselyt;
import debug.model.SQLkyselyt.OsallistuminenKyselyt;
import debug.model.SQLkyselyt.SQLProseduurit;
import debug.util.LocalisationBundle;
import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.velocity.*;
import org.apache.velocity.app.*;

/**
 * Luokka sisältää apumetodeja jäädytys-toiminnon toteuttamiseksi.
 */
public class Jaadytys {
    
    public static boolean onkoSuorituspvmNull(Kurssi kurssi) {
        if (kurssi.getSuoritus_pvm() == null) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Metodi tarkastaa, onko parametrina annetun kurssin suorituspäivämäärä ennen metodin ajamisajankohtaa.
     * Jos on, niin metodi palauttaa true.
     * 
     * @param kurssi Kurssi, jonka suoritupäivämäärälle vertailu suoritetaan
     * @return Onko suorituspäivämäärä ennen metodin ajamisajankohtaa?
     */
    public static boolean tarkastaSuorituspvm(Kurssi kurssi) {
        Calendar suoritusPvm = Calendar.getInstance();
        suoritusPvm.setTime(kurssi.getSuoritus_pvm());
        if (suoritusPvm.after(Calendar.getInstance())) {
            return false;
        }
        return true;
    }
    
    /**
     * Metodi asettaa kurssin suorituspäivämäärän asetukselle ylärajan. 
     * 
     * @param kurssi Kurssi, jolle yläraja asetetaan
     * @return Asetettu yläraja sopivassa muodossa
     */
    public static String asetaSuorituspvmlleYlaraja(Kurssi kurssi) {
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
        
        SimpleDateFormat muotoilu = new SimpleDateFormat("MM/dd/yyyy");
        return muotoilu.format(ylaraja.getTime());
    }
    
    public static boolean tarkastaSuorituspvmnMuoto(String suoritusPvm, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        LocalisationBundle bundle = SessioApuri.bundle(request);
        try {
            int[] paivamaara = parsiPaivamaaraSyote(suoritusPvm);
            if (suoritusPvm.length() > 10) {
                SessioApuri.annaVirhe(istunto, bundle.getString("virheVirheellinenMuoto") + ".");
                return false;
            }
            if (paivamaara[0] < 1 || paivamaara[0] > 12) {
                SessioApuri.annaVirhe(istunto, bundle.getString("virheKuukausi"));
                return false;
            }
            if (paivamaara[0] == 2) {
                if (!kasitteleHelmikuu(paivamaara[1], paivamaara[0], paivamaara[2], request)){
                    return false;
                }
            }
            if (!kasittelePaiva(paivamaara[1], paivamaara[0], request)) {
                return false;
            }
            return true;  
        } catch (Exception poikkeus) {
            SessioApuri.annaVirhe(istunto, bundle.getString("virheVirheellinenMuoto") + ": " + poikkeus.getLocalizedMessage());
            return false;
        }  
    }
    
    private static int[] parsiPaivamaaraSyote(String syote) {
        int[] palautus = new int[3];
        String kuukausiString = syote.substring(0, 2);
        String paivaString = syote.substring(3, 5);
        String vuosiString = syote.substring(6, 10);
        palautus[0] = Integer.parseInt(kuukausiString);
        palautus[1] = Integer.parseInt(paivaString);
        palautus[2] = Integer.parseInt(vuosiString);
        return palautus;
    }
    
    private static boolean kasitteleHelmikuu(int paiva, int kuukausi, int vuosi, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        LocalisationBundle bundle = SessioApuri.bundle(request);
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
    
    private static boolean onkoKarkausvuosi(int vuosi) {
        if (vuosi % 400 == 0)
            return true;
        if (vuosi % 100 == 0)
            return false;
        if (vuosi % 4 == 0)
            return true;
        return false;
    }
    
    private static boolean kasittelePaiva(int paiva, int kuukausi, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        LocalisationBundle bundle = SessioApuri.bundle(request);
        int[] kuukaudetJoissa31Paivaa = {1, 3, 5, 7, 8, 10, 12};
        for (int alkio : kuukaudetJoissa31Paivaa) {
            if (kuukausi == alkio) {
                if (paiva < 1 || paiva > 31) {
                    SessioApuri.annaVirhe(istunto, bundle.getString("virhePaiva1"));
                    return false;
                }
            } else {
                if (paiva < 1 || paiva > 30) {
                    SessioApuri.annaVirhe(istunto, bundle.getString("virhePaiva2"));
                    return false;
                }
            }
        }
        return true;
    }
    /*
    private static boolean tarkastaSuorituspvmnRajat(String suoritusPvm, Kurssi kurssi) {
        Calendar ylaraja = Calendar.getInstance();
        String ylarajaString = asetaSuorituspvmlleYlaraja(kurssi);
    }
    */
    public static String vaihdaPvmnMuotoa(String pvm) {
        int[] paivamaara = parsiPaivamaaraSyote(pvm);
        String palautus = paivamaara[2] + "-" + paivamaara[0] + "-" + paivamaara[1];
        return palautus;
    }
    
    /**
     * Metodi lähettää jäädytyksestä tiedottavan sähköpostin asianmukaisille tahoille.
     * 
     * @param kurssi Jäädytettävä kurssi
     * @param kurssinTila Jäädytettävän kurssin tila ennen jäädytystä
     * @param request 
     */
    public static void lahetaPostia(Kurssi kurssi, String kurssinTila, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        LocalisationBundle bundle = SessioApuri.bundle(request);
        String aihe = asetaAihe(kurssi, kurssinTila);
        String viestinSisalto = luoViestinSisalto(request, kurssi, kurssinTila);
        String kayttajanOsoite = request.getRemoteUser() + "@cs.helsinki.fi";
        MimeMessage viesti = luoViesti();
        try {
            viesti.setFrom(new InternetAddress(Konfiguraatio.getProperty("webmaster")));
            viesti.addRecipient(Message.RecipientType.TO, new InternetAddress(Konfiguraatio.getProperty("oodi")));
            viesti.addRecipient(Message.RecipientType.TO, new InternetAddress("heikki.havukainen@helsinki.fi")); //kayttajanOsoite
            viesti.setSubject(aihe);
            viesti.setText(viestinSisalto);
            Transport.send(viesti);
        } catch (AddressException poikkeus1) {
            SessioApuri.annaVirhe(istunto, bundle.getString("sahkopostiPoikkeus") + ": " + poikkeus1.getLocalizedMessage());
        } catch (MessagingException poikkeus2) {
            SessioApuri.annaVirhe(istunto, bundle.getString("sahkopostiPoikkeus") + ": " + poikkeus2.getLocalizedMessage());
        }
    }
    
    /**
     * Metodi luo tyhjän viesti-olion.
     * 
     * @return Tyhjä viesti-olio
     */
    private static MimeMessage luoViesti() {
        Properties ominaisuudet = new Properties();
        ominaisuudet.put("mail.smtp.host", "localhost");
        javax.mail.Session postiIstunto = javax.mail.Session.getInstance(ominaisuudet);
        MimeMessage viesti = new MimeMessage(postiIstunto);
        return viesti;
    }
    
    /**
     * Metodi luo sähköpostiviestin aiheen.
     * 
     * @param kurssi Jäädytettävä kurssi
     * @param kurssinTila Jäädytettävän kurssin tila ennen jäädytystä
     * @return Sähköpostiviestin aihe
     */
    private static String asetaAihe(Kurssi kurssi, String kurssinTila) {
        String kurssinTiedot = kurssi.listaString();
        kurssinTiedot = kurssinTiedot.substring(0, kurssinTiedot.length()-2);
        String aihe = "KURKI: " + kurssinTiedot;
        if (kurssinTila.equals("J")) {
            aihe += " korjaukset";
        } else {
            aihe += " tulokset";
        }
        return aihe;
    }
    
    /**
     * Metodi luo sähköpostiviestin tekstiosan sisällön.
     * 
     * @param request
     * @param kurssi Jäädytettävä kurssi
     * @param kurssinTila Jäädytettävän kurssin tila ennen jäädytystä
     * @return Sähköpostiviestin tekstiosa
     */
    private static String luoViestinSisalto(HttpServletRequest request, Kurssi kurssi, String kurssinTila) {
        VelocityContext konteksti = new VelocityContext();
        konteksti.put("bundle", SessioApuri.bundle(request));
        konteksti.put("kurssi", kurssi);
        konteksti.put("kurssinTila", kurssinTila);
        konteksti.put("OsallistuminenKyselyt", OsallistuminenKyselyt.class);
        konteksti.put("SQLProseduurit", SQLProseduurit.class);
        konteksti.put("HenkiloKyselyt", HenkiloKyselyt.class);
        konteksti.put("rivinvaihto", "\n");
        StringWriter kirjoittaja = new StringWriter();
        URL url = Thread.currentThread().getContextClassLoader().getResource("../velocity.properties");
        Velocity.init(url.getPath());
        Velocity.mergeTemplate("sahkoposti.vm", "utf-8", konteksti, kirjoittaja);
        return kirjoittaja.toString();
    }
}
