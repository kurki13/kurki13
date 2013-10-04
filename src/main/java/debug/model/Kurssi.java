/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model;

import debug.model.util.Column;
import debug.model.util.Table;
import java.util.EnumMap;

/**
 *
 * @author mkctammi
 */
public class Kurssi implements Table {

    public static enum Sarake implements Column {
        kurssikoodi("kurssikoodi", String.class),
        lukukausi("lukukausi", String.class),
        lukuvuosi("lukuvuosi", Integer.class),
        tyyppi("tyyppi", String.class),
        kurssi_nro("kurssi_nro", Integer.class),
        nimi("nimi", String.class);
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
        return "kurssi";
    }

    @Override
    public Column[] getColumns() {
        return Sarake.values();
    }
    
    @Override
    public Table getNewInstance() {
        return new Kurssi();
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
