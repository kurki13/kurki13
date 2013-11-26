/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.util;

import model.column.Column;
import model.column.StringColumn;
import model.Kurssi;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mkctammi
 */
public abstract class Table {

    private final List<Column> columns;
    private final List<Column> idColumns;
    
    /**
     * Palauttaa tämän taulun nimen kannassa
     * @return 
     */
    public abstract String getTableName();

    public Table() {
        Field[] fields = this.getClass().getFields();
        columns = new ArrayList();
        idColumns = new ArrayList();
        for (Field field : fields) {
            if (Column.class.isAssignableFrom(field.getType())) {
                try {
                    Column column = (Column) field.get(new StringColumn(""));
                    columns.add(column);
                    if (column.isId())
                        idColumns.add(column);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Kurssi.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Kurssi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Palauttaa tämän taulun kaikki sarakkeet.
     * @return 
     */
    public final List<Column> getColumns() {
        return columns;
    }

    /**
     * Palauttaa tämän taulun kaikki IDsarakkeet.
     * @return 
     */
    public List<Column> getIdColumns() {
        return idColumns;
    }
    

    /**
     * Tämän funktion tulee palauttaa Table -luokkaa perivän luokan uuden instanssin.
     * @param <T>
     * @return 
     */
    public abstract <T extends Table> T getNewInstance();
    
    /**
     * Tässä hajautustaulussa säilytetään sarakkeiden arvot
     */
    private final HashMap<Column,Object> values = new HashMap();

    /**
     * Palauttaa tämän tauluolion tietyn sarakkeen arvon.
     * @param <T>
     * @param sarake
     * @return 
     */
    public final <T> T getValue(Column<T> sarake) {
        return (T) values.get(sarake);
    }

    /**
     * Asettaa jonkin sarakkeen arvon.
     * @param <T>
     * @param sarake
     * @param value 
     */
    public final <T> void setValue(Column<T> sarake, T value) {
        values.put(sarake, value);
    }

    @Override
    public String toString() {
        String ret = "";
        
        for (Column column : getColumns()) {
            ret += column.getColumnName() + " : " + this.getValue(column) + "<br>";
        }
        
        return ret;
    }
    
    public abstract void update();
    
    
}
