/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model;

import debug.model.column.DateColumn;
import debug.model.column.IntegerColumn;
import debug.model.column.StringColumn;
import debug.model.column.TimestampColumn;
import debug.model.util.Table;

/**
 *
 * @author mkctammi
 */
public class Osallistuminen extends Table {
        public static final StringColumn personid = new StringColumn("personid");
        public static final StringColumn kurssikoodi = new StringColumn("kurssikoodi");
        public static final StringColumn lukukausi = new StringColumn("lukukausi");
        public static final IntegerColumn lukuvuosi = new IntegerColumn("lukuvuosi");
        public static final StringColumn tyyppi = new StringColumn("tyyppi");
        public static final IntegerColumn kurssi_nro = new IntegerColumn("kurssi_nro");
        public static final IntegerColumn ryhma_nro = new IntegerColumn("ryhma_nro");
        public static final StringColumn kommentti_1 = new StringColumn("kommentti_1");
        public static final StringColumn kommentti_2 = new StringColumn("kommentti_2");
        public static final IntegerColumn laskari_lasnaolo_lkm = new IntegerColumn("laskari_lasnaolo_lkm");
        public static final StringColumn laskarisuoritukset = new StringColumn("laskarisuoritukset");
        public static final IntegerColumn laskarisuoritukset_summa = new IntegerColumn("laskarisuoritukset_summa");
        public static final IntegerColumn laskarihyvitys = new IntegerColumn("laskarihyvitys");
        public static final IntegerColumn harjoitustyo_lasnaolo_lkm = new IntegerColumn("harjoitustyo_lasnaolo_lkm");
        public static final StringColumn harjoitustyopisteet = new StringColumn("harjoitustyopisteet");
        public static final IntegerColumn harjoitustyo_summa = new IntegerColumn("harjoitustyo_summa");
        public static final IntegerColumn harjoitustyohyvitys = new IntegerColumn("harjoitustyohyvitys");
        public static final StringColumn koepisteet = new StringColumn("koepisteet");
        public static final IntegerColumn koepisteet_summa = new IntegerColumn("koepisteet_summa");
        public static final IntegerColumn yhteispisteet = new IntegerColumn("yhteispisteet");
        public static final StringColumn arvosana = new StringColumn("arvosana");
        public static final TimestampColumn ilmoittautumis_pvm = new TimestampColumn("ilmoittautumis_pvm");
        public static final StringColumn voimassa = new StringColumn("voimassa");
        public static final TimestampColumn viimeinen_kasittely_pvm = new TimestampColumn("viimeinen_kasittely_pvm");
        public static final IntegerColumn ilmo_jnro = new IntegerColumn("ilmo_jnro");
        public static final StringColumn jaassa = new StringColumn("jaassa");
        public static final IntegerColumn laajuus_ov = new IntegerColumn("laajuus_ov");
        public static final IntegerColumn laajuus_op = new IntegerColumn("laajuus_op");
        public static final StringColumn hetu = new StringColumn("hetu");
        public static final TimestampColumn kypsyys_pvm = new TimestampColumn("kypsyys_pvm");
        public static final StringColumn tenttija = new StringColumn("tenttija");
        public static final StringColumn kielikoodi = new StringColumn("kielikoodi");
        
    @Override
    public String getTableName() {
        return "osallistuminen";
    }

    @Override
    public Table getNewInstance() {
        return new Osallistuminen();
    }

    //<editor-fold defaultstate="collapsed" desc="getterit">
    /**
     * @return the personid
     */
    public static StringColumn getPersonid() {
        return personid;
    }

    /**
     * @return the kurssikoodi
     */
    public static StringColumn getKurssikoodi() {
        return kurssikoodi;
    }

    /**
     * @return the lukukausi
     */
    public static StringColumn getLukukausi() {
        return lukukausi;
    }

    /**
     * @return the lukuvuosi
     */
    public static IntegerColumn getLukuvuosi() {
        return lukuvuosi;
    }

    /**
     * @return the tyyppi
     */
    public static StringColumn getTyyppi() {
        return tyyppi;
    }

    /**
     * @return the kurssi_nro
     */
    public static IntegerColumn getKurssi_nro() {
        return kurssi_nro;
    }

    /**
     * @return the ryhma_nro
     */
    public static IntegerColumn getRyhma_nro() {
        return ryhma_nro;
    }

    /**
     * @return the kommentti_1
     */
    public static StringColumn getKommentti_1() {
        return kommentti_1;
    }

    /**
     * @return the kommentti_2
     */
    public static StringColumn getKommentti_2() {
        return kommentti_2;
    }

    /**
     * @return the laskari_lasnaolo_lkm
     */
    public static IntegerColumn getLaskari_lasnaolo_lkm() {
        return laskari_lasnaolo_lkm;
    }

    /**
     * @return the laskarisuoritukset
     */
    public static StringColumn getLaskarisuoritukset() {
        return laskarisuoritukset;
    }

    /**
     * @return the laskarisuoritukset_summa
     */
    public static IntegerColumn getLaskarisuoritukset_summa() {
        return laskarisuoritukset_summa;
    }

    /**
     * @return the laskarihyvitys
     */
    public static IntegerColumn getLaskarihyvitys() {
        return laskarihyvitys;
    }

    /**
     * @return the harjoitustyo_lasnaolo_lkm
     */
    public static IntegerColumn getHarjoitustyo_lasnaolo_lkm() {
        return harjoitustyo_lasnaolo_lkm;
    }

    /**
     * @return the harjoitustyopisteet
     */
    public static StringColumn getHarjoitustyopisteet() {
        return harjoitustyopisteet;
    }

    /**
     * @return the harjoitustyo_summa
     */
    public static IntegerColumn getHarjoitustyo_summa() {
        return harjoitustyo_summa;
    }

    /**
     * @return the harjoitustyohyvitys
     */
    public static IntegerColumn getHarjoitustyohyvitys() {
        return harjoitustyohyvitys;
    }

    /**
     * @return the koepisteet
     */
    public static StringColumn getKoepisteet() {
        return koepisteet;
    }

    /**
     * @return the koepisteet_summa
     */
    public static IntegerColumn getKoepisteet_summa() {
        return koepisteet_summa;
    }

    /**
     * @return the yhteispisteet
     */
    public static IntegerColumn getYhteispisteet() {
        return yhteispisteet;
    }

    /**
     * @return the arvosana
     */
    public static StringColumn getArvosana() {
        return arvosana;
    }

    /**
     * @return the ilmoittautumis_pvm
     */
    public static TimestampColumn getIlmoittautumis_pvm() {
        return ilmoittautumis_pvm;
    }

    /**
     * @return the voimassa
     */
    public static StringColumn getVoimassa() {
        return voimassa;
    }

    /**
     * @return the viimeinen_kasittely_pvm
     */
    public static TimestampColumn getViimeinen_kasittely_pvm() {
        return viimeinen_kasittely_pvm;
    }

    /**
     * @return the ilmo_jnro
     */
    public static IntegerColumn getIlmo_jnro() {
        return ilmo_jnro;
    }

    /**
     * @return the jaassa
     */
    public static StringColumn getJaassa() {
        return jaassa;
    }

    /**
     * @return the laajuus_ov
     */
    public static IntegerColumn getLaajuus_ov() {
        return laajuus_ov;
    }

    /**
     * @return the laajuus_op
     */
    public static IntegerColumn getLaajuus_op() {
        return laajuus_op;
    }

    /**
     * @return the hetu
     */
    public static StringColumn getHetu() {
        return hetu;
    }

    /**
     * @return the kypsyys_pvm
     */
    public static TimestampColumn getKypsyys_pvm() {
        return kypsyys_pvm;
    }

    /**
     * @return the tenttija
     */
    public static StringColumn getTenttija() {
        return tenttija;
    }

    /**
     * @return the kielikoodi
     */
    public static StringColumn getKielikoodi() {
        return kielikoodi;
    }
    //</editor-fold>
}
