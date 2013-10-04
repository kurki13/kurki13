/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model;

import debug.model.column.DateColumn;
import debug.model.column.IntegerColumn;
import debug.model.column.StringColumn;
import debug.model.util.Table;

/**
 *
 * @author mkctammi
 */
public class Osallistuminen extends Table {
        public static final StringColumn hetu = new StringColumn("hetu");
        public static final IntegerColumn kurssi_nro= new IntegerColumn("kurssi_nro");
        public static final IntegerColumn ryhma_nro= new IntegerColumn("ryhma_nro");
        public static final DateColumn ilmoittautumis_pvm = new DateColumn("ilmoittautumis_pvm");

    @Override
    public String getTableName() {
        return "osallistuminen";
    }

    @Override
    public Table getNewInstance() {
        return new Osallistuminen();
    }
}
