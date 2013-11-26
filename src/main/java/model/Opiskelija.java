/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.column.IntegerColumn;
import model.column.StringColumn;
import model.column.TimestampColumn;
import model.util.Table;
import java.sql.Timestamp;

/**
 *
 * @author mkctammi
 */
public class Opiskelija extends Table {

    public static final StringColumn hetu = new StringColumn("hetu", true);
    public static final StringColumn personid = new StringColumn("personid");
    public static final StringColumn etunimi = new StringColumn("etunimi");
    public static final StringColumn sukunimi = new StringColumn("sukunimi");
    public static final StringColumn entinen_sukunimi = new StringColumn("entinen_sukunimi");
    public static final StringColumn osoite = new StringColumn("osoite");
    public static final StringColumn puhelin = new StringColumn("puhelin");
    public static final StringColumn sahkopostiosoite = new StringColumn("sahkopostiosoite");
    public static final StringColumn paa_aine = new StringColumn("paa_aine");
    public static final IntegerColumn aloitusvuosi = new IntegerColumn("aloitusvuosi");
    public static final TimestampColumn kaytto_pvm = new TimestampColumn("kaytto_pvm");
    public static final StringColumn opnro = new StringColumn("opnro");
    public static final StringColumn lupa = new StringColumn("lupa");
    public static final StringColumn vinkki = new StringColumn("vinkki");
    public static final StringColumn varmenne = new StringColumn("varmenne");

    
    @Override
    public void update() {
    }
    
    @Override
    public String getTableName() {
        return "opiskelija";
    }

    @Override
    public Table getNewInstance() {
        return new Opiskelija();
    }


    //<editor-fold defaultstate="collapsed" desc="Getters for columns (Stupid velocity)">

    public String getHetu() {
        return getValue(Opiskelija.hetu);
    }
    
    public String getPersonid() {
        return getValue(Opiskelija.personid);
    }

    public String getEtunimi() {
        return getValue(Opiskelija.etunimi);
    }

    public String getSukunimi() {
        return getValue(Opiskelija.sukunimi);
    }

    public String getEntinen_sukunimi() {
        return getValue(Opiskelija.entinen_sukunimi);
    }
    
    public String getOsoite() {
        return getValue(Opiskelija.osoite);
    }

    public String getPuhelin() {
        return getValue(Opiskelija.puhelin);
    }

    public String getSahkopostiosoite() {
        return getValue(Opiskelija.sahkopostiosoite);
    }

    public String getPaa_aine() {
        return getValue(Opiskelija.paa_aine);
    }

    public Integer getAloitusvuosi() {
        return getValue(Opiskelija.aloitusvuosi);
    }

    public Timestamp getKaytto_pvm() {
        return getValue(Opiskelija.kaytto_pvm);
    }

    public String getOpnro() {
        return getValue(Opiskelija.opnro);
    }

    public String getLupa() {
        return getValue(Opiskelija.lupa);
    }

    public String getVinkki() {
        return getValue(Opiskelija.vinkki);
    }

    public String getVarmenne() {
        return getValue(Opiskelija.varmenne);
    }
    
    //</editor-fold>
}
