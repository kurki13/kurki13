/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.column;

/**
 *
 * @author mikko
 */
public class StringColumn extends Column<String> {

    public StringColumn(String columnName) {
        super(columnName);
    }

    public StringColumn(String columnName, boolean isId) {
        super(columnName, isId);
    }
    @Override
    public Class getType() {
        return String.class;
    }
    
}
