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

    public static List filteroiRyhmanMukaan(List<Osallistuminen> lista, String filter) {
        int numero = Integer.parseInt(filter);
        ArrayList<Osallistuminen> kopio = new ArrayList<Osallistuminen>(lista);
        for (Osallistuminen osallistuminen : lista) {
            if (osallistuminen.getIlmo_jnro() != numero) {
                kopio.remove(osallistuminen);
            }
        }
        return kopio;
    }

    public static List filteroiSukunimenMukaan(List<Osallistuminen> lista, String filter) {
        ArrayList<Osallistuminen> kopio = new ArrayList<Osallistuminen>(lista);
        Collections.sort(kopio, new OsallistuminenSukunimenMukaan());
        for (Osallistuminen osallistuminen : lista) {
            if (!osallistuminen.getSukunimi().matches(filter + ".*")) {
                kopio.remove(osallistuminen);
            }
        }
        return kopio;
    }

    public static List filteroiOpiskelijanumeronMukaan(List<Osallistuminen> lista, String filter) {
        ArrayList<Osallistuminen> kopio = new ArrayList<Osallistuminen>(lista);
        String substring = filter.substring(1);
        for (Osallistuminen osallistuminen : lista) {
            if (!osallistuminen.getHetu().matches(substring + ".*")) {
                kopio.remove(osallistuminen);
            }
        }
        return kopio;
    }

    public static List filteroiAlkuLoppu(List<Osallistuminen> lista, String filter) {
        ArrayList<Osallistuminen> kopio = new ArrayList<Osallistuminen>(lista);
        Collections.sort(kopio, new OsallistuminenSukunimenMukaan());
        Collections.sort(lista, new OsallistuminenSukunimenMukaan());
        String[] sanat = filter.split("\\.\\.");
        if (filter.trim().charAt(0) == '.' && filter.trim().charAt(1) == '.' && sanat.length > 1) {
            System.out.println("Piste ekana ja tokana!");
            for (String string : sanat) {
                System.out.println("moi" + string);
            }
            Collections.sort(lista, new OsallistuminenSukunimenMukaanKaanteinen());
            for (Osallistuminen osallistuminen : lista) {
                if (!osallistuminen.getSukunimi().matches(sanat[1].trim() + ".*")) {
                    kopio.remove(osallistuminen);
                } else {
                    break;
                }
            }
            return kopio;
        } else if (sanat.length == 1) {
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

    public static List useaFilteri(List<Osallistuminen> lista, String filter) {
        String[] sanat = filter.split(",");
        ArrayList<Osallistuminen> kopio = new ArrayList<Osallistuminen>(lista);
        for (String string : sanat) {            
            if (StringUtils.isNumeric(string.trim())) {
                kopio = (ArrayList<Osallistuminen>) filteroiRyhmanMukaan(kopio, string.trim());
            } else {
                if (string.trim().contains("..")) {
                    kopio = (ArrayList<Osallistuminen>) filteroiAlkuLoppu(kopio, string.trim());
                }
                if (string.trim().charAt(0) != '#') {
                    kopio = (ArrayList<Osallistuminen>) filteroiSukunimenMukaan(kopio, string.trim());
                }
                if (string.trim().charAt(0) == '#') {
                    kopio = (ArrayList<Osallistuminen>) filteroiOpiskelijanumeronMukaan(kopio, string.trim());
                }
            }
        }
        return kopio;
    }

    public static List filteroi(List<Osallistuminen> lista, String filter) {
        if (filter.contains(",")) {            
            return useaFilteri(lista, filter);
        }
        if (StringUtils.isNumeric(filter)) {
            return filteroiRyhmanMukaan(lista, filter);
        } else {
            if (filter.contains("..")) {
                return filteroiAlkuLoppu(lista, filter);
            }
            if (filter.charAt(0) != '#') {
                return filteroiSukunimenMukaan(lista, filter);
            } else if (filter.charAt(0) == '#') {
                return filteroiOpiskelijanumeronMukaan(lista, filter);
            }
        }
        return lista;
    }
}
