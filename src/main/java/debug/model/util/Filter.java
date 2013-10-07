/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.util;

import debug.model.column.Column;

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
}
