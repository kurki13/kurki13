/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.column;

/**
 *
 * @author mikko
 */
public class IntegerColumn extends Column<Integer> {

    public IntegerColumn(String columnName) {
        super(columnName);
    }
    @Override
    public Class getType() {
        return Integer.class;
    }
    
}
