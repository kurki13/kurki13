/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.toiminnot;

import debug.Konfiguraatio;
import debug.SessioApuri;
import debug.model.Kurssi;
import debug.model.SQLkyselyt.HenkiloKyselyt;
import debug.model.SQLkyselyt.OsallistuminenKyselyt;
import debug.model.SQLkyselyt.SQLProseduurit;
import debug.util.LocalisationBundle;
import java.io.StringWriter;
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
        konteksti.put("jaadytys", Jaadytys.class);
        konteksti.put("HenkiloKyselyt", HenkiloKyselyt.class);
        konteksti.put("rivinvaihto", "\n");
        StringWriter kirjoittaja = new StringWriter();
        String projektinJuuri = System.getProperty("user.dir") + "/../..";
        Velocity.init(projektinJuuri + "/src/main/webapp/WEB-INF/velocity.properties");
        Velocity.mergeTemplate("sahkoposti.vm", "utf-8", konteksti, kirjoittaja);
        return kirjoittaja.toString();
    }
}
