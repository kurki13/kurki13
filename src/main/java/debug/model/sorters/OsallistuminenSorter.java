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
    
     public static void sorttaaSukunimenMukaanKaanteinen(List<Osallistuminen> lista) {
        Collections.sort(lista, new OsallistuminenSukunimenMukaanKaanteinen());
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
            return kopio;

        } else {
            if (filter.contains("..")) {
                System.out.println("Pisteitä filterissä!");
                System.out.println(filter);
                Collections.sort(kopio, new OsallistuminenSukunimenMukaan());
                Collections.sort(lista, new OsallistuminenSukunimenMukaan());
                String[] sanat = filter.split("\\.\\.");
                if (sanat.length == 1) {
                    String alku = sanat[0].trim();
                    System.out.println(alku);
                    for (Osallistuminen osallistuminen : lista) {
                        if (!osallistuminen.getSukunimi().matches(alku + ".*")) {                            
                            kopio.remove(osallistuminen);
                        } else {                            
                            break;
                        }
                    }
                    return kopio;
                } else if (sanat.length > 1) {
                    String alku = sanat[0].trim();
                    String loppu = sanat[1].trim();
                    System.out.println(alku);
                    for (Osallistuminen osallistuminen : lista) {
                        if (!osallistuminen.getSukunimi().matches(alku + ".*")) {                            
                            kopio.remove(osallistuminen);
                        } else {                           
                            break;
                        }
                    }  
                    Collections.sort(lista, new OsallistuminenSukunimenMukaanKaanteinen());
                    for (Osallistuminen osallistuminen : lista) {
                        if (!osallistuminen.getSukunimi().matches(loppu + ".*")) {                            
                            kopio.remove(osallistuminen);
                        } else {                           
                            break;
                        }
                    }                    
                    return kopio;
                } else {
                    return lista;
                }
            }
            if (filter.charAt(0) != '#') {
                for (Osallistuminen osallistuminen : lista) {
                    if (!osallistuminen.getSukunimi().matches(filter + ".*")) {
                        kopio.remove(osallistuminen);
                    }
                }
                lista = kopio;
                return lista;
            } else if (filter.charAt(0) == '#') {
                String substring = filter.substring(1);
                for (Osallistuminen osallistuminen : lista) {
                    if (!osallistuminen.getHetu().matches(substring + ".*")) {
                        kopio.remove(osallistuminen);
                    }
                }
                return kopio;
            }
        }
        return lista;
    }
}
