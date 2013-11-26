/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.sorters;

import model.Osallistuminen;
import java.util.Comparator;

/**
 *
 * @author topisark
 */
public class OsallistuminenSukunimenMukaan implements Comparator<Osallistuminen>{

    @Override
    public int compare(Osallistuminen o1, Osallistuminen o2) {
        String sukunimi1 = o1.getSukunimi();
        String sukunimi2 = o2.getSukunimi();
        return sukunimi1.compareTo(sukunimi2);
    }
    
}
