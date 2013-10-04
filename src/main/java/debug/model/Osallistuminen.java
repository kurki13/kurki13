/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model;

import debug.model.util.Column;
import debug.model.util.Table;
import java.sql.Date;
import java.util.EnumMap;

/**
 *
 * @author mkctammi
 */
public class Osallistuminen implements Table {

    public static enum Sarake implements Column {

        hetu("hetu", String.class),
        kurssi_nro("kurssi_nro", Integer.class),
        ryhma_nro("ryhma_nro", Integer.class),
        ilmoittautumis_pvm("ilmoittautumis_pvm", Date.class),
        ilmo_jnro("ilmo_jnro", Integer.class);
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
        return "osallistuminen";
    }

    @Override
    public Column[] getColumns() {
        return Sarake.values();
    }

    @Override
    public Table getNewInstance() {
        return new Osallistuminen();
    }
    //<editor-fold defaultstate="collapsed" desc="more boilerplate">
    
    EnumMap<Sarake, Object> values = new EnumMap(Sarake.class);

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
