/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model;

import debug.model.column.IntegerColumn;
import debug.model.column.StringColumn;
import debug.model.column.TimestampColumn;
import debug.model.util.Table;

/**
 *
 * @author mkctammi
 */
public class Opiskelija extends Table {
    
        public static final StringColumn hetu = new StringColumn("hetu");
        public static final StringColumn personid= new StringColumn("personid");
        public static final StringColumn etunimi= new StringColumn("etunimi");
        public static final StringColumn sukunimi= new StringColumn("sukunimi");
        public static final StringColumn entinen_sukunimi= new StringColumn("entinen_sukunimi");
        public static final StringColumn osoite= new StringColumn("osoite");
        public static final StringColumn puhelin= new StringColumn("puhelin");
        public static final StringColumn sahkopostiosoite= new StringColumn("sahkopostiosoite");
        public static final StringColumn paa_aine= new StringColumn("paa_aine");
        public static final IntegerColumn aloitusvuosi= new IntegerColumn("aloitusvuosi");
        public static final TimestampColumn kaytto_pvm= new TimestampColumn("kaytto_pvm");
        public static final StringColumn opnro= new StringColumn("opnro");
        public static final StringColumn lupa= new StringColumn("lupa");
        public static final StringColumn vinkki= new StringColumn("vinkki");
        public static final StringColumn varmenne= new StringColumn("varmenne");

    @Override
    public String getTableName() {
        return "opiskelija";
    }

    @Override
    public Table getNewInstance() {
        return new Opiskelija();
    }
}
