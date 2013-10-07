/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.util;

import debug.model.column.Column;
import debug.model.column.StringColumn;
import debug.model.Kurssi;
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

    public abstract String getTableName();

    public final List<Column> getColumns() {
        Field[] fields = this.getClass().getFields();
        ArrayList<Column> columns = new ArrayList();
        for (Field field : fields) {
            if (Column.class.isAssignableFrom(field.getType())) {
                try {
                    Column column = (Column) field.get(new StringColumn(""));
                    columns.add(column);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Kurssi.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Kurssi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return columns;
    }

    public abstract <T extends Table> T getNewInstance();
    
    private final HashMap<Column,Object> values = new HashMap();

    public final <T> T get(Column<T> sarake) {
        return (T) values.get(sarake);
    }

    public final <T> void set(Column<T> sarake, T value) {
        values.put(sarake, value);
    }
}
