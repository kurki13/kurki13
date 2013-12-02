/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.sorters;

import model.Osallistuminen;
import java.util.Collections;
import java.util.List;

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
}
