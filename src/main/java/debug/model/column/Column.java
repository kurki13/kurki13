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
    
    /**
     * Sarakkeen nimi kannassa
     */
    final String columnName;

    /**
     * Luo uuden sarakkeen, jonka parametrina annettu merkkijono kertoo sarakkeen nimen kannassa.
     * @param columnName 
     */
    public Column(String columnName) {
        this.columnName = columnName;
    }
    
    /**
     * Palauttaa sarakkeen nimen kannassa.
     * @return 
     */
    public final String getColumnName() {
        return this.columnName;
    }
    
    /**
     * Palauttaa sen luokan, jonka tyyppinen arvo tähän sarakkeeseen laitetaan.
     * @return 
     */
    public abstract Class getType();
}
