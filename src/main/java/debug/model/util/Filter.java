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

    public Filter(Column column, Object expectation) {
        if (column.getType() != expectation.getClass()) {
            throw new IllegalArgumentException("Filter for column "
                    + column.getColumnName() + " need a expectation object of type"
                    + column.getType().getCanonicalName()
                    + ". Received argument had type " + expectation.getClass().getCanonicalName());
        }
        this.column = column;
        this.expectation = expectation;
    }
}
