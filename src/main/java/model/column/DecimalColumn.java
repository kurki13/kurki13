/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.column;

/**
 *
 * @author mkctammi
 */
public class DecimalColumn extends Column<Float> {

    public DecimalColumn(String name) {
        super(name);
    }
    

    public DecimalColumn(String columnName, boolean isId) {
        super(columnName, isId);
    }

    @Override
    public Class getType() {
        return Float.class;
    }
    
}
