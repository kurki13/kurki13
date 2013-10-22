/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.sorters;

import debug.model.Osallistuminen;
import java.sql.Timestamp;
import java.util.Comparator;

/**
 *
 * @author topisark
 */
public class OsallistuminenIlmjarjestyksenMukaan implements Comparator<Osallistuminen> {

    @Override
    public int compare(Osallistuminen o1, Osallistuminen o2) {
        Timestamp ilm1 = o1.getIlmoittautumis_pvm();
        Timestamp ilm2 = o2.getIlmoittautumis_pvm();
        return ilm1.compareTo(ilm2);
    }
    
}
