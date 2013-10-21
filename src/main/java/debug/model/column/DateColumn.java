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
public class DateColumn extends Column<Date> {

    public DateColumn(String columnName) {
        super(columnName);
    }

    public DateColumn(String columnName, boolean isId) {
        super(columnName, isId);
    }
    
    

    @Override
    public Class getType() {
        return Date.class;
    }
    
}
