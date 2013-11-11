/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.toiminnot;

import debug.Konfiguraatio;
import debug.SessioApuri;
import debug.model.Kurssi;
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

public class Jaadytys {
    
    public static boolean tarkastaSuorituspvm(Kurssi kurssi) {
        Calendar suoritusPvm = Calendar.getInstance();
        suoritusPvm.setTime(kurssi.getSuoritus_pvm());
        if (suoritusPvm.after(Calendar.getInstance())) {
            return false;
        }
        return true;
    }
    
    public static String asetaSuorituspvmlleAlaraja() {
        Calendar alaraja = Calendar.getInstance();
        alaraja.add(Calendar.MONTH, -6);
        SimpleDateFormat muotoilu = new SimpleDateFormat("yyyy-MM-dd");
        return muotoilu.format(alaraja.getTime());
    }
    
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
        
        SimpleDateFormat muotoilu = new SimpleDateFormat("yyyy-MM-dd");
        return muotoilu.format(ylaraja.getTime());
    }
    
    public static int prosenttiLasku(int jaettava, int jakaja) {
        if (jakaja == 0) {
            return 100;
        }
        float pyoristysKaannos1 = jaettava;
        float pyoristysKaannos2 = jakaja;
        return Math.round((jaettava/jakaja)*100);
    }
    
    public static void lahetaPostia(Kurssi kurssi, String kurssinTila, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        LocalisationBundle bundle = SessioApuri.bundle(request);
        String kurssinTiedot = kurssi.listaString();
        String etaKayttaja = request.getRemoteUser();
        String aihe = "KURKI: " + kurssinTiedot;
        if (kurssinTila.equals("J")) {
            aihe += " korjaukset";
        } else {
            aihe += " tulokset";
        }
        String viestinSisalto = luoViesti(request, kurssi, kurssinTila);
        String kayttajanOsoite = etaKayttaja + "@cs.helsinki.fi";
        
        Properties ominaisuudet = new Properties();
        ominaisuudet.put("mail.smtp.host", "localhost");
        javax.mail.Session postiIstunto = javax.mail.Session.getInstance(ominaisuudet);
        MimeMessage viesti = new MimeMessage(postiIstunto);
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
    
    public static String luoViesti(HttpServletRequest request, Kurssi kurssi, String kurssinTila) {
        VelocityContext konteksti = new VelocityContext();
        konteksti.put("request", request);
        konteksti.put("kurssi", kurssi);
        konteksti.put("kurssinTila", kurssinTila);
        StringWriter kirjoittaja = new StringWriter();
        //Velocity.init();
        Velocity.mergeTemplate("sahkoposti.vm", "utf-8", konteksti, kirjoittaja);
        return kirjoittaja.toString();
    }
}
