/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.toiminnot;

import debug.model.Kurssi;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
}
