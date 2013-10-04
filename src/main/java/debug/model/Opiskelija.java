/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model;

import debug.model.util.Column;
import debug.model.util.Table;
import java.sql.Timestamp;
import java.util.EnumMap;

/**
 *
 * @author mkctammi
 */
public class Opiskelija implements Table {

    public static enum Sarake implements Column {

        hetu("hetu", String.class),
        personid("personid", String.class),
        etunimi("etunimi", String.class),
        sukunimi("sukunimi", String.class),
        entinen_sukunimi("entinen_sukunimi", String.class),
        osoite("osoite", String.class),
        puhelin("puhelin", String.class),
        sahkopostiosoite("sahkopostiosoite", String.class),
        paa_aine("paa_aine", String.class),
        aloitusvuosi("aloitusvuosi", Integer.class),
        kaytto_pvm("kaytto_pvm", Timestamp.class),
        opnro("opnro", String.class),
        lupa("lupa", String.class),
        vinkki("vinkki", String.class),
        varmenne("varmenne", String.class);
        //<editor-fold defaultstate="collapsed" desc="boilerplate code"> 
        String name;
        Class type;

        private Sarake(String name, Class type) {
            this.name = name;
            this.type = type; //TODO chekkejä ettei heitetä mitään vääriä tyyppejä
        }

        @Override
        public String getColumnName() {
            return name;
        }

        @Override
        public Class getType() {
            return type;
        }
        //</editor-fold>
    }

    @Override
    public String getTableName() {
        return "opiskelija";
    }

    @Override
    public Column[] getColumns() {
        return Sarake.values();
    }

    @Override
    public Table getNewInstance() {
        return new Opiskelija();
    }
    //<editor-fold defaultstate="collapsed" desc="more boilerplate">
    EnumMap<Sarake, Object> values = new EnumMap(Opiskelija.Sarake.class);

    @Override
    public <T> T get(Column sarake, Class<T> type) {
        Helper.checkGetType(this, sarake, type);
        return type.cast(values.get(sarake));
    }

    @Override
    public void set(Column sarake, Object value) {
        Helper.checkSetType(this, sarake, value);
        values.put((Sarake) sarake, value);
    }
    //</editor-fold>
}
