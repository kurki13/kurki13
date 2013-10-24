/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.toiminnot;

import debug.model.Kurssi;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

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
    /*
    public static void lahetaPostia(String kurssinTiedot, String kurssinTila, String etaKayttaja) 
            throws AddressException, MessagingException {
        String aihe = "KURKI: " + kurssinTiedot;
        if (kurssinTila.equals("J")) {
            aihe += " korjaukset";
        } else {
            aihe += " tulokset";
        }
        String viestinSisalto = luoViesti();
        String kayttajanOsoite = etaKayttaja + "@cs.helsinki.fi";
        
        Properties ominaisuudet = new Properties();
        ominaisuudet.put("mail.smtp.host", "localhost");
        javax.mail.Session postiIstunto = javax.mail.Session.getInstance(ominaisuudet);
        MimeMessage viesti = new MimeMessage(postiIstunto);
        viesti.setFrom(new InternetAddress("heikki.havukainen@helsinki.fi")); // webmaster
        viesti.addRecipient(Message.RecipientType.TO, new InternetAddress("heikki.havukainen@helsinki.fi")); // oodi
        viesti.addRecipient(Message.RecipientType.TO, new InternetAddress("kayttajanOsoite"));
        viesti.setSubject(aihe);
        viesti.setText(viestinSisalto);
        Transport.send(viesti);
    }
    
    public static String luoViesti() {
        VelocityContext konteksti = new VelocityContext();
        StringWriter kirjoittaja = new StringWriter();
        Velocity.mergeTemplate("sahkoposti.vm", "utf-8", konteksti, kirjoittaja);
        return kirjoittaja.toString();
    }
    
    
    private VelocityContext setResults(VelocityContext results, Course course, kurki.Session session) {
        results.put("students", course.getStudents());
        results.put("selectedCourse", session.getSelectedCourse());
        results.put("inc_ssn", "true"); // Opiskelijanro (HL08/8/)
        results.put("inc_name", "true"); // Nimi
        results.put("inc_grade", "true"); // Arvosana
        results.put("inc_gradename", "Arvosana"); // Arvosanan sarakeotsake
        results.put("inc_accepted", "true"); // Hyväksytyt
        results.put("inc_failed", "true"); // Hylätyt   
 	results.put("inc_", "true");
        return results;
   }
   */
}
