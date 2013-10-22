/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model;

import debug.model.osasuoritukset.Osasuoritukset;
import debug.ApplicationException;
import debug.model.column.IntegerColumn;
import debug.model.column.StringColumn;
import debug.model.column.TimestampColumn;
import debug.model.util.Table;
import java.sql.Timestamp;

/**
 *
 * @author mkctammi
 */
public class Osallistuminen extends Table {

    private Kurssi kurssi; //kurssi johon tämä osallistuminen kuuluu
    private Osasuoritukset laskarit;
    private Osasuoritukset kokeet;
    private Osasuoritukset harjoitustyot;
    public static final StringColumn personid = new StringColumn("personid");
    public static final StringColumn kurssikoodi = new StringColumn("kurssikoodi", true); //Foreign Key Kurssi
    public static final StringColumn lukukausi = new StringColumn("lukukausi", true); //Foreign Key Kurssi
    public static final IntegerColumn lukuvuosi = new IntegerColumn("lukuvuosi", true); //Foreign Key Kurssi
    public static final StringColumn tyyppi = new StringColumn("tyyppi", true); //Foreign Key Kurssi
    public static final IntegerColumn kurssi_nro = new IntegerColumn("kurssi_nro", true); //Foreign Key Kurssi
    public static final IntegerColumn ryhma_nro = new IntegerColumn("ryhma_nro"); //Tääki on joku avain?
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
    public static final StringColumn hetu = new StringColumn("hetu", true); //Foreign Key Oppilas
    public static final TimestampColumn kypsyys_pvm = new TimestampColumn("kypsyys_pvm");
    public static final StringColumn tenttija = new StringColumn("tenttija");
    public static final StringColumn kielikoodi = new StringColumn("kielikoodi");
    private String nimi;
    private String etunimi;
    private String sukunimi;
    private String email;

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getNimi() {
        return nimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    public String getEtunimi() {
        return etunimi;
    }

    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public void setEmail(String email) {
        if (email == null) {
            this.email = "undefined";
        } else {
            this.email = email;
        }
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getTableName() {
        return "osallistuminen";
    }

    @Override
    public Table getNewInstance() {
        return new Osallistuminen();
    }

    public void setKurssi(Kurssi kurssi) {
        this.kurssi = kurssi;
    }

    public Osasuoritukset getLaskarit() {
        if (kurssi == null) {
            throw new ApplicationException("Kurssi täytyy asettaa osallistumiselle sen luonnin jälkeen");
        }
        if (laskarit == null) {
            laskarit = new Osasuoritukset(kurssi.getLaskaritehtava_lkm(), getLaskarisuoritukset(), kurssi.getLaskarikerta_lkm());
        }
        return laskarit;
    }

    public Osasuoritukset getHarjoitustyot() {
        if (kurssi == null) {
            throw new ApplicationException("Kurssi täytyy asettaa osallistumiselle sen luonnin jälkeen");
        }
        if (harjoitustyot == null) {
            harjoitustyot = new Osasuoritukset(kurssi.getMax_harjoitustyopisteet(), getHarjoitustyopisteet(), kurssi.getHarjoitustyo_lkm());
        }
        return harjoitustyot;
    }

    public Osasuoritukset getKokeet() {
        if (kurssi == null) {
            throw new ApplicationException("Kurssi täytyy asettaa osallistumiselle sen luonnin jälkeen");
        }
        if (kokeet == null) {
            kokeet = new Osasuoritukset(kurssi.getMax_koepisteet(), getKoepisteet(), kurssi.getValikokeet_lkm());
        }
        return kokeet;
    }

    //<editor-fold defaultstate="collapsed" desc="getterit">
    public String getPersonid() {
        return getValue(Osallistuminen.personid);
    }

    public String getKurssikoodi() {
        return getValue(Osallistuminen.kurssikoodi);
    }

    public String getLukukausi() {
        return getValue(Osallistuminen.lukukausi);
    }

    public Integer getLukuvuosi() {
        return getValue(Osallistuminen.lukuvuosi);
    }

    public String getTyyppi() {
        return getValue(Osallistuminen.tyyppi);
    }

    public Integer getKurssi_nro() {
        return getValue(Osallistuminen.kurssi_nro);
    }

    public Integer getRyhma_nro() {
        return getValue(Osallistuminen.ryhma_nro);
    }

    public String getKommentti_1() {
        return getValue(Osallistuminen.kommentti_1);
    }

    public String getKommentti_2() {
        return getValue(Osallistuminen.kommentti_2);
    }

    public Integer getLaskari_lasnaolo_lkm() {
        return getValue(Osallistuminen.laskari_lasnaolo_lkm);
    }

    public String getLaskarisuoritukset() {
        return getValue(Osallistuminen.laskarisuoritukset);
    }

    public Integer getLaskarisuoritukset_summa() {
        return getValue(Osallistuminen.laskarisuoritukset_summa);
    }

    public Integer getLaskarihyvitys() {
        return getValue(Osallistuminen.laskarihyvitys);
    }

    public Integer getHarjoitustyo_lasnaolo_lkm() {
        return getValue(Osallistuminen.harjoitustyo_lasnaolo_lkm);
    }

    public String getHarjoitustyopisteet() {
        return getValue(Osallistuminen.harjoitustyopisteet);
    }

    public Integer getHarjoitustyo_summa() {
        return getValue(Osallistuminen.harjoitustyo_summa);
    }

    public Integer getHarjoitustyohyvitys() {
        return getValue(Osallistuminen.harjoitustyohyvitys);
    }

    public String getKoepisteet() {
        return getValue(Osallistuminen.koepisteet);
    }

    public Integer getKoepisteet_summa() {
        return getValue(Osallistuminen.koepisteet_summa);
    }

    public Integer getYhteispisteet() {
        return getValue(Osallistuminen.yhteispisteet);
    }

    public String getArvosana() {
        return getValue(Osallistuminen.arvosana);
    }

    public Timestamp getIlmoittautumis_pvm() {
        return getValue(Osallistuminen.ilmoittautumis_pvm);
    }

    public String getVoimassa() {
        return getValue(Osallistuminen.voimassa);
    }

    public Timestamp getViimeinen_kasittely_pvm() {
        return getValue(Osallistuminen.viimeinen_kasittely_pvm);
    }

    public Integer getIlmo_jnro() {
        return getValue(Osallistuminen.ilmo_jnro);
    }

    public String getJaassa() {
        return getValue(Osallistuminen.jaassa);
    }

    public Integer getLaajuus_ov() {
        return getValue(Osallistuminen.laajuus_ov);
    }

    public Integer getLaajuus_op() {
        return getValue(Osallistuminen.laajuus_op);
    }

    public String getHetu() {
        return getValue(Osallistuminen.hetu);
    }

    public Timestamp getKypsyys_pvm() {
        return getValue(Osallistuminen.kypsyys_pvm);
    }

    public String getTenttija() {
        return getValue(Osallistuminen.tenttija);
    }

    public String getKielikoodi() {
        return getValue(Osallistuminen.kielikoodi);
    }
    //</editor-fold>
}
