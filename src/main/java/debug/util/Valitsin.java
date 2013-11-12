/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.util;

import java.util.ArrayList;

/**
 *
 * @author tkairola
 */
public class Valitsin {
    
    private ArrayList<String> sallitut = new ArrayList<String>();
    private String kuvaus;
    
    /*
     * Tarkista onko arvo sallittu
     */
    public boolean loytyykoArvo(String arvo) {
        return sallitut.contains(arvo);
    }
    
    /*
     * Lisää sallitut-listaan kaikki arvot väliltä alku ja loppu
     */
    public void lisaaVali(int alku, int loppu) {
        for (int i = alku; i <= loppu; i++) {
            sallitut.add(i+"");
        }
    }
    
    /*
     * Lisää yhden tai useamman arvon sallitut-listaan
     */
    public void lisaaArvot(String... arvot) {
        for (String string : arvot) {
            sallitut.add(string);
        }
    }
    
    /*
     * Palauttaa sallitut arvot arraylistinä
     */
    public ArrayList<String> getSallitut() {
        return sallitut;
    }
    
}
