/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.column.IntegerColumn;
import model.column.StringColumn;

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
    public void update() {
    }

    @Override
    public Table getNewInstance() {
        return new Henkilo();
    }

    //<editor-fold defaultstate="collapsed" desc="getters">
    
    public String getHtunnus() {
        return getValue(Henkilo.htunnus);
    }

    public String getEtunimet() {
        return getValue(Henkilo.etunimet);
    }

    public String getSukunimi() {
        return getValue(Henkilo.sukunimi);
    }

    public String getKutsumanimi() {
        return getValue(Henkilo.kutsumanimi);
    }

    public String getAktiivisuus() {
        return getValue(Henkilo.aktiivisuus);
    }

    public String getHuone_nro() {
        return getValue(Henkilo.huone_nro);
    }

    public String getHetu() {
        return getValue(Henkilo.hetu);
    }

    public String getOppiarvo() {
        return getValue(Henkilo.oppiarvo);
    }

    public String getTitteli() {
        return getValue(Henkilo.titteli);
    }

    public String getPuhelin_tyo() {
        return getValue(Henkilo.puhelin_tyo);
    }

    public String getKannykka() {
        return getValue(Henkilo.kannykka);
    }

    public String getKatuosoite() {
        return getValue(Henkilo.katuosoite);
    }

    public String getPostinro() {
        return getValue(Henkilo.postinro);
    }
    
    public String getPostitoimipaikka() {
        return getValue(Henkilo.postitoimipaikka);
    }

    public Integer getValvontasaldo() {
        return getValue(Henkilo.valvontasaldo);
    }

    public String getSahkopostiosoite() {
        return getValue(Henkilo.sahkopostiosoite);
    }

    public String getHallinnollinen_kommentti() {
        return getValue(Henkilo.hallinnollinen_kommentti);
    }

    public String getOpiskelija_kommentti() {
        return getValue(Henkilo.opiskelija_kommentti);
    }

    public String getKtunnus() {
        return getValue(Henkilo.ktunnus);
    }

    public String getPuhelin_koti() {
        return getValue(Henkilo.puhelin_koti);
    }

    public String getPostilokerohuone() {
        return getValue(Henkilo.postilokerohuone);
    }

    public String getHy_tyosuhde() {
        return getValue(Henkilo.hy_tyosuhde);
    }

    public String getHy_puhelinluettelossa() {
        return getValue(Henkilo.hy_puhelinluettelossa);
    }
    //</editor-fold>
}
