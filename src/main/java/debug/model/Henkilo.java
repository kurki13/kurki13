/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model;

import debug.model.column.IntegerColumn;
import debug.model.column.StringColumn;
import debug.model.column.TimestampColumn;
import debug.model.util.Table;

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
}
