/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model;

import debug.model.column.IntegerColumn;
import debug.model.column.StringColumn;
import debug.model.util.Filter;
import debug.model.util.SQLoader;
import debug.model.util.Table;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author tkairola
 */
public class Henkilo extends Table {

    public static final StringColumn htunnus = new StringColumn("htunnus");
    public static final StringColumn etunimet = new StringColumn("etunimet");
    public static final StringColumn sukunimi = new StringColumn("sukunimi");
    public static final StringColumn kutsumanimi = new StringColumn("kutsumanimi");
    public static final StringColumn aktiivisuus = new StringColumn("aktiivisuus");
    public static final StringColumn huone_nro = new StringColumn("huone_nro");
    public static final StringColumn hetu = new StringColumn("hetu");
    public static final StringColumn oppiarvo = new StringColumn("oppiarvo");
    public static final StringColumn titteli = new StringColumn("titteli");
    public static final StringColumn puhelin_tyo = new StringColumn("puhelin_tyo");
    public static final StringColumn kannykka = new StringColumn("kannykka");
    public static final StringColumn katuosoite = new StringColumn("katuosoite");
    public static final StringColumn postinro = new StringColumn("postinro");
    public static final StringColumn postitoimipaikka = new StringColumn("postitoimipaikka");
    public static final IntegerColumn valvontasaldo = new IntegerColumn("valvontasaldo");
    public static final StringColumn sahkopostiosoite = new StringColumn("sahkopostiosoite");
    public static final StringColumn hallinnollinen_kommentti = new StringColumn("hallinnollinen_kommentti");
    public static final StringColumn opiskelija_kommentti = new StringColumn("opiskelija_kommentti");
    public static final StringColumn ktunnus = new StringColumn("ktunnus");
    public static final StringColumn puhelin_koti = new StringColumn("puhelin_koti");
    public static final StringColumn postilokerohuone = new StringColumn("postilokerohuone");
    public static final StringColumn hy_tyosuhde = new StringColumn("hy_tyosuhde");
    public static final StringColumn hy_puhelinluettelossa = new StringColumn("hy_puhelinluettelossa");

    @Override
    public String getTableName() {
        return "henkilo";
    }

    @Override
    public Table getNewInstance() {
        return new Henkilo();
    }

    //<editor-fold defaultstate="collapsed" desc="getterit">
    /**
     * @return the htunnus
     */
    public static StringColumn getHtunnus() {
        return htunnus;
    }

    /**
     * @return the etunimet
     */
    public static StringColumn getEtunimet() {
        return etunimet;
    }

    /**
     * @return the sukunimi
     */
    public static StringColumn getSukunimi() {
        return sukunimi;
    }

    /**
     * @return the kutsumanimi
     */
    public static StringColumn getKutsumanimi() {
        return kutsumanimi;
    }

    /**
     * @return the aktiivisuus
     */
    public static StringColumn getAktiivisuus() {
        return aktiivisuus;
    }

    /**
     * @return the huone_nro
     */
    public static StringColumn getHuone_nro() {
        return huone_nro;
    }

    /**
     * @return the hetu
     */
    public static StringColumn getHetu() {
        return hetu;
    }

    /**
     * @return the oppiarvo
     */
    public static StringColumn getOppiarvo() {
        return oppiarvo;
    }

    /**
     * @return the titteli
     */
    public static StringColumn getTitteli() {
        return titteli;
    }

    /**
     * @return the puhelin_tyo
     */
    public static StringColumn getPuhelin_tyo() {
        return puhelin_tyo;
    }

    /**
     * @return the kannykka
     */
    public static StringColumn getKannykka() {
        return kannykka;
    }

    /**
     * @return the katuosoite
     */
    public static StringColumn getKatuosoite() {
        return katuosoite;
    }

    /**
     * @return the postinro
     */
    public static StringColumn getPostinro() {
        return postinro;
    }

    /**
     * @return the postitoimipaikka
     */
    public static StringColumn getPostitoimipaikka() {
        return postitoimipaikka;
    }

    /**
     * @return the valvontasaldo
     */
    public static IntegerColumn getValvontasaldo() {
        return valvontasaldo;
    }

    /**
     * @return the sahkopostiosoite
     */
    public static StringColumn getSahkopostiosoite() {
        return sahkopostiosoite;
    }

    /**
     * @return the hallinnollinen_kommentti
     */
    public static StringColumn getHallinnollinen_kommentti() {
        return hallinnollinen_kommentti;
    }

    /**
     * @return the opiskelija_kommentti
     */
    public static StringColumn getOpiskelija_kommentti() {
        return opiskelija_kommentti;
    }

    /**
     * @return the ktunnus
     */
    public static StringColumn getKtunnus() {
        return ktunnus;
    }

    /**
     * @return the puhelin_koti
     */
    public static StringColumn getPuhelin_koti() {
        return puhelin_koti;
    }

    /**
     * @return the postilokerohuone
     */
    public static StringColumn getPostilokerohuone() {
        return postilokerohuone;
    }

    /**
     * @return the hy_tyosuhde
     */
    public static StringColumn getHy_tyosuhde() {
        return hy_tyosuhde;
    }

    /**
     * @return the hy_puhelinluettelossa
     */
    public static StringColumn getHy_puhelinluettelossa() {
        return hy_puhelinluettelossa;
    }
    //</editor-fold>
}
