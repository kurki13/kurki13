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
public class Opetus extends Table{

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

    /**
     * @return the kurssikoodi
     */
    public static StringColumn getKurssikoodi() {
        return kurssikoodi;
    }

    /**
     * @return the lukukausi
     */
    public static StringColumn getLukukausi() {
        return lukukausi;
    }

    /**
     * @return the lukuvuosi
     */
    public static IntegerColumn getLukuvuosi() {
        return lukuvuosi;
    }

    /**
     * @return the tyyppi
     */
    public static StringColumn getTyyppi() {
        return tyyppi;
    }

    /**
     * @return the kurssi_nro
     */
    public static IntegerColumn getKurssi_nro() {
        return kurssi_nro;
    }

    /**
     * @return the ryhma_nro
     */
    public static IntegerColumn getRyhma_nro() {
        return ryhma_nro;
    }

    /**
     * @return the ilmo_jnro
     */
    public static IntegerColumn getIlmo_jnro() {
        return ilmo_jnro;
    }

    /**
     * @return the ilmo
     */
    public static StringColumn getIlmo() {
        return ilmo;
    }

    /**
     * @return the opetustehtava
     */
    public static StringColumn getOpetustehtava() {
        return opetustehtava;
    }

    /**
     * @return the alkamisaika
     */
    public static TimestampColumn getAlkamisaika() {
        return alkamisaika;
    }

    /**
     * @return the paattymisaika
     */
    public static TimestampColumn getPaattymisaika() {
        return paattymisaika;
    }

    /**
     * @return the alkamis_pvm
     */
    public static TimestampColumn getAlkamis_pvm() {
        return alkamis_pvm;
    }

    /**
     * @return the paattymis_pvm
     */
    public static TimestampColumn getPaattymis_pvm() {
        return paattymis_pvm;
    }

    /**
     * @return the max_osallistuja_lkm
     */
    public static IntegerColumn getMax_osallistuja_lkm() {
        return max_osallistuja_lkm;
    }

    /**
     * @return the ilmoittautuneuden_lkm
     */
    public static IntegerColumn getIlmoittautuneuden_lkm() {
        return ilmoittautuneuden_lkm;
    }

    /**
     * @return the kuvaustieto
     */
    public static StringColumn getKuvaustieto() {
        return kuvaustieto;
    }

    /**
     * @return the kieli
     */
    public static StringColumn getKieli() {
        return kieli;
    }
}
