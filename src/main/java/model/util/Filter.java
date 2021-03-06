/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.util;

import model.column.Column;
import model.column.StringColumn;

/**
 *
 * @author mkctammi
 */
public class Filter {

    public final Column column;
    public final Object expectation;

    public <T> Filter(Column<T> column, T expectation) {
        this.column = column;
        this.expectation = expectation;
    }

    @Override
    public String toString() {
        return column.getColumnName()+":"+expectation;
    }
    
   
}
