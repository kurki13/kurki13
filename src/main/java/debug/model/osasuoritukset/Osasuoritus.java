/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.osasuoritukset;

import debug.ApplicationException;

/**
 * Tämä luokka näyttää Osallistuminen -olion yhden osasuorituksen. 
 * Sitä käytetään Osasuoritukset -olion avulla.
 * @author mkctammi
 */
public class Osasuoritus {

    private int pisteet;
    private int maxPisteet;

    public Osasuoritus(int pisteet, int maxPisteet) {
        if (pisteet > maxPisteet) {
            pisteet = maxPisteet;
        }
        if (pisteet < 0) {
            pisteet = Muotoilija.EMPTY;
        }
        this.pisteet = pisteet;
        this.maxPisteet = maxPisteet;
    }

    public int getPisteet() {
        return pisteet;
    }

    public boolean setPisteet(int pisteet) {
        if (pisteet > maxPisteet || pisteet < 0 || pisteet > Muotoilija.MAX_PISTE) {
            return false;
        }
        this.pisteet = pisteet;
        return true;
    }

    public void poistaPisteet() {
        this.pisteet = Muotoilija.EMPTY;
    }

    public int getMaxPisteet() {
        return maxPisteet;
    }
}
