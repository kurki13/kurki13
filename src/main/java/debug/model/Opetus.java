/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model;

import debug.model.column.IntegerColumn;
import debug.model.column.StringColumn;
import debug.model.column.TimestampColumn;
import debug.model.util.Table;
import java.sql.Timestamp;

/**
 *
 * @author tkairola
 */
public class Opetus extends Table {

    public static final StringColumn kurssikoodi = new StringColumn("kurssikoodi");
    public static final StringColumn lukukausi = new StringColumn("lukukausi");
    public static final IntegerColumn lukuvuosi = new IntegerColumn("lukuvuosi");
    public static final StringColumn tyyppi = new StringColumn("tyyppi");
    public static final IntegerColumn kurssi_nro = new IntegerColumn("kurssi_nro");
    public static final IntegerColumn ryhma_nro = new IntegerColumn("ryhma_nro");
    public static final IntegerColumn ilmo_jnro = new IntegerColumn("ilmo_jnro");
    public static final StringColumn ilmo = new StringColumn("ilmo");
    public static final StringColumn opetustehtava = new StringColumn("opetustehtava");
    public static final TimestampColumn alkamisaika = new TimestampColumn("alkamisaika");
    public static final TimestampColumn paattymisaika = new TimestampColumn("paattymisaika");
    public static final TimestampColumn alkamis_pvm = new TimestampColumn("alkamis_pvm");
    public static final TimestampColumn paattymis_pvm = new TimestampColumn("paattymis_pvm");
    public static final IntegerColumn max_osallistuja_lkm = new IntegerColumn("max_osallistuja_lkm");
    public static final IntegerColumn ilmoittautuneuden_lkm = new IntegerColumn("ilmoittautuneiden_lkm");
    public static final StringColumn kuvaustieto = new StringColumn("kuvaustieto");
    public static final StringColumn kieli = new StringColumn("kieli");

    @Override
    public String getTableName() {
        return "opetus";
    }

    @Override
    public Table getNewInstance() {
        return new Opetus();
    }

    //<editor-fold defaultstate="collapsed" desc="getters">
    public String getKurssikoodi() {
        return getValue(Opetus.kurssikoodi);
    }

    public String getLukukausi() {
        return getValue(Opetus.lukukausi);
    }

    public Integer getLukuvuosi() {
        return getValue(Opetus.lukuvuosi);
    }

    public String getTyyppi() {
        return getValue(Opetus.tyyppi);
    }

    public Integer getKurssi_nro() {
        return getValue(Opetus.kurssi_nro);
    }

    public Integer getRyhma_nro() {
        return getValue(Opetus.ryhma_nro);
    }

    public Integer getIlmo_jnro() {
        return getValue(Opetus.ilmo_jnro);
    }

    public String getIlmo() {
        return getValue(Opetus.ilmo);
    }

    public String getOpetustehtava() {
        return getValue(Opetus.opetustehtava);
    }

    public Timestamp getAlkamisaika() {
        return getValue(Opetus.alkamisaika);
    }

    public Timestamp getPaattymisaika() {
        return getValue(Opetus.paattymisaika);
    }

    public Timestamp getAlkamis_pvm() {
        return getValue(Opetus.alkamis_pvm);
    }

    public Timestamp getPaattymis_pvm() {
        return getValue(Opetus.paattymis_pvm);
    }

    public Integer getMax_osallistuja_lkm() {
        return getValue(Opetus.max_osallistuja_lkm);
    }

    public Integer getIlmoittautuneuden_lkm() {
        return getValue(Opetus.ilmoittautuneuden_lkm);
    }

    public String getKuvaustieto() {
        return getValue(Opetus.kuvaustieto);
    }

    public String getKieli() {
        return getValue(Opetus.kieli);
    }
    //</editor-fold>
}
