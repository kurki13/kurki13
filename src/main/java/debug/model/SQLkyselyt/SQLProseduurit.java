/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import debug.DatabaseConnection;
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
     * @param kurssikoodi Jäädytettävän kurssin kurssikoodi
     * @param kurssinLukuvuosi Jäädytettävän kurssin lukuvuosi
     * @param kurssinLukukausi Jäädytettävän kurssin lukukausi
     * @param kurssinNumero Jäädytettävän kurssin kurssinumero
     * @param kurssinTyyppi Jäädytettävän kurssin tyyppi
     * @return Merkkijono "OK" jos jäädytys onnistui.
     * @throws SQLException Tietokantavirhe
     */
    public static String suoritaJaadytys(String kurssikoodi, int kurssinLukuvuosi, String kurssinLukukausi, 
            int kurssinNumero, String kurssinTyyppi) throws SQLException {
        Connection tietokantaYhteys = DatabaseConnection.makeConnection();
        CallableStatement kutsuttavaLause = tietokantaYhteys.prepareCall("{ ? = call jaadytys05 (?, ?, ?, ?, ?, ?) }");
        kutsuttavaLause.registerOutParameter(1, java.sql.Types.VARCHAR);
            
        kutsuttavaLause.setString(2, kurssikoodi);
        kutsuttavaLause.setInt(3, kurssinLukuvuosi);
        kutsuttavaLause.setString(4, kurssinLukukausi);
        kutsuttavaLause.setInt(5, kurssinNumero);
        kutsuttavaLause.setString(6, kurssinTyyppi);
        kutsuttavaLause.setInt(7, 0);
        kutsuttavaLause.executeUpdate();
        
        return kutsuttavaLause.getString(1);
    }
    
    /**
     * Metodi suorittaa SQL-Proseduurin arvostelu05 funktion arvostele.
     * 
     * @param kurssikoodi Arvosteltavan kurssin kurssikoodi
     * @param kurssinLukuvuosi Arvosteltavan kurssin lukuvuosi
     * @param kurssinLukukausi Arvosteltavan kurssin lukukausi
     * @param kurssinTyyppi Arvosteltavan kurssin tyyppi
     * @param kurssinNumero Arvosteltavan kurssin kurssinumero
     * @param arvostelija Arvosteltavan kurssin arvostelija
     * @return Kokonaisluku 0, jos arvostelu onnistui
     * @throws SQLException Tietokantavirhe
     */
    public static int suoritaArvostelu(String kurssikoodi, int kurssinLukuvuosi, String kurssinLukukausi, 
            String kurssinTyyppi, int kurssinNumero, String arvostelija) throws SQLException {
        Connection tietokantaYhteys = DatabaseConnection.makeConnection();
        CallableStatement kutsuttavaLause = tietokantaYhteys.prepareCall("{ ? = call arvostelu05.arvostele (?, ?, ?, ?, ?, ?) }");

        kutsuttavaLause.registerOutParameter(1, java.sql.Types.INTEGER);
        kutsuttavaLause = asetaTietojaLauseeseen(kutsuttavaLause, kurssikoodi, kurssinLukuvuosi, kurssinLukukausi, 
            kurssinTyyppi, kurssinNumero);
        kutsuttavaLause.setString(7, arvostelija);
        kutsuttavaLause.executeUpdate();
        
        return kutsuttavaLause.getInt(1);
    }
    
    /**
     * Metodi suorittaa SQL-Proseduurin palautaopiskelija
     * 
     * @param kurssikoodi Sen kurssin kurssikoodi, jolle opiskelija palautetaan
     * @param kurssinLukuvuosi Sen kurssin lukuvuosi, jolle opiskelija palautetaan
     * @param kurssinLukukausi Sen kurssin lukukausi, jolle opiskelija palautetaan
     * @param kurssinTyyppi Sen kurssin tyyppi, jolle opiskelija palautetaan
     * @param kurssinNumero Sen kurssin numero, jolle opiskelija palautetaan
     * @param opiskelijanOpiskelijanumero Palautettavan opiskelijan opiskelijanumero
     * @return Kokonaisluku 0, jos palautus onnistui
     * @throws SQLException Tietokantavirhe
     */
    public static int palautaOpiskelija(String kurssikoodi, int kurssinLukuvuosi, String kurssinLukukausi, 
            String kurssinTyyppi, int kurssinNumero, String opiskelijanOpiskelijanumero) 
            throws SQLException {
        Connection tietokantaYhteys = DatabaseConnection.makeConnection();
        CallableStatement kutsuttavaLause = tietokantaYhteys.prepareCall("{ ? = call palautaopiskelija (?, ?, ?, ?, ?, ?, ?) }");
        kutsuttavaLause.registerOutParameter(1, java.sql.Types.INTEGER);

        kutsuttavaLause = asetaTietojaLauseeseen(kutsuttavaLause, kurssikoodi, kurssinLukuvuosi, kurssinLukukausi, 
            kurssinTyyppi, kurssinNumero);
        kutsuttavaLause.setInt(7, -1);
        kutsuttavaLause.setString(8, opiskelijanOpiskelijanumero);

        kutsuttavaLause.executeUpdate();

        return kutsuttavaLause.getInt(1);
    }
    
    /**
     * Metodi asettaa kutsuttavaan lauseeseen kurssiin liittyviä parametreja.
     * 
     * @param kutsuttavaLause Lause, johon parametreja asetetaan
     * @param kurssikoodi Kurssin kurssikoodi
     * @param kurssinLukuvuosi Kurssin lukuvuosi
     * @param kurssinLukukausi Kurssin lukukausi
     * @param kurssinTyyppi Kurssin tyyppi
     * @param kurssinNumero Kurssin kurssinumero
     * @return Lause, johon parametrit asetettiin
     * @throws SQLException Tietokantavirhe
     */
    private static CallableStatement asetaTietojaLauseeseen(CallableStatement kutsuttavaLause, String kurssikoodi, 
            int kurssinLukuvuosi, String kurssinLukukausi, String kurssinTyyppi, int kurssinNumero) throws SQLException {
        kutsuttavaLause.setString(2, kurssikoodi);
        kutsuttavaLause.setInt(3, kurssinLukuvuosi);
        kutsuttavaLause.setString(4, kurssinLukukausi);
        kutsuttavaLause.setString(5, kurssinTyyppi);
        kutsuttavaLause.setInt(6, kurssinNumero);
        return kutsuttavaLause;
    }
}
