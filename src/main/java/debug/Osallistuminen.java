/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug;

import java.sql.Date;
import java.sql.SQLException;

/**
 *
 * @author mkctammi
 */
public class Osallistuminen {

    //Opiskelija opiskelija; tuli vähän liikaa kyselyitä tälleen
    String hetu;
    int kurssi_nro;
    int ryhma_nro;
    
    Date ilmoittautumis_pvm;
    int ilmo_jnro;

    public Osallistuminen(String hetu, int ilmo_jnro, Date ilmoittautumis_pvm, int kurssi_nro, int ryhma_nro) {
        this.hetu = hetu;
        this.ilmo_jnro = ilmo_jnro;
        this.ilmoittautumis_pvm = ilmoittautumis_pvm;
        this.kurssi_nro = kurssi_nro;
        this.ryhma_nro = ryhma_nro;
    }
    public Opiskelija getOpiskelija() throws SQLException {
        return Pipe.opiskelijaHetulla(hetu);
    }
    
}
