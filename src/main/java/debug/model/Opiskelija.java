/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model;

import debug.model.column.Column;
import debug.model.column.IntegerColumn;
import debug.model.column.StringColumn;
import debug.model.column.TimestampColumn;
import debug.model.util.Filter;
import debug.model.util.SQLoader;
import debug.model.util.Table;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author mkctammi
 */
public class Opiskelija extends Table {

    public static final StringColumn hetu = new StringColumn("hetu");
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
    public String getTableName() {
        return "opiskelija";
    }

    @Override
    public Table getNewInstance() {
        return new Opiskelija();
    }

    public List<Opiskelija> opiskelijatHetulla(String hetu) throws SQLException {
        Filter f = new Filter(Opiskelija.hetu, hetu);
        return SQLoader.loadTable(new Opiskelija(), f);
    }

    //<editor-fold defaultstate="collapsed" desc="Getters for columns (Stupid velocity)">
    /**
     * @return the hetu
     */
    public static StringColumn getHetu() {
        return hetu;
    }

    /**
     * @return the personid
     */
    public static StringColumn getPersonid() {
        return personid;
    }

    /**
     * @return the etunimi
     */
    public static StringColumn getEtunimi() {
        return etunimi;
    }

    /**
     * @return the sukunimi
     */
    public static StringColumn getSukunimi() {
        return sukunimi;
    }

    /**
     * @return the entinen_sukunimi
     */
    public static StringColumn getEntinen_sukunimi() {
        return entinen_sukunimi;
    }

    /**
     * @return the osoite
     */
    public static StringColumn getOsoite() {
        return osoite;
    }

    /**
     * @return the puhelin
     */
    public static StringColumn getPuhelin() {
        return puhelin;
    }

    /**
     * @return the sahkopostiosoite
     */
    public static StringColumn getSahkopostiosoite() {
        return sahkopostiosoite;
    }

    /**
     * @return the paa_aine
     */
    public static StringColumn getPaa_aine() {
        return paa_aine;
    }

    /**
     * @return the aloitusvuosi
     */
    public static IntegerColumn getAloitusvuosi() {
        return aloitusvuosi;
    }

    /**
     * @return the kaytto_pvm
     */
    public static TimestampColumn getKaytto_pvm() {
        return kaytto_pvm;
    }

    /**
     * @return the opnro
     */
    public static StringColumn getOpnro() {
        return opnro;
    }

    /**
     * @return the lupa
     */
    public static StringColumn getLupa() {
        return lupa;
    }

    /**
     * @return the vinkki
     */
    public static StringColumn getVinkki() {
        return vinkki;
    }

    /**
     * @return the varmenne
     */
    public static StringColumn getVarmenne() {
        return varmenne;
    }
    //</editor-fold>
}
