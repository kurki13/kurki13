/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import debug.model.Opiskelija;
import debug.model.util.Filter;
import debug.model.util.SQLoader;
import java.sql.SQLException;

/**
 *
 * @author mkctammi
 */
public class OpiskelijaKyselyt {
    

    public static Opiskelija opiskelijaHetulla(String hetu) throws SQLException {
        Filter f = new Filter(Opiskelija.hetu, hetu);
        return SQLoader.loadTable(new Opiskelija(), f).get(0);
    }
}
