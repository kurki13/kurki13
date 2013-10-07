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
    public static StringColumn getKurssikoodi() {
        return kurssikoodi;
    }

    public static StringColumn getLukukausi() {
        return lukukausi;
    }

    public static IntegerColumn getLukuvuosi() {
        return lukuvuosi;
    }

    public static StringColumn getTyyppi() {
        return tyyppi;
    }

    public static IntegerColumn getKurssi_nro() {
        return kurssi_nro;
    }

    public static StringColumn getKielikoodi() {
        return kielikoodi;
    }

    public static StringColumn getNimi() {
        return nimi;
    }

    public static IntegerColumn getOpintoviikot() {
        return opintoviikot;
    }

    public static IntegerColumn getLuentotunnit() {
        return luentotunnit;
    }

    public static IntegerColumn getLuentokerta_lkm() {
        return luentokerta_lkm;
    }

    public static IntegerColumn getHarjoitustunnit() {
        return harjoitustunnit;
    }

    public static IntegerColumn getLaskarikerta_lkm() {
        return laskarikerta_lkm;
    }

    public static StringColumn getLyhenne() {
        return lyhenne;
    }

    public static StringColumn getSalasana() {
        return salasana;
    }

    public static DateColumn getSuoritus_pvm() {
        return suoritus_pvm;
    }

    public static StringColumn getTila() {
        return tila;
    }

    public static DateColumn getAlkamis_pvm() {
        return alkamis_pvm;
    }

    public static DateColumn getPaattymis_pvm() {
        return paattymis_pvm;
    }

    public static DateColumn getPaivitys_pvm() {
        return paivitys_pvm;
    }

    public static IntegerColumn getMax_osallistuja_lkm() {
        return max_osallistuja_lkm;
    }

    public static StringColumn getLaskaritehtava_lkm() {
        return laskaritehtava_lkm;
    }

    public static IntegerColumn getPakolliset_laskarikerta_lkm() {
        return pakolliset_laskarikerta_lkm;
    }

    public static IntegerColumn getPakolliset_laskaritehtava_lkm() {
        return pakolliset_laskaritehtava_lkm;
    }

    public static IntegerColumn getMax_laskaripisteet() {
        return max_laskaripisteet;
    }

    public static StringColumn getHyvaksytty_laskarilasnaolo() {
        return hyvaksytty_laskarilasnaolo;
    }

    public static IntegerColumn getLisapistealaraja() {
        return lisapistealaraja;
    }

    public static StringColumn getLisapisterajat() {
        return lisapisterajat;
    }

    public static IntegerColumn getLisapisteiden_askelkoko() {
        return lisapisteiden_askelkoko;
    }

    public static IntegerColumn getHarjoitustyo_lkm() {
        return harjoitustyo_lkm;
    }

    public static IntegerColumn getPakolliset_harjoitustyo_lkm() {
        return pakolliset_harjoitustyo_lkm;
    }

    public static IntegerColumn getHarjoitustyopisteet() {
        return harjoitustyopisteet;
    }

    public static StringColumn getMax_harjoitustyopisteet() {
        return max_harjoitustyopisteet;
    }

    public static StringColumn getMin_harjoitustyopisteet() {
        return min_harjoitustyopisteet;
    }

    public static IntegerColumn getMin_harjoitustyopisteet_summa() {
        return min_harjoitustyopisteet_summa;
    }

    public static StringColumn getHarjoitustyon_pisterajat() {
        return harjoitustyon_pisterajat;
    }

    public static IntegerColumn getHarjoitustoiden_askelkoko() {
        return harjoitustoiden_askelkoko;
    }

    public static IntegerColumn getValikokeet_lkm() {
        return valikokeet_lkm;
    }

    public static IntegerColumn getPakolliset_koe_lkm() {
        return pakolliset_koe_lkm;
    }

    public static StringColumn getMax_koepisteet() {
        return max_koepisteet;
    }

    public static StringColumn getMin_koepisteet() {
        return min_koepisteet;
    }

    public static IntegerColumn getMin_koepisteet_summa() {
        return min_koepisteet_summa;
    }

    public static IntegerColumn getMin_yhteispisteet() {
        return min_yhteispisteet;
    }

    public static IntegerColumn getArvostelun_askelkoko() {
        return arvostelun_askelkoko;
    }

    public static StringColumn getArvosanarajat() {
        return arvosanarajat;
    }

    public static StringColumn getArvostellaanko() {
        return arvostellaanko;
    }

    public static StringColumn getKokonaistiedot() {
        return kokonaistiedot;
    }

    public static StringColumn getKuvaustieto1() {
        return kuvaustieto1;
    }

    public static StringColumn getKuvaustieto2() {
        return kuvaustieto2;
    }

    public static StringColumn getKuvaustieto3() {
        return kuvaustieto3;
    }

    public static StringColumn getHakukysymykset() {
        return hakukysymykset;
    }

    public static StringColumn getSuunnittelukommentti() {
        return suunnittelukommentti;
    }

    public static StringColumn getOmistaja() {
        return omistaja;
    }

    public static StringColumn getPeruttavissa() {
        return peruttavissa;
    }

    public static IntegerColumn getLaskentakaava() {
        return laskentakaava;
    }

    public static DateColumn getArvostelu_pvm() {
        return arvostelu_pvm;
    }

    public static DateColumn getSiirto_pvm() {
        return siirto_pvm;
    }

    public static IntegerColumn getHt_lisapistealaraja() {
        return ht_lisapistealaraja;
    }

    public static StringColumn getArvostelija() {
        return arvostelija;
    }

    public static IntegerColumn getOpintoviikot_ylaraja() {
        return opintoviikot_ylaraja;
    }

    public static IntegerColumn getOpintopisteet_ylaraja() {
        return opintopisteet_ylaraja;
    }

    public static IntegerColumn getOpintopisteet() {
        return opintopisteet;
    }

    public static IntegerColumn getPeriodi() {
        return periodi;
    }

    public static StringColumn getKotisivu() {
        return kotisivu;
    }

    public static IntegerColumn getPeriodi2() {
        return periodi2;
    }

    //</editor-fold>

    public Kurssi kurssitIDlla(String kKoodi, String lKausi,
            int lVuosi, String tyyppi, int kNro) throws SQLException {
        
        List filters = new ArrayList<Filter>();
        filters.add(new Filter(Kurssi.kurssikoodi,kKoodi));
        filters.add(new Filter(Kurssi.lukukausi,lKausi));
        filters.add(new Filter(Kurssi.lukuvuosi,lVuosi));
        filters.add(new Filter(Kurssi.tyyppi,tyyppi));
        filters.add(new Filter(Kurssi.kurssi_nro,kNro));
        List<Kurssi> kurssit = SQLoader.loadTable(new Kurssi(), filters);
        return kurssit.get(0);
    }
    
    @Override
    public String getTableName() {
        return "kurssi";
    }

    @Override
    public Table getNewInstance() {
        return new Kurssi();
    }
}
