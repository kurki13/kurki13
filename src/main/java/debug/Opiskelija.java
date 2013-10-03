/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author mkctammi
 */
public class Opiskelija { //Täs on jo kaikki
    
    String hetu; // ID
    String personid;
    String etunimi;
    String sukunimi;
    String entinen_sukunimi;
    String osoite;
    String puhelin;
    String sahkopostiosoite;
    String paa_aine;
    int aloitusvuosi;
    Timestamp kaytto_pvm;
    String opnro;
    String lupa;
    String vinkki;
    String varmenne;

    public Opiskelija(String hetu, String personid, String etunimi, String sukunimi, String entinen_sukunimi, String osoite, String puhelin, String sahkopostiosoite, String paa_aine, int aloitusvuosi, Timestamp kaytto_pvm, String opnro, String lupa, String vinkki, String varmenne) {
        this.hetu = hetu;
        this.personid = personid;
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.entinen_sukunimi = entinen_sukunimi;
        this.osoite = osoite;
        this.puhelin = puhelin;
        this.sahkopostiosoite = sahkopostiosoite;
        this.paa_aine = paa_aine;
        this.aloitusvuosi = aloitusvuosi;
        this.kaytto_pvm = kaytto_pvm;
        this.opnro = opnro;
        this.lupa = lupa;
        this.vinkki = vinkki;
        this.varmenne = varmenne;
    }
    
    
}
