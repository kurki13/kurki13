/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import debug.dbconnection.DatabaseConnection;
import debug.model.Henkilo;
import debug.model.Kurssi;
import debug.model.util.Filter;
import debug.model.util.SQLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author tkairola
 */
public class HenkiloKyselyt {
    public static final String haeNimet = //<editor-fold defaultstate="collapsed" desc="haeNimet">
            "SELECT sukunimi, kutsumanimi FROM henkilo\n"
                + " WHERE htunnus = ?";
            //</editor-fold>
    
    public List<Henkilo> henkilotHtunnuksella(String htunnus) throws SQLException {
        Filter f = new Filter(Henkilo.htunnus, htunnus);
        return SQLoader.loadTable(new Henkilo(), f);
    }
    
    public List<Henkilo> henkilotAktiivisuudenMukaan(String aktiivisuus) throws SQLException {
        Filter f = new Filter(Henkilo.aktiivisuus, aktiivisuus);
        return SQLoader.loadTable(new Henkilo(), f);
    }
    
    public static String haeNimet(String tunnus) throws SQLException {
        Connection tietokantayhteys = DatabaseConnection.makeConnection();
        PreparedStatement valmisteltuLause = tietokantayhteys.prepareStatement(haeNimet);
        valmisteltuLause.setString(1, tunnus);
        ResultSet tulosJoukko = valmisteltuLause.executeQuery();
        String palautus;
        if (tulosJoukko.next()) {
            String kutsumanimi = tulosJoukko.getString("kutsumanimi");
            String sukunimi = tulosJoukko.getString("sukunimi");
            palautus = sukunimi + " " + kutsumanimi;
        } else {
            palautus = "Tuntematon";
        }
        tulosJoukko.close();
        valmisteltuLause.close();
        tietokantayhteys.close();
        return palautus;
    }
}
