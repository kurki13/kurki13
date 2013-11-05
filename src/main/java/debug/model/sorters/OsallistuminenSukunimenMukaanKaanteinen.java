/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.sorters;

import debug.model.Osallistuminen;
import java.util.Comparator;

/**
 *
 * @author topisark
 */
public class OsallistuminenSukunimenMukaanKaanteinen implements Comparator<Osallistuminen>{

    @Override
    public int compare(Osallistuminen o1, Osallistuminen o2) {
        String sukunimi1 = o1.getSukunimi();
        String sukunimi2 = o2.getSukunimi();
        return sukunimi2.compareTo(sukunimi1);
    }
    
}
