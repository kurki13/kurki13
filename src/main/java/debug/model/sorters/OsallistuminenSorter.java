/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.sorters;

import debug.model.Osallistuminen;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author topisark
 */
public class OsallistuminenSorter {

    public static void sorttaaSukunimenMukaan(List<Osallistuminen> lista) {
        Collections.sort(lista, new OsallistuminenSukunimenMukaan());
    }

    public static void sorttaaIlmjarjestyksenMukaan(List<Osallistuminen> lista) {
        Collections.sort(lista, new OsallistuminenIlmjarjestyksenMukaan());
    }

    public static List filteroi(List<Osallistuminen> lista, String filter) {
        ArrayList<Osallistuminen> kopio = new ArrayList<Osallistuminen>(lista);
        if (StringUtils.isNumeric(filter)) {
            int numero = Integer.parseInt(filter);
            for (Osallistuminen osallistuminen : lista) {
                if (osallistuminen.getIlmo_jnro() != numero) {                    
                    kopio.remove(osallistuminen);
                }
            }
            lista = kopio; 
            return lista;
        } else {
            for (Osallistuminen osallistuminen : lista) {
                if (!osallistuminen.getSukunimi().matches(filter + ".*")) {                    
                    kopio.remove(osallistuminen);
                }
            }
            lista = kopio; 
            return lista;
        }     
    }
}
