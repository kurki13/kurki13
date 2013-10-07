/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model;

import debug.model.column.IntegerColumn;
import debug.model.column.StringColumn;
import debug.model.util.Table;

/**
 *
 * @author mkctammi
 */
public class Kurssi extends Table {

    public static final StringColumn kurssikoodi = new StringColumn("kurssikoodi");
    public static final StringColumn lukukausi = new StringColumn("lukukausi");
    public static final IntegerColumn lukuvuosi = new IntegerColumn("lukuvuosi");
    public static final StringColumn tyyppi = new StringColumn("tyyppi");
    public static final IntegerColumn kurssi_nro = new IntegerColumn("kurssi_nro");
    public static final StringColumn nimi = new StringColumn("nimi");


    @Override
    public String getTableName() {
        return "kurssi";
    }
    
    public static void main(String[] args) {
        Kurssi k = new Kurssi();
        k.getColumns();
    }

    @Override
    public Table getNewInstance() {
        return new Kurssi();
    }
    
}
