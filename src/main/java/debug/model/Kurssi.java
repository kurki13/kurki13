/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model;

import debug.model.column.DateColumn;
import debug.model.column.IntegerColumn;
import debug.model.column.StringColumn;
import debug.model.osasuoritukset.Muotoilija;
import debug.model.util.Table;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mkctammi
 */
public class Kurssi extends Table {


    public static final StringColumn kurssikoodi = new StringColumn("kurssikoodi"); //ID
    public static final StringColumn lukukausi = new StringColumn("lukukausi"); //ID
    public static final IntegerColumn lukuvuosi = new IntegerColumn("lukuvuosi"); //ID
    public static final StringColumn tyyppi = new StringColumn("tyyppi"); //ID
    public static final IntegerColumn kurssi_nro = new IntegerColumn("kurssi_nro"); //ID
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
        List<OsasuoritusRajat> osasuorit = new ArrayList();
        if (laskariRajat == null)
            laskariRajat = new OsasuoritusRajat(getHyvaksytty_laskarilasnaolo(),getLaskaritehtava_lkm(), getLaskarikerta_lkm());
        return laskariRajat;
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

    public Integer getLisapisteiden_askelkoko() {
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

    public Integer getHarjoitustoiden_askelkoko() {
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

    public Integer getArvostelun_askelkoko() {
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
