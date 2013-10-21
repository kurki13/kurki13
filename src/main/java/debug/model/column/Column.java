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
    final boolean isId;

    /**
     * Luo uuden sarakkeen, jonka parametrina annettu merkkijono kertoo sarakkeen nimen kannassa.
     * @param columnName 
     */
    public Column(String columnName) {
        this(columnName, false);
    }
    
    /**
     * Luo uuden sarakkeen, jonka  parametrina annettu merkkijono kertoo sarakkeen nimen kannassa.
     * Totuusarvolla määritetään, onko sarake ID sarake.
     * @param columnName
     * @param isId 
     */
    public Column(String columnName, boolean isId) {
        this.columnName = columnName;
        this.isId = isId;
    }
    
    /**
     * Onko sarake ID sarake
     * @return 
     */
    public boolean isId() {
        return isId;
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
