/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import debug.model.Henkilo;
import debug.model.util.Filter;
import debug.model.util.SQLoader;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author tkairola
 */
public class HenkiloKyselyt {
    
    public List<Henkilo> henkilotHtunnuksella(String htunnus) throws SQLException {
        Filter f = new Filter(Henkilo.htunnus, htunnus);
        return SQLoader.loadTable(new Henkilo(), f);
    }
    
    public List<Henkilo> henkilotAktiivisuudenMukaan(String aktiivisuus) throws SQLException {
        Filter f = new Filter(Henkilo.aktiivisuus, aktiivisuus);
        return SQLoader.loadTable(new Henkilo(), f);
    }
}
