/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.util;

import database.Konfiguraatio;
import model.Kurssi;
import model.SQLkyselyt.HenkiloKyselyt;
import model.SQLkyselyt.OsallistuminenKyselyt;
import model.SQLkyselyt.SQLProseduurit;
import java.io.StringWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.Osallistuminen;
import org.apache.velocity.*;
import org.apache.velocity.app.*;

/**
 * Luokka sisältää apumetodeja jäädytys-toiminnon toteuttamiseksi.
 */
public class Jaadytys {
    
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
     * Metodi lähettää jäädytyksestä tiedottavan sähköpostin asianmukaisille tahoille.
     * 
     * @param kurssi Jäädytettävä kurssi
     * @param kurssinTila Jäädytettävän kurssin tila ennen jäädytystä
     * @param osallistumisetKurssilla Lista jäädytettävän kurssin osallistujista
     * @param request 
     */
    public static void lahetaPostia(Kurssi kurssi, String kurssinTila, List<Osallistuminen> osallistumisetKurssilla, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        Lokalisaatio bundle = Lokalisaatio.bundle(request);
        
        String aihe = asetaAihe(kurssi, kurssinTila);
        String viestinSisalto = luoViestinSisalto(request, kurssi, kurssinTila, osallistumisetKurssilla);
        String kayttajanOsoite = request.getRemoteUser() + "@cs.helsinki.fi";
        MimeMessage viesti = luoViesti();
        try {
            viesti.setFrom(new InternetAddress(Konfiguraatio.getProperty("webmaster")));
            viesti.addRecipient(Message.RecipientType.TO, new InternetAddress(Konfiguraatio.getProperty("oodi")));
            viesti.addRecipient(Message.RecipientType.TO, new InternetAddress(kayttajanOsoite));
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
     * Metodi luo sähköpostiviestin aiheen.
     * 
     * @param kurssi Jäädytettävä kurssi
     * @param kurssinTila Jäädytettävän kurssin tila ennen jäädytystä
     * @return Sähköpostiviestin aihe
     */
    private static String asetaAihe(Kurssi kurssi, String kurssinTila) {
        String kurssinTiedot = kurssi.listaString();
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
     * @param osallistumisetKurssilla Lista jäädytettävän kurssin osallistujista
     * @return Sähköpostiviestin tekstiosa
     */
    private static String luoViestinSisalto(HttpServletRequest request, Kurssi kurssi, String kurssinTila, List<Osallistuminen> osallistumisetKurssilla) {
        VelocityContext konteksti = new VelocityContext();
        konteksti.put("bundle", Lokalisaatio.bundle(request));
        konteksti.put("kurssi", kurssi);
        konteksti.put("kurssinTila", kurssinTila);
        konteksti.put("osallistumisetKurssilla", osallistumisetKurssilla);
        konteksti.put("jaadytys", Jaadytys.class);
        konteksti.put("OsallistuminenKyselyt", OsallistuminenKyselyt.class);
        konteksti.put("SQLProseduurit", SQLProseduurit.class);
        konteksti.put("Suorituspaivamaara", Suorituspaivamaara.class);
        konteksti.put("HenkiloKyselyt", HenkiloKyselyt.class);
        konteksti.put("rivinvaihto", "\n");
        StringWriter kirjoittaja = new StringWriter();
        URL url = Thread.currentThread().getContextClassLoader().getResource("../velocity.properties");
        Velocity.init(url.getPath());
        Velocity.mergeTemplate("sahkoposti.vm", "utf-8", konteksti, kirjoittaja);
        return kirjoittaja.toString();
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
}
