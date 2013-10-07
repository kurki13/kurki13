/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.column;

/**
 *
 * @author mkctammi
 * @param <T>
 */
public abstract class Column<T> {
    
    final String columnName;

    public Column(String columnName) {
        this.columnName = columnName;
    }
    
    public final String getColumnName() {
        return this.columnName;
    }
    
    public abstract Class getType();
}
