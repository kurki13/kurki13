/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import debug.SessioApuri;
import debug.dbconnection.DatabaseConnection;
import debug.model.Kurssi;
import debug.model.Opiskelija;
import debug.util.LocalisationBundle;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    
    public static boolean poistaOpiskelija(Kurssi kurssi, int ryhmanNumero, String opiskelijanumero, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        LocalisationBundle bundle = SessioApuri.bundle(request);
        int tulos;
        try {
            tulos = poistaOpiskelija(kurssi, ryhmanNumero, opiskelijanumero);
        } catch (SQLException poikkeus) {
            istunto.setAttribute(SessioApuri.Virhe, bundle.getString("tkvirhe") + ": " + poikkeus.getLocalizedMessage());
            return false;
        }
        if (tulos == 1) {
            istunto.setAttribute(SessioApuri.Virhe, bundle.getString("kurssiltaPoistoEO") + ": " + bundle.getString("poistaminenEiOnnistunut"));
            return false;
        } else if (tulos == 2) {
            istunto.setAttribute(SessioApuri.Virhe, bundle.getString("kurssiltaPoistoEO") + ": " + bundle.getString("opiskelijalaskuriEpaonnistui"));
            return false;
        }
        return true;
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
    private static int poistaOpiskelija(Kurssi kurssi, int ryhmanNumero, String opiskelijanumero) throws SQLException {
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
    
    public static boolean lisaaOpiskelija(Kurssi kurssi, int ryhmanNumero, String opiskelijanumero, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        LocalisationBundle bundle = SessioApuri.bundle(request);
        String tulos;
        try {
            tulos = lisaaOpiskelija(kurssi, ryhmanNumero, opiskelijanumero);
        } catch (SQLException poikkeus) {
            istunto.setAttribute(SessioApuri.Virhe, bundle.getString("tkvirhe") + ": " + poikkeus.getLocalizedMessage());
            return false;
        }
        if (tulos.equals("ok")) {
            return true;
        } else if (tulos.equals("virhe: opiskelija on jo ilmoittautunut kurssille.")) {
            istunto.setAttribute(SessioApuri.Virhe, bundle.getString("lisaysKEO") + ": " + bundle.getString("opiskelijaojik") + ".");
            return false;
        } else {
            istunto.setAttribute(SessioApuri.Virhe, bundle.getString("lisaysKEO") + ": " + bundle.getString("tkvirhe"));
            return false;
        }
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
    private static String lisaaOpiskelija(Kurssi kurssi, int ryhmanNumero, String opiskelijanumero) throws SQLException {
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
    
    public static void vaihdaOpiskelijanRyhmaa(int uusiRyhma, Kurssi kurssi, int ryhmanNumero, String opiskelijanumero, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        LocalisationBundle bundle = SessioApuri.bundle(request);
        int tulos;
        String virheilmoituksenAlku;
        try {
            tulos = vaihdaOpiskelijanRyhmaa(uusiRyhma, kurssi, ryhmanNumero, opiskelijanumero);
            virheilmoituksenAlku = bundle.getString("ryhmanVeo") + " " + palautaOpiskelijanNimi(opiskelijanumero)  + " " + bundle.getString("ryhmanVEO2") + ": ";
        } catch (SQLException poikkeus) {
            istunto.setAttribute(SessioApuri.Virhe, bundle.getString("tkvirhe") + ": " + poikkeus.getLocalizedMessage());
            return;
        }
        if (tulos == -1) {
            istunto.setAttribute(SessioApuri.Virhe, virheilmoituksenAlku + bundle.getString("ryhmaa") + uusiRyhma + bundle.getString("eiMaaritelty"));
        } else if (tulos == -2) {
            istunto.setAttribute(SessioApuri.Virhe, virheilmoituksenAlku + bundle.getString("opiskelijanVoimassaolevaa"));
        } else if (tulos == -3) {
            istunto.setAttribute(SessioApuri.Virhe, virheilmoituksenAlku + bundle.getString("opiskelijalaskuri"));
        } else if (tulos == -4) {
            istunto.setAttribute(SessioApuri.Virhe, virheilmoituksenAlku + bundle.getString("opiskelijalaskuriKasvattaminen"));
        } else {
        }
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
    
    private static String palautaOpiskelijanNimi(String opiskelijanumero) throws SQLException {
        Opiskelija palautettava = OpiskelijaKyselyt.opiskelijaHetulla(opiskelijanumero);
        return palautettava.getEtunimi() + " " + palautettava.getSukunimi();
    }
}