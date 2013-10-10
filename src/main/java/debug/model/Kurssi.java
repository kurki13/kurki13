/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model;

import debug.model.column.DateColumn;
import debug.model.column.IntegerColumn;
import debug.model.column.StringColumn;
import debug.model.util.Filter;
import debug.model.util.SQLoader;
import debug.model.util.Table;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mkctammi
 */
public class Kurssi extends Table {

     public static final StringColumn kurssikoodi = new StringColumn("kurssikoodi");
     public static final StringColumn lukukausi = new StringColumn("lukukausi");
     public static final IntegerColumn lukuvuosi = new IntegerColumn("lukuvuosi");
     public static final StringColumn tyyppi = new StringColumn("tyyppi");
     public static final IntegerColumn kurssi_nro = new IntegerColumn("kurssi_nro");
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
     public static final IntegerColumn pakolliset_laskaritehtava_lkm = new IntegerColumn("pakolliset_laskarikerta_lkm");
     public static final IntegerColumn max_laskaripisteet = new IntegerColumn("max_laskaripisteet");
     public static final StringColumn hyvaksytty_laskarilasnaolo = new StringColumn("hyvaksytty_laskarilasnaolo");
     public static final IntegerColumn lisapistealaraja = new IntegerColumn("lisapistealaraja");
     public static final StringColumn lisapisterajat = new StringColumn("lisapisterajat");
     public static final IntegerColumn lisapisteiden_askelkoko = new IntegerColumn("lisapisteiden_askelkoko");
     public static final IntegerColumn harjoitustyo_lkm = new IntegerColumn("harjoitustyo_lkm");
     public static final IntegerColumn pakolliset_harjoitustyo_lkm = new IntegerColumn("pakolliset_harjoitustyo_lkm");
     public static final IntegerColumn harjoitustyopisteet = new IntegerColumn("harjoitustyopisteet");
     public static final StringColumn max_harjoitustyopisteet = new StringColumn("max_harjoitustyopisteet");
     public static final StringColumn min_harjoitustyopisteet = new StringColumn("min_harjoitustyopisteet");
     public static final IntegerColumn min_harjoitustyopisteet_summa = new IntegerColumn("min_harjoitustyopisteet_summa");
     public static final StringColumn harjoitustyon_pisterajat = new StringColumn("harjoitustyon_pisterajat");
     public static final IntegerColumn harjoitustoiden_askelkoko = new IntegerColumn("harjoitustoiden_askelkoko");
     public static final IntegerColumn valikokeet_lkm = new IntegerColumn("valikokeet_lkm");
     public static final IntegerColumn pakolliset_koe_lkm = new IntegerColumn("pakolliset_koe_lkm");
     public static final StringColumn max_koepisteet = new StringColumn("max_koepisteet");
     public static final StringColumn min_koepisteet = new StringColumn("min_koepisteet");
     public static final IntegerColumn min_koepisteet_summa = new IntegerColumn("min_koepisteet_summa");
     public static final IntegerColumn min_yhteispisteet = new IntegerColumn("min_yhteispisteet");
     public static final IntegerColumn arvostelun_askelkoko = new IntegerColumn("arvostelun_askelkoko");
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

    //<editor-fold defaultstate="collapsed" desc="getters">
    public String getKurssikoodi() {
        return this.getValue(Kurssi.kurssikoodi);
    }

    public String getLukukausi() {
        return this.getValue(Kurssi.lukukausi);
    }

    public Integer getLukuvuosi() {
        return this.getValue(Kurssi.lukuvuosi);
    }

    public String getTyyppi() {
        return this.getValue(Kurssi.tyyppi);
    }

    public Integer getKurssi_nro() {
        return this.getValue(Kurssi.kurssi_nro);
    }

    public String getKielikoodi() {
        return this.getValue(Kurssi.kielikoodi);
    }

    public String getNimi() {
        return this.getValue(Kurssi.nimi);
    }

    public Integer getOpintoviikot() {
        return this.getValue(Kurssi.opintoviikot);
    }

    public Integer getLuentotunnit() {
        return this.getValue(Kurssi.luentotunnit);
    }

    public Integer getLuentokerta_lkm() {
        return this.getValue(Kurssi.luentokerta_lkm);
    }

    public Integer getHarjoitustunnit() {
        return this.getValue(Kurssi.harjoitustunnit);
    }

    public Integer getLaskarikerta_lkm() {
        return this.getValue(Kurssi.laskarikerta_lkm);
    }

    public String getLyhenne() {
        return this.getValue(Kurssi.lyhenne);
    }

    public String getSalasana() {
        return this.getValue(Kurssi.salasana);
    }

    public Date getSuoritus_pvm() {
        return this.getValue(Kurssi.suoritus_pvm);
    }

    public String getTila() {
        return this.getValue(Kurssi.tila);
    }

    public Date getAlkamis_pvm() {
        return this.getValue(Kurssi.alkamis_pvm);
    }

    public Date getPaattymis_pvm() {
        return this.getValue(Kurssi.paattymis_pvm);
    }

    public Date getPaivitys_pvm() {
        return this.getValue(Kurssi.paivitys_pvm);
    }

    public Integer getMax_osallistuja_lkm() {
        return this.getValue(Kurssi.max_osallistuja_lkm);
    }

    public String getLaskaritehtava_lkm() {
        return this.getValue(Kurssi.laskaritehtava_lkm);
    }

    public Integer getPakolliset_laskarikerta_lkm() {
        return this.getValue(Kurssi.pakolliset_laskarikerta_lkm);
    }

    public Integer getPakolliset_laskaritehtava_lkm() {
        return this.getValue(Kurssi.pakolliset_laskaritehtava_lkm);
    }

    public Integer getMax_laskaripisteet() {
        return this.getValue(Kurssi.max_laskaripisteet);
    }

    public String getHyvaksytty_laskarilasnaolo() {
        return this.getValue(Kurssi.hyvaksytty_laskarilasnaolo);
    }

    public Integer getLisapistealaraja() {
        return this.getValue(Kurssi.lisapistealaraja);
    }

    public String getLisapisterajat() {
        return this.getValue(Kurssi.lisapisterajat);
    }

    public Integer getLisapisteiden_askelkoko() {
        return this.getValue(Kurssi.lisapisteiden_askelkoko);
    }

    public Integer getHarjoitustyo_lkm() {
        return this.getValue(Kurssi.harjoitustyo_lkm);
    }

    public Integer getPakolliset_harjoitustyo_lkm() {
        return this.getValue(Kurssi.pakolliset_harjoitustyo_lkm);
    }

    public Integer getHarjoitustyopisteet() {
        return this.getValue(Kurssi.harjoitustyopisteet);
    }

    public String getMax_harjoitustyopisteet() {
        return this.getValue(Kurssi.max_harjoitustyopisteet);
    }

    public String getMin_harjoitustyopisteet() {
        return this.getValue(Kurssi.min_harjoitustyopisteet);
    }

    public Integer getMin_harjoitustyopisteet_summa() {
        return this.getValue(Kurssi.min_harjoitustyopisteet_summa);
    }

    public String getHarjoitustyon_pisterajat() {
        return this.getValue(Kurssi.harjoitustyon_pisterajat);
    }

    public Integer getHarjoitustoiden_askelkoko() {
        return this.getValue(Kurssi.harjoitustoiden_askelkoko);
    }

    public Integer getValikokeet_lkm() {
        return this.getValue(Kurssi.valikokeet_lkm);
    }

    public Integer getPakolliset_koe_lkm() {
        return this.getValue(Kurssi.pakolliset_koe_lkm);
    }

    public String getMax_koepisteet() {
        return this.getValue(Kurssi.max_koepisteet);
    }

    public String getMin_koepisteet() {
        return this.getValue(Kurssi.min_koepisteet);
    }

    public Integer getMin_koepisteet_summa() {
        return this.getValue(Kurssi.min_koepisteet_summa);
    }

    public Integer getMin_yhteispisteet() {
        return this.getValue(Kurssi.min_yhteispisteet);
    }

    public Integer getArvostelun_askelkoko() {
        return this.getValue(Kurssi.arvostelun_askelkoko);
    }

    public String getArvosanarajat() {
        return this.getValue(Kurssi.arvosanarajat);
    }

    public String getArvostellaanko() {
        return this.getValue(Kurssi.arvostellaanko);
    }

    public String getKokonaistiedot() {
        return this.getValue(Kurssi.kokonaistiedot);
    }

    public String getKuvaustieto1() {
        return this.getValue(Kurssi.kuvaustieto1);
    }

    public String getKuvaustieto2() {
        return this.getValue(Kurssi.kuvaustieto2);
    }

    public String getKuvaustieto3() {
        return this.getValue(Kurssi.kuvaustieto3);
    }

    public String getHakukysymykset() {
        return this.getValue(Kurssi.hakukysymykset);
    }

    public String getSuunnittelukommentti() {
        return this.getValue(Kurssi.suunnittelukommentti);
    }

    public String getOmistaja() {
        return this.getValue(Kurssi.omistaja);
    }

    public String getPeruttavissa() {
        return this.getValue(Kurssi.peruttavissa);
    }

    public Integer getLaskentakaava() {
        return this.getValue(Kurssi.laskentakaava);
    }

    public Date getArvostelu_pvm() {
        return this.getValue(Kurssi.arvostelu_pvm);
    }

    public Date getSiirto_pvm() {
        return this.getValue(Kurssi.siirto_pvm);
    }

    public Integer getHt_lisapistealaraja() {
        return this.getValue(Kurssi.ht_lisapistealaraja);
    }

    public String getArvostelija() {
        return this.getValue(Kurssi.arvostelija);
    }

    public Integer getOpintoviikot_ylaraja() {
        return this.getValue(Kurssi.opintoviikot_ylaraja);
    }

    public Integer getOpintopisteet_ylaraja() {
        return this.getValue(Kurssi.opintopisteet_ylaraja);
    }

    public Integer getOpintopisteet() {
        return this.getValue(Kurssi.opintopisteet);
    }

    public Integer getPeriodi() {
        return this.getValue(Kurssi.periodi);
    }

    public String getKotisivu() {
        return this.getValue(Kurssi.kotisivu);
    }

    public Integer getPeriodi2() {
        return this.getValue(Kurssi.periodi2);
    }

    //</editor-fold>

    @Override
    public String getTableName() {
        return "kurssi";
    }

    @Override
    public Table getNewInstance() {
        return new Kurssi();
    }

    /**
     * Palauttaa Stringin jota näytetään käyttäjälle kurssilistauksessa
     *
     * @return
     */
    public String listaString() {
        return getValue(Kurssi.nimi) + " [" + getValue(Kurssi.alkamis_pvm) + "] " + getValue(Kurssi.tyyppi);
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
}
