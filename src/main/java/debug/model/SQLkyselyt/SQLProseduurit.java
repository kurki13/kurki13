/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import debug.dbconnection.DatabaseConnection;
import debug.model.Kurssi;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Luokka SQL-Proseduurien suorittamiseen.
 */
public class SQLProseduurit {
    
    /**
     * Metodi suorittaa SQL-Proseduurin jaadytys05.
     * 
     * @param kurssi Jäädytettävä kurssi
     * @return Merkkijono "OK" jos jäädytys onnistui.
     * @throws SQLException Tietokantavirhe
     */
    public static String suoritaJaadytys(Kurssi kurssi) throws SQLException {
        Connection tietokantayhteys = DatabaseConnection.makeConnection();
        CallableStatement kutsuttavaLause = tietokantayhteys.prepareCall("{ ? = call jaadytys05 (?, ?, ?, ?, ?, ?) }");
        kutsuttavaLause.registerOutParameter(1, java.sql.Types.VARCHAR);
            
        kutsuttavaLause.setString(2, kurssi.getKurssikoodi());
        kutsuttavaLause.setInt(3, kurssi.getLukuvuosi());
        kutsuttavaLause.setString(4, kurssi.getLukukausi());
        kutsuttavaLause.setInt(5, kurssi.getKurssi_nro());
        kutsuttavaLause.setString(6, kurssi.getTyyppi());
        kutsuttavaLause.setInt(7, 0);
        kutsuttavaLause.executeUpdate();
        
        String palautus = kutsuttavaLause.getString(1);
        kutsuttavaLause.close();
        tietokantayhteys.close();
        return palautus;
    }
    
    /**
     * Metodi suorittaa SQL-Proseduurin arvostelu05 funktion arvostele.
     * 
     * @param kurssi Arvosteltava kurssi
     * @param arvostelija Arvosteltavan kurssin arvostelija
     * @return Kokonaisluku 0, jos arvostelu onnistui
     * @throws SQLException Tietokantavirhe
     */
    public static int suoritaArvostelu(Kurssi kurssi, String arvostelija) throws SQLException {
        Connection tietokantayhteys = DatabaseConnection.makeConnection();
        CallableStatement kutsuttavaLause = tietokantayhteys.prepareCall("{ ? = call arvostelu05.arvostele (?, ?, ?, ?, ?, ?) }");

        kutsuttavaLause.registerOutParameter(1, java.sql.Types.INTEGER);
        kutsuttavaLause.setString(2, kurssi.getKurssikoodi());
        kutsuttavaLause.setInt(3, kurssi.getLukuvuosi());
        kutsuttavaLause.setString(4, kurssi.getLukukausi());
        kutsuttavaLause.setString(5, kurssi.getTyyppi());
        kutsuttavaLause.setInt(6, kurssi.getKurssi_nro());
        kutsuttavaLause.setString(7, arvostelija);
        kutsuttavaLause.executeUpdate();
        
        int palautus = kutsuttavaLause.getInt(1);
        kutsuttavaLause.close();
        tietokantayhteys.close();
        return palautus;
    }
    
    /**
     * Metodi suorittaa SQL-Proseduurin palautaopiskelija
     * 
     * @param kurssi Kurssi, jolle opiskelija palautetaan
     * @param ryhmanNumero Ryhmä, johon opiskelija palautetaan
     * @param opiskelijanumero Palautettavan opiskelijan opiskelijanumero
     * @return Kokonaisluku 0, jos palautus onnistui
     * @throws SQLException Tietokantavirhe
     */
    public static int palautaOpiskelija(Kurssi kurssi, int ryhmanNumero, String opiskelijanumero) 
            throws SQLException {
        Connection tietokantayhteys = DatabaseConnection.makeConnection();
        CallableStatement kutsuttavaLause = tietokantayhteys.prepareCall("{ ? = call palautaopiskelija (?, ?, ?, ?, ?, ?, ?) }");
        kutsuttavaLause.registerOutParameter(1, java.sql.Types.INTEGER);

        kutsuttavaLause.setString(2, kurssi.getKurssikoodi());
        kutsuttavaLause.setInt(3, kurssi.getLukuvuosi());
        kutsuttavaLause.setString(4, kurssi.getLukukausi());
        kutsuttavaLause.setString(5, kurssi.getTyyppi());
        kutsuttavaLause.setInt(6, kurssi.getKurssi_nro());
        kutsuttavaLause.setInt(7, ryhmanNumero);
        kutsuttavaLause.setString(8, opiskelijanumero);

        kutsuttavaLause.executeUpdate();

        int palautus = kutsuttavaLause.getInt(1);
        kutsuttavaLause.close();
        tietokantayhteys.close();
        return palautus;
    }
    
    /**
     * Metodi suorittaa SQL-Proseduurin poistaopiskelija.
     * 
     * @param kurssi Kurssi, jolta opiskelija poistetaan
     * @param ryhmanNumero Ryhmä, jolta opiskelija poistetaan
     * @param opiskelijanumero Poistettavan opiskelijan opiskelijanumero
     * @return Kokonaisluku 0, jos poistaminen onnistui
     * @throws SQLException Tietokantavirhe
     */
    public static int poistaOpiskelija(Kurssi kurssi, int ryhmanNumero ,String opiskelijanumero) throws SQLException {
        Connection tietokantayhteys = DatabaseConnection.makeConnection();
        CallableStatement kutsuttavaLause = tietokantayhteys.prepareCall("{ ? = call poistaopiskelija (?, ?, ?, ?, ?, ?, ?) }");
        kutsuttavaLause.registerOutParameter(1, java.sql.Types.INTEGER);
        
        kutsuttavaLause.setString(2, kurssi.getKurssikoodi());
        kutsuttavaLause.setInt(3, kurssi.getLukuvuosi());
        kutsuttavaLause.setString(4, kurssi.getLukukausi());
        kutsuttavaLause.setString(5, kurssi.getTyyppi());
        kutsuttavaLause.setInt(6, kurssi.getKurssi_nro());
        kutsuttavaLause.setInt(7, ryhmanNumero);
        kutsuttavaLause.setString(8, opiskelijanumero);
        
        kutsuttavaLause.executeUpdate();
        
        int palautus = kutsuttavaLause.getInt(1);
        kutsuttavaLause.close();
        tietokantayhteys.close();
        return palautus;
    }
    
    /**
     * Metodi suorittaa SQL-Proseduurin kurkiilmo.
     * 
     * @param kurssi Kurssi, jolle opiskelija lisätään
     * @param ryhmanNumero Ryhmä, jolle opiskelija lisätään
     * @param opiskelijanumero Lisättävän opiskelijan opiskelijanumero
     * @return Merkkijono ok, jos lisääminen onnistui
     * @throws SQLException Tietokantavirhe
     */
    public static String lisaaOpiskelija(Kurssi kurssi, int ryhmanNumero, String opiskelijanumero) throws SQLException {
        Connection tietokantayhteys = DatabaseConnection.makeConnection();
        CallableStatement kutsuttavaLause = tietokantayhteys.prepareCall("{ ? = call kurkiilmo (?, ?, ?, ?, ?, ?, ?) }");
        kutsuttavaLause.registerOutParameter(1, java.sql.Types.VARCHAR);
        
        kutsuttavaLause.setString(2, kurssi.getKurssikoodi());
        kutsuttavaLause.setString(3, kurssi.getLukukausi());
        kutsuttavaLause.setInt(4, kurssi.getLukuvuosi());
        kutsuttavaLause.setString(5, kurssi.getTyyppi());
        kutsuttavaLause.setInt(6, kurssi.getKurssi_nro());
        kutsuttavaLause.setInt(7, ryhmanNumero);
        kutsuttavaLause.setString(8, opiskelijanumero);
        
        kutsuttavaLause.executeUpdate();
        
        String palautus = kutsuttavaLause.getString(1);
        kutsuttavaLause.close();
        tietokantayhteys.close();
        return palautus;
    }
    
    /**
     * Metodi suorittaa SQL-Proseduurin ryhmavaihto.
     * 
     * @param uusiRyhma Ryhmä, johon vaihdetaan
     * @param kurssi Kurssi, jossa ryhmää vaihdetaan
     * @param ryhmanNumero Vanha ryhmä
     * @param opiskelijanumero Ryhmää vaihtavan opiskelijan opiskelijanumero
     * @return Vaihdetun ryhmän numero, jos ryhmänvaihto onnistui
     * @throws SQLException Tietokantavirhe
     */
    public static int vaihdaOpiskelijanRyhmaa(int uusiRyhma, Kurssi kurssi, int ryhmanNumero, String opiskelijanumero) throws SQLException {
        Connection tietokantayhteys = DatabaseConnection.makeConnection();
        CallableStatement kutsuttavaLause = tietokantayhteys.prepareCall("{ ? = call ryhmavaihto (?, ?, ?, ?, ?, ?, ?, ?, ?) }");
        kutsuttavaLause.registerOutParameter(1, java.sql.Types.INTEGER);
        
        kutsuttavaLause.setInt(2, uusiRyhma);
        kutsuttavaLause.setString(3, kurssi.getKurssikoodi());
        kutsuttavaLause.setInt(4, kurssi.getLukuvuosi());
        kutsuttavaLause.setString(5, kurssi.getLukukausi());
        kutsuttavaLause.setString(6, kurssi.getTyyppi());
        kutsuttavaLause.setInt(7, kurssi.getKurssi_nro());
        kutsuttavaLause.setInt(8, ryhmanNumero);
        kutsuttavaLause.setString(9, opiskelijanumero);
        kutsuttavaLause.setString(10, "KurKi");
        
        kutsuttavaLause.executeUpdate();
        
        int palautus = kutsuttavaLause.getInt(1);
        kutsuttavaLause.close();
        tietokantayhteys.close();
        return palautus;
    }
}