    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.osasuoritus_rajat.OsasuoritusRajat;
import model.column.DateColumn;
import model.column.DecimalColumn;
import model.column.IntegerColumn;
import model.column.StringColumn;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author mkctammi
 */
public class Kurssi extends Table {

    public static final StringColumn kurssikoodi = new StringColumn("kurssikoodi", true); //ID
    public static final StringColumn lukukausi = new StringColumn("lukukausi", true); //ID
    public static final IntegerColumn lukuvuosi = new IntegerColumn("lukuvuosi", true); //ID
    public static final StringColumn tyyppi = new StringColumn("tyyppi", true); //ID
    public static final IntegerColumn kurssi_nro = new IntegerColumn("kurssi_nro", true); //ID
    public static final StringColumn kielikoodi = new StringColumn("kielikoodi");
    public static final StringColumn nimi = new StringColumn("nimi");
    public static final IntegerColumn opintoviikot = new IntegerColumn("opintoviikot");
    public static final IntegerColumn luentotunnit = new IntegerColumn("luentotunnit");
    public static final IntegerColumn luentokerta_lkm = new IntegerColumn("luentokerta_lkm");
    public static final IntegerColumn harjoitustunnit = new IntegerColumn("harjoitustunnit");
    public static final IntegerColumn laskarikerta_lkm = new IntegerColumn("laskarikerta_lkm");
    public static final StringColumn lyhenne = new StringColumn("lyhenne");
    public static final StringColumn salasana = new StringColumn("salasana");
    public static final DateColumn suoritus_pvm = new DateColumn("suoritus_pvm");
    public static final StringColumn tila = new StringColumn("tila");
    public static final DateColumn alkamis_pvm = new DateColumn("alkamis_pvm");
    public static final DateColumn paattymis_pvm = new DateColumn("paattymis_pvm");
    public static final DateColumn paivitys_pvm = new DateColumn("paivitys_pvm");
    public static final IntegerColumn max_osallistuja_lkm = new IntegerColumn("max_osallistuja_lkm");
    public static final StringColumn laskaritehtava_lkm = new StringColumn("laskaritehtava_lkm");
    public static final IntegerColumn pakolliset_laskarikerta_lkm = new IntegerColumn("pakolliset_laskarikerta_lkm");
    public static final IntegerColumn pakolliset_laskaritehtava_lkm = new IntegerColumn("pakolliset_laskaritehtava_lkm");
    public static final IntegerColumn max_laskaripisteet = new IntegerColumn("max_laskaripisteet");
    public static final StringColumn hyvaksytty_laskarilasnaolo = new StringColumn("hyvaksytty_laskarilasnaolo");
    public static final IntegerColumn lisapistealaraja = new IntegerColumn("lisapistealaraja");
    public static final StringColumn lisapisterajat = new StringColumn("lisapisterajat");
    public static final DecimalColumn lisapisteiden_askelkoko = new DecimalColumn("lisapisteiden_askelkoko");
    public static final IntegerColumn harjoitustyo_lkm = new IntegerColumn("harjoitustyo_lkm");
    public static final IntegerColumn pakolliset_harjoitustyo_lkm = new IntegerColumn("pakolliset_harjoitustyo_lkm");
    public static final IntegerColumn harjoitustyopisteet = new IntegerColumn("harjoitustyopisteet");
    public static final StringColumn max_harjoitustyopisteet = new StringColumn("max_harjoitustyopisteet");
    public static final StringColumn min_harjoitustyopisteet = new StringColumn("min_harjoitustyopisteet");
    public static final IntegerColumn min_harjoitustyopisteet_summa = new IntegerColumn("min_harjoitustyopisteet_summa");
    public static final StringColumn harjoitustyon_pisterajat = new StringColumn("harjoitustyon_pisterajat");
    public static final DecimalColumn harjoitustoiden_askelkoko = new DecimalColumn("harjoitustoiden_askelkoko");
    public static final IntegerColumn valikokeet_lkm = new IntegerColumn("valikokeet_lkm"); //Välikokeet tässä tarkoittaa siis kaikkia kokeita yhteensä
    public static final IntegerColumn pakolliset_koe_lkm = new IntegerColumn("pakolliset_koe_lkm");
    public static final StringColumn max_koepisteet = new StringColumn("max_koepisteet");
    public static final StringColumn min_koepisteet = new StringColumn("min_koepisteet");
    public static final IntegerColumn min_koepisteet_summa = new IntegerColumn("min_koepisteet_summa");
    public static final IntegerColumn min_yhteispisteet = new IntegerColumn("min_yhteispisteet");
    public static final DecimalColumn arvostelun_askelkoko = new DecimalColumn("arvostelun_askelkoko");
    public static final StringColumn arvosanarajat = new StringColumn("arvosanarajat");
    public static final StringColumn arvostellaanko = new StringColumn("arvostellaanko");
    public static final StringColumn kokonaistiedot = new StringColumn("kokonaistiedot");
    public static final StringColumn kuvaustieto1 = new StringColumn("kuvaustieto1");
    public static final StringColumn kuvaustieto2 = new StringColumn("kuvaustieto2");
    public static final StringColumn kuvaustieto3 = new StringColumn("kuvaustieto3");
    public static final StringColumn hakukysymykset = new StringColumn("hakukysymykset");
    public static final StringColumn suunnittelukommentti = new StringColumn("suunnittelukommentti");
    public static final StringColumn omistaja = new StringColumn("omistaja");
    public static final StringColumn peruttavissa = new StringColumn("peruttavissa");
    public static final IntegerColumn laskentakaava = new IntegerColumn("laskentakaava");
    public static final DateColumn arvostelu_pvm = new DateColumn("arvostelu_pvm");
    public static final DateColumn siirto_pvm = new DateColumn("siirto_pvm");
    public static final IntegerColumn ht_lisapistealaraja = new IntegerColumn("ht_lisapistealaraja");
    public static final StringColumn arvostelija = new StringColumn("arvostelija");
    public static final IntegerColumn opintoviikot_ylaraja = new IntegerColumn("opintoviikot_ylaraja");
    public static final IntegerColumn opintopisteet_ylaraja = new IntegerColumn("opintopisteet_ylaraja");
    public static final IntegerColumn opintopisteet = new IntegerColumn("opintopisteet");
    public static final IntegerColumn periodi = new IntegerColumn("periodi");
    public static final StringColumn kotisivu = new StringColumn("kotisivu");
    public static final IntegerColumn periodi2 = new IntegerColumn("periodi2");

    //<editor-fold defaultstate="collapsed" desc="setters">
    public boolean setLaskarikerta_lkm(String laskarikerta_lkm) {
        if (isInteger(laskarikerta_lkm)) {
            int lkm = Integer.parseInt(laskarikerta_lkm);
            if (lkm >= 0 && lkm <= 18) {
                this.setValue(Kurssi.laskarikerta_lkm, lkm);
                laskariRajat.setAktiivisia(lkm);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean setPakolliset_laskaritehtava_lkm(String pakolliset_laskaritehtava_lkm, Integer lh_lkm) {
        if (isInteger(pakolliset_laskaritehtava_lkm)) {
            int lkm = Integer.parseInt(pakolliset_laskaritehtava_lkm);
            if (lkm >= 0 && lkm <= 18 && lkm <= lh_lkm) {
                this.setValue(Kurssi.pakolliset_laskaritehtava_lkm, lkm);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean setMax_laskaripisteet(String max_laskaripisteet) {
        if (isInteger(max_laskaripisteet)) {
            int max = Integer.parseInt(max_laskaripisteet);
            if (max >= 0 && max <= 60) {
                this.setValue(Kurssi.max_laskaripisteet, max);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean setHarjoitustyo_lkm(String aHarjoitustyo_lkm) {
        if (isInteger(aHarjoitustyo_lkm)) {
            int lkm = Integer.parseInt(aHarjoitustyo_lkm);
            if (lkm >= 0 && lkm <= 18) {
                this.setValue(Kurssi.harjoitustyo_lkm, lkm);
                harjoitustyoRajat.setAktiivisia(lkm);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean setPakolliset_harjoitustyo_lkm(String aPakolliset_harjoitustyo_lkm, Integer ht_lkm) {
        if (isInteger(aPakolliset_harjoitustyo_lkm)) {
            int lkm = Integer.parseInt(aPakolliset_harjoitustyo_lkm);
            if (lkm >= 0 && lkm <= 18 && lkm <= ht_lkm) {
                this.setValue(Kurssi.pakolliset_harjoitustyo_lkm, lkm);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean setHarjoitustyopisteet(String aHarjoitustyopisteet) {
        if (isInteger(aHarjoitustyopisteet)) {
            int lkm = Integer.parseInt(aHarjoitustyopisteet);
            if (lkm >= 0 && lkm <= 60) {
                this.setValue(Kurssi.harjoitustyopisteet, lkm);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean setValikokeet_lkm(String aValikokeet_lkm) {
        if (isInteger(aValikokeet_lkm)) {
            int lkm = Integer.parseInt(aValikokeet_lkm);
            if (lkm >= 0 && lkm <= 18) {
                this.setValue(Kurssi.valikokeet_lkm, lkm);
                koeRajat.setAktiivisia(lkm);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean setPakolliset_koe_lkm(String aPakolliset_koe_lkm, Integer koe_lkm) {
        if (isInteger(aPakolliset_koe_lkm)) {
            int lkm = Integer.parseInt(aPakolliset_koe_lkm);
            if (lkm >= 0 && lkm <= 18 && lkm <= koe_lkm) {
                this.setValue(Kurssi.pakolliset_koe_lkm, lkm);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean setArvosteluasteikko(String arv_asteikko) {
        if (arv_asteikko.equals("K") || arv_asteikko.equals("E")) {
            this.setValue(Kurssi.arvostellaanko, arv_asteikko);
            return true;
        } else {
            return false;
        }
    }

    public void setSuoritus_pvm(Date suoritusPvm) {
        this.setValue(Kurssi.suoritus_pvm, suoritusPvm);
    }

    public void setPaattymis_pvm(Date paattymisPvm) {
        this.setValue(Kurssi.paattymis_pvm, paattymisPvm);
    }
    //</editor-fold>
    OsasuoritusRajat laskariRajat;
    OsasuoritusRajat harjoitustyoRajat;
    OsasuoritusRajat koeRajat;

    @Override
    public String getTableName() {
        return "kurssi";
    }

    @Override
    public Table getNewInstance() {
        return new Kurssi();
    }

    public OsasuoritusRajat getLaskariRajat() {
        if (laskariRajat == null) {
            laskariRajat = new OsasuoritusRajat(getHyvaksytty_laskarilasnaolo(), getLaskaritehtava_lkm(), getLaskarikerta_lkm());
        }
        return laskariRajat;
    }

    public OsasuoritusRajat getHarjoitustyoRajat() {
        if (harjoitustyoRajat == null) {
            harjoitustyoRajat = new OsasuoritusRajat(getMin_harjoitustyopisteet(), getMax_harjoitustyopisteet(), getHarjoitustyo_lkm());
        }
        return harjoitustyoRajat;
    }

    public OsasuoritusRajat getKoeRajat() {
        if (koeRajat == null) {
            koeRajat = new OsasuoritusRajat(getMin_koepisteet(), getMax_koepisteet(), getValikokeet_lkm());
        }
        return koeRajat;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Valmistaa kurssin tallennusta varten. Kurssin osasuoritusten rajat on
     * tulkittu apuolioiksi, joten ne täytyy ennen tallennusta muuttaa takaisin
     * tietokannassa olevaan muotoon.
     */
    @Override
    public void update() {
        //laskarirajat muutetaan
        this.setValue(Kurssi.hyvaksytty_laskarilasnaolo, getLaskariRajat().minArvotTietokantamuodossa());
        this.setValue(Kurssi.laskaritehtava_lkm, getLaskariRajat().maxArvotTietokantamuodossa());
        //htrajat vastaavasti
        this.setValue(Kurssi.min_harjoitustyopisteet, getHarjoitustyoRajat().minArvotTietokantamuodossa());
        this.setValue(Kurssi.max_harjoitustyopisteet, getHarjoitustyoRajat().maxArvotTietokantamuodossa());
        //koerajat vastaavasti
        this.setValue(Kurssi.min_koepisteet, getKoeRajat().minArvotTietokantamuodossa());
        this.setValue(Kurssi.max_koepisteet, getKoeRajat().maxArvotTietokantamuodossa());
    }

    /**
     * Palauttaa Stringin jota näytetään käyttäjälle kurssilistauksessa
     *
     * @return
     */
    public String listaString() {
        String alkamispvm = "";
        String jaassa = "";
        String koepvm = "";
        try {
            koepvm = ", koe " + new SimpleDateFormat("dd.MM.yy").format(getValue(Kurssi.paattymis_pvm));
        } catch (Exception e) {} //Päivä näytetään jos se on olemassa ja oikeassa formaatissa
        if (getValue(Kurssi.alkamis_pvm) != null) {
            alkamispvm = " [" + getValue(Kurssi.lukukausi) + getValue(Kurssi.alkamis_pvm).toString().substring(2, 4) + "]";
        }
        if (getValue(Kurssi.tila).equals("J")) {
            jaassa = "* ";
        }
        return jaassa + getValue(Kurssi.nimi) + koepvm + alkamispvm;
    }

    /**
     * Palauttaa Stringin joka koostetaan taulun ensisijaisista avaimista, se on
     * siis uniikki jokaiselle Kurssi -oliolle.
     *
     * @return
     */
    public String idString() {
        return getValue(Kurssi.kurssikoodi)
                + "." + getValue(Kurssi.lukukausi)
                + "." + getValue(Kurssi.lukuvuosi)
                + "." + getValue(Kurssi.tyyppi)
                + "." + getValue(Kurssi.kurssi_nro);
    }
    
    public boolean isJaassa() {
        return this.getTila().equals("J");
    }

    //<editor-fold defaultstate="collapsed" desc="getters">
    public String getKurssikoodi() {
        return getValue(Kurssi.kurssikoodi);
    }

    public String getLukukausi() {
        return getValue(Kurssi.lukukausi);
    }

    public Integer getLukuvuosi() {
        return getValue(Kurssi.lukuvuosi);
    }

    public String getTyyppi() {
        return getValue(Kurssi.tyyppi);
    }

    public Integer getKurssi_nro() {
        return getValue(Kurssi.kurssi_nro);
    }

    public String getKielikoodi() {
        return getValue(Kurssi.kielikoodi);
    }

    public String getNimi() {
        return getValue(Kurssi.nimi);
    }

    public Integer getOpintoviikot() {
        return getValue(Kurssi.opintoviikot);
    }

    public Integer getLuentotunnit() {
        return getValue(Kurssi.luentotunnit);
    }

    public Integer getLuentokerta_lkm() {
        return getValue(Kurssi.luentokerta_lkm);
    }

    public Integer getHarjoitustunnit() {
        return getValue(Kurssi.harjoitustunnit);
    }

    public Integer getLaskarikerta_lkm() {
        return getValue(Kurssi.laskarikerta_lkm);
    }

    public String getLyhenne() {
        return getValue(Kurssi.lyhenne);
    }

    public String getSalasana() {
        return getValue(Kurssi.salasana);
    }

    public Date getSuoritus_pvm() {
        return getValue(Kurssi.suoritus_pvm);
    }

    public String getTila() {
        return getValue(Kurssi.tila);
    }

    public Date getAlkamis_pvm() {
        return getValue(Kurssi.alkamis_pvm);
    }

    public Date getPaattymis_pvm() {
        return getValue(Kurssi.paattymis_pvm);
    }

    public Date getPaivitys_pvm() {
        return getValue(Kurssi.paivitys_pvm);
    }

    public Integer getMax_osallistuja_lkm() {
        return getValue(Kurssi.max_osallistuja_lkm);
    }

    public String getLaskaritehtava_lkm() {
        return getValue(Kurssi.laskaritehtava_lkm);
    }

    public Integer getPakolliset_laskarikerta_lkm() {
        return getValue(Kurssi.pakolliset_laskarikerta_lkm);
    }

    public Integer getPakolliset_laskaritehtava_lkm() {
        return getValue(Kurssi.pakolliset_laskaritehtava_lkm);
    }

    public Integer getMax_laskaripisteet() {
        return getValue(Kurssi.max_laskaripisteet);
    }

    public String getHyvaksytty_laskarilasnaolo() {
        return getValue(Kurssi.hyvaksytty_laskarilasnaolo);
    }

    public Integer getLisapistealaraja() {
        return getValue(Kurssi.lisapistealaraja);
    }

    public String getLisapisterajat() {
        return getValue(Kurssi.lisapisterajat);
    }

    public Float getLisapisteiden_askelkoko() {
        return getValue(Kurssi.lisapisteiden_askelkoko);
    }

    public Integer getHarjoitustyo_lkm() {
        return getValue(Kurssi.harjoitustyo_lkm);
    }

    public Integer getPakolliset_harjoitustyo_lkm() {
        return getValue(Kurssi.pakolliset_harjoitustyo_lkm);
    }

    public Integer getHarjoitustyopisteet() {
        return getValue(Kurssi.harjoitustyopisteet);
    }

    public String getMax_harjoitustyopisteet() {
        return getValue(Kurssi.max_harjoitustyopisteet);
    }

    public String getMin_harjoitustyopisteet() {
        return getValue(Kurssi.min_harjoitustyopisteet);
    }

    public Integer getMin_harjoitustyopisteet_summa() {
        return getValue(Kurssi.min_harjoitustyopisteet_summa);
    }

    public String getHarjoitustyon_pisterajat() {
        return getValue(Kurssi.harjoitustyon_pisterajat);
    }

    public Float getHarjoitustoiden_askelkoko() {
        return getValue(Kurssi.harjoitustoiden_askelkoko);
    }

    public Integer getValikokeet_lkm() {
        return getValue(Kurssi.valikokeet_lkm);
    }

    public Integer getPakolliset_koe_lkm() {
        return getValue(Kurssi.pakolliset_koe_lkm);
    }

    public String getMax_koepisteet() {
        return getValue(Kurssi.max_koepisteet);
    }

    public String getMin_koepisteet() {
        return getValue(Kurssi.min_koepisteet);
    }

    public Integer getMin_koepisteet_summa() {
        return getValue(Kurssi.min_koepisteet_summa);
    }

    public Integer getMin_yhteispisteet() {
        return getValue(Kurssi.min_yhteispisteet);
    }

    public Float getArvostelun_askelkoko() {
        return getValue(Kurssi.arvostelun_askelkoko);
    }

    public String getArvosanarajat() {
        return getValue(Kurssi.arvosanarajat);
    }

    public String getArvostellaanko() {
        return getValue(Kurssi.arvostellaanko);
    }

    public String getKokonaistiedot() {
        return getValue(Kurssi.kokonaistiedot);
    }

    public String getKuvaustieto1() {
        return getValue(Kurssi.kuvaustieto1);
    }

    public String getKuvaustieto2() {
        return getValue(Kurssi.kuvaustieto2);
    }

    public String getKuvaustieto3() {
        return getValue(Kurssi.kuvaustieto3);
    }

    public String getHakukysymykset() {
        return getValue(Kurssi.hakukysymykset);
    }

    public String getSuunnittelukommentti() {
        return getValue(Kurssi.suunnittelukommentti);
    }

    public String getOmistaja() {
        return getValue(Kurssi.omistaja);
    }

    public String getPeruttavissa() {
        return getValue(Kurssi.peruttavissa);
    }

    public Integer getLaskentakaava() {
        return getValue(Kurssi.laskentakaava);
    }

    public Date getArvostelu_pvm() {
        return getValue(Kurssi.arvostelu_pvm);
    }

    public Date getSiirto_pvm() {
        return getValue(Kurssi.siirto_pvm);
    }

    public Integer getHt_lisapistealaraja() {
        return getValue(Kurssi.ht_lisapistealaraja);
    }

    public String getArvostelija() {
        return getValue(Kurssi.arvostelija);
    }

    public Integer getOpintoviikot_ylaraja() {
        return getValue(Kurssi.opintoviikot_ylaraja);
    }

    public Integer getOpintopisteet_ylaraja() {
        return getValue(Kurssi.opintopisteet_ylaraja);
    }

    public Integer getOpintopisteet() {
        return getValue(Kurssi.opintopisteet);
    }

    public Integer getPeriodi() {
        return getValue(Kurssi.periodi);
    }

    public String getKotisivu() {
        return getValue(Kurssi.kotisivu);
    }

    public Integer getPeriodi2() {
        return getValue(Kurssi.periodi2);
    }
    //</editor-fold>
}