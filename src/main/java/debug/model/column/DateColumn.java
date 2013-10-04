/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.column;

import java.sql.Date;

/**
 *
 * @author mikko
 */
public class DateColumn implements Column<String> {

    String columnName;

    public DateColumn(String columnName) {
        this.columnName = columnName;
    }
    
    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public Class getType() {
        return Date.class;
    }
    
}
