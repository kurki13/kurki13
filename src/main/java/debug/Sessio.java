/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug;

import debug.model.Kurssi;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author mkctammi
 */
public class Sessio {
    public Locale locale = new Locale("fi");
    public Kurssi valittuKurssi;
    public List<Kurssi> kurssit;
}
