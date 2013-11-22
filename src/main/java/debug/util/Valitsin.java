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
    private ArrayList<String> kielikoodit = new ArrayList<String>();
    
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
     * Lisää yhden tai useamman arvon sallitut-listaan
     */
    public void lisaaArvotListasta(ArrayList<String> lista) {
        for (String string : lista) {
            sallitut.add(string);
        }
    }
    
    /*
     * Palauttaa sallitut arvot arraylistinä
     */
    public ArrayList<String> getSallitut() {
        return sallitut;
    }
    
    /*
     * Lisätään tietokantaan tallennettavat kielikoodit (sekä tyhjä-vaihtoehto)
     * Ja palautetaan
     */
    public ArrayList<String> getKielet() {
        kielikoodit.add("");
        kielikoodit.add("S");
        kielikoodit.add("E");
        kielikoodit.add("R");
        
        return kielikoodit;
    }
    
    public String getSallitutJS() {
        StringBuilder ret = new StringBuilder();
        ret.append("[");
        for (String string : sallitut) {
            ret.append("'").append(string).append("'");
            ret.append(",");
        }
        if (!sallitut.isEmpty()) {
            ret.deleteCharAt(ret.length()-1);
        }
        ret.append("]");
        return ret.toString();
    }
    
}
