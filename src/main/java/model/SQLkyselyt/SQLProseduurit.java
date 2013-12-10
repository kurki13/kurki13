/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.SQLkyselyt;

import view.util.SessioApuri;
import database.DatabaseConnection;
import model.Kurssi;
import model.Opiskelija;
import view.util.Jaadytys;
import view.util.Lokalisaatio;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.Osallistuminen;

/**
 * Luokka SQL-Proseduurien suorittamiseen.
 */
public class SQLProseduurit {

    /**
     * Metodin avulla suoritetaan kurssin jäädytys. Jos jäädytys onnistui, niin
     * istuntoon asetetaan onnistumisviesti. Jos jäädytys epäonnistui, niin
     * istuntoon asetetaan virheilmoitus.
     *
     * @param kurssi Jäädytettävä kurssi
     * @param kurssinTila Jäädytettävän kurssin tila ennen jäädytystä
     * @param osallistumisetKurssilla Lista jäädytettävän kurssin osallistujista
     * @param request
     */
    public static void suoritaJaadytys(Kurssi kurssi, String kurssinTila, List<Osallistuminen> osallistumisetKurssilla, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        Lokalisaatio bundle = Lokalisaatio.bundle(request);

        try {
            String tulos = suoritaJaadytys(kurssi);
            tarkastaJaadytyksenTulos(tulos, request, bundle, kurssi, kurssinTila, osallistumisetKurssilla);
        } catch (SQLException poikkeus) {
            String virhe = bundle.getString("tkvirhe") + ": " + poikkeus.getLocalizedMessage();
            SessioApuri.annaVirhe(istunto, virhe);
        }
    }

    /**
     * Metodi suorittaa SQL-Proseduurin jaadytys.
     *
     * @param kurssi Jäädytettävä kurssi
     * @return Merkkijono "OK" jos jäädytys onnistui
     * @throws SQLException Tietokantavirhe
     */
    private static String suoritaJaadytys(Kurssi kurssi) throws SQLException {
        Connection tietokantayhteys = DatabaseConnection.makeConnection();
        CallableStatement kutsuttavaLause = tietokantayhteys.prepareCall("{ ? = call jaadytys (?, ?, ?, ?, ?, ?) }");
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
     * Metodi tarkastaa SQL-Proseduurin jaadytys palautusarvon ja asettaa
     * onnistumisviestin tai virheilmoituksen asianmukaisesti. Jos jäädytys
     * onnistui, lähetetään jäädytyksestä tiedottava sähköpostiviesti
     * asianmukaisille tahoille.
     *
     * @param tulos Tarkastettava palautusarvo
     * @param request
     * @param bundle Lokalisaatio työkalu
     * @param kurssi Jäädytettävä kurssi
     * @param kurssinTila Jäädytettävän kurssin tila ennen jäädytystä
     * @param osallistumisetKurssilla Lista jäädytettävän kurssin osallistujista
     */
    private static void tarkastaJaadytyksenTulos(String tulos, HttpServletRequest request, Lokalisaatio bundle, Kurssi kurssi, String kurssinTila, List<Osallistuminen> osallistumisetKurssilla) {
        HttpSession istunto = request.getSession();
        if (tulos.equals("OK")) {
            String viesti = bundle.getString("jaadytysInfo") + ". " + bundle.getString("jaadytysInfo3") + " "
                    + bundle.getString("jaadytysInfo4");
            Jaadytys.lahetaPostia(kurssi, kurssinTila, osallistumisetKurssilla, request);
            SessioApuri.annaViesti(istunto, viesti);
        } else {
            String virhe = bundle.getString("jaadytysEpaonnistui") + ". " + bundle.getString("jaadytysEpaonnistuiInfo")
                    + " " + "<a href=\"mailto:tktl-kurki@cs.helsinki.fi\">tktl-kurki@cs.helsinki.fi</a>";
            SessioApuri.annaVirhe(istunto, virhe);
        }
    }

    /**
     * Metodin avulla suoritetaan kurssin arvostelu. Jos arvostelu onnistui,
     * niin istuntoon asetetaan onnistumisviesti. Jos arvostelu epäonnistui,
     * niin istuntoon asetetaan virheilmoitus.
     *
     * @param kurssi Arvosteltava kurssi
     * @param arvostelija Arvosteltavan kurssin arvostelija
     * @param request
     */
    public static void suoritaArvostelu(Kurssi kurssi, String arvostelija, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        Lokalisaatio bundle = Lokalisaatio.bundle(request);

        if (kurssi.getSuoritus_pvm() == null) {
            String virhe = bundle.getString("arvosteluEiOnnistunut") + ": " + bundle.getString("kurssilleEiAsetettuSuorpvm");
            SessioApuri.annaVirhe(istunto, virhe);
            return;
        }

        try {
            int tulos = suoritaArvostelu(kurssi, arvostelija);
            tarkastaArvostelunTulos(tulos, istunto, bundle);
        } catch (SQLException poikkeus) {
            String virhe = bundle.getString("tkvirhe") + ": " + poikkeus.getLocalizedMessage();
            SessioApuri.annaVirhe(istunto, virhe);
        }
    }

    /**
     * Metodi suorittaa SQL-Proseduurin arvostelu05 funktion arvostele.
     *
     * @param kurssi Arvosteltava kurssi
     * @param arvostelija Arvosteltavan kurssin arvostelija
     * @return Kokonaisluku 0, jos arvostelu onnistui
     * @throws SQLException Tietokantavirhe
     */
    private static int suoritaArvostelu(Kurssi kurssi, String arvostelija) throws SQLException {
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
     * Metodi tarkastaa SQL-Proseduurin arvostelu05 funktion arvostele
     * palautusarvon ja asettaa onnistumisviestin tai virheilmoituksen
     * asianmukaisesti.
     *
     * @param tulos Tarkastettava palautusarvo
     * @param istunto Käyttäjän istunto
     * @param bundle Lokalisaatio työkalu
     */
    private static void tarkastaArvostelunTulos(int tulos, HttpSession istunto, Lokalisaatio bundle) {
        if (tulos == 0) {
            String viesti = bundle.getString("arvosteluSuor") + ".";
            SessioApuri.annaViesti(istunto, viesti);
        } else {
            String virhe = bundle.getString("virhe") + ": " + bundle.getString("arvosteluEiOnnistunut") + "(" + tulos + ").";
            SessioApuri.annaVirhe(istunto, virhe);
        }
    }

    /**
     * Metodin avulla palautetaan opiskelija kurssille. Jos palautus onnistui,
     * niin istuntoon asetetaan onnistumisviesti. Jos palautus epäonnistui, niin
     * istuntoon asetetaan virheilmoitus.
     *
     * @param kurssi Kurssi, jolle opiskelija palautetaan
     * @param ryhmanNumero Ryhmä, johon opiskelija palautetaan
     * @param opiskelijanumero Palautettavan opiskelijan opiskelijanumero
     * @param request
     */
    public static void palautaOpiskelija(Kurssi kurssi, int ryhmanNumero, String opiskelijanumero, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        Lokalisaatio bundle = Lokalisaatio.bundle(request);

        try {
            int tulos = palautaOpiskelija(kurssi, ryhmanNumero, opiskelijanumero);
            tarkastaPalautuksenTulos(tulos, istunto, bundle, opiskelijanumero);
        } catch (SQLException poikkeus) {
            String virhe = bundle.getString("tkvirhe") + ": " + poikkeus.getLocalizedMessage();
            SessioApuri.annaVirhe(istunto, virhe);
        }
    }

    /**
     * Metodi suorittaa SQL-Proseduurin palautaopiskelija.
     *
     * @param kurssi Kurssi, jolle opiskelija palautetaan
     * @param ryhmanNumero Ryhmä, johon opiskelija palautetaan
     * @param opiskelijanumero Palautettavan opiskelijan opiskelijanumero
     * @return Kokonaisluku 0, jos palautus onnistui
     * @throws SQLException Tietokantavirhe
     */
    private static int palautaOpiskelija(Kurssi kurssi, int ryhmanNumero, String opiskelijanumero)
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
     * Metodi tarkastaa SQL-Proseduurin palautaopiskelija palautusarvon ja
     * asettaa onnistumisviestin tai virheilmoituksen asianmukaisesti.
     *
     * @param tulos Tarkastettava palautusarvo
     * @param istunto Käyttäjän istunto
     * @param bundle Lokalisaatio työkalu
     * @param opiskelijanumero Palautettavan opiskelijan opiskelijanumero
     * @throws SQLException Tietokantavirhe
     */
    private static void tarkastaPalautuksenTulos(int tulos, HttpSession istunto, Lokalisaatio bundle, String opiskelijanumero) throws SQLException {
        String virhe;
        String virheilmoituksenAlku = bundle.getString("palauttaminenKEO") + ": ";

        if (tulos == 1) {
            virhe = virheilmoituksenAlku + bundle.getString("palautusEiOnnistunut");
            SessioApuri.annaVirhe(istunto, virhe);
        } else if (tulos == 2) {
            virhe = virheilmoituksenAlku + bundle.getString("opiskelijalaskuriEpaonnistui");
            SessioApuri.annaVirhe(istunto, virhe);
        } else {
            String viesti = bundle.getString("opiskelija") + " " + palautaOpiskelijanNimi(opiskelijanumero) + " " + bundle.getString("palautettuK") + ".";
            SessioApuri.annaViesti(istunto, viesti);
        }
    }

    /**
     * Metodin avulla poistetaan opiskelija kurssilta. Jos poistaminen onnistui,
     * niin istuntoon asetetaan onnistumisviesti. Jos poistaminen epäonnistui,
     * niin istuntoon asetetaan virheilmoitus.
     *
     * @param kurssi Kurssi, jolta opiskelija poistetaan
     * @param ryhmanNumero Ryhmä, jolta opiskelija poistetaan
     * @param opiskelijanumero Poistettavan opiskelijan opiskelijanumero
     * @param request
     */
    public static void poistaOpiskelija(Kurssi kurssi, int ryhmanNumero, String opiskelijanumero, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        Lokalisaatio bundle = Lokalisaatio.bundle(request);

        try {
            int tulos = poistaOpiskelija(kurssi, ryhmanNumero, opiskelijanumero);
            tarkastaPoistonTulos(tulos, istunto, bundle, opiskelijanumero);
        } catch (SQLException poikkeus) {
            String virhe = bundle.getString("tkvirhe") + ": " + poikkeus.getLocalizedMessage();
            SessioApuri.annaVirhe(istunto, virhe);
        }
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

    /**
     * Metodi tarkastaa SQL-Proseduurin poistaopiskelija palautusarvon ja
     * asettaa onnistumisviestin tai virheilmoituksen asianmukaisesti.
     *
     * @param tulos Tarkastettava palautusarvo
     * @param istunto Käyttäjän istunto
     * @param bundle Lokalisaatio työkalu
     * @param opiskelijanumero Poistettavan opiskelijan opiskelijanumero
     * @throws SQLException Tietokantavirhe
     */
    private static void tarkastaPoistonTulos(int tulos, HttpSession istunto, Lokalisaatio bundle, String opiskelijanumero) throws SQLException {
        String virhe;
        String virheilmoituksenAlku = bundle.getString("kurssiltaPoistoEO") + ": ";

        if (tulos == 1) {
            virhe = virheilmoituksenAlku + bundle.getString("poistaminenEiOnnistunut");
            SessioApuri.annaVirhe(istunto, virhe);
        } else if (tulos == 2) {
            virhe = virheilmoituksenAlku + bundle.getString("opiskelijalaskuriEpaonnistui");
            SessioApuri.annaVirhe(istunto, virhe);
        } else {
            String viesti = bundle.getString("opiskelija") + " " + palautaOpiskelijanNimi(opiskelijanumero) + " " + bundle.getString("poistettuK") + ".";
            SessioApuri.annaViesti(istunto, viesti);
        }
    }

    /**
     * Metodin avulla lisätään opiskelija kurssille. Jos lisääminen onnistui,
     * niin istuntoon asetetaan onnistumisviesti. Jos lisääminen epäonnistui,
     * niin istuntoon asetetaan virheilmoitus.
     *
     * @param kurssi Kurssi, jolle opiskelija lisätään
     * @param ryhmanNumero Ryhmä, jolle opiskelija lisätään
     * @param opiskelijanumero Lisättävän opiskelijan opiskelijanumero
     * @param request
     */
    public static void lisaaOpiskelija(Kurssi kurssi, int ryhmanNumero, String opiskelijanumero, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        Lokalisaatio bundle = Lokalisaatio.bundle(request);

        try {
            String tulos = lisaaOpiskelija(kurssi, ryhmanNumero, opiskelijanumero);
            tarkastaLisayksenTulos(tulos, istunto, bundle, opiskelijanumero);
        } catch (SQLException poikkeus) {
            String virhe = bundle.getString("tkvirhe") + ": " + poikkeus.getLocalizedMessage();
            SessioApuri.annaVirhe(istunto, virhe);
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

    /**
     * Metodi tarkastaa SQL-Proseduurin kurkiilmo palautusarvon ja asettaa
     * onnistumisviestin tai virheilmoituksen asianmukaisesti.
     *
     * @param tulos Tarkastettava palautusarvo
     * @param istunto Käyttäjän istunto
     * @param bundle Lokalisaatio työkalu
     */
    private static void tarkastaLisayksenTulos(String tulos, HttpSession istunto, Lokalisaatio bundle, String hetu) {
        String virhe;
        String virheilmoituksenAlku = bundle.getString("lisaysKEO") + ": ";

        if (tulos.equals("ok")) {
            String viesti = hetu + " " + bundle.getString("lisaysInfo") + ".";
            SessioApuri.annaViesti(istunto, viesti);
        } else if (tulos.equals("virhe: opiskelija on jo ilmoittautunut kurssille.")) {
            virhe = virheilmoituksenAlku + bundle.getString("opiskelijaojik") + " " + hetu + ".";
            SessioApuri.annaVirhe(istunto, virhe);
        } else {
            virhe = virheilmoituksenAlku + bundle.getString("tkvirhe");
            SessioApuri.annaVirhe(istunto, virhe);
        }
    }

    /**
     * Metodin avulla vaihdetaan opiskelijan ryhmää kurssilla. Jos ryhmänvaihto
     * onnistui, niin istuntoon asetetaan onnistumisviesti. Jos ryhmänvaihto
     * epäonnistui, niin istuntoon asetetaan virheilmoitus.
     *
     * @param uusiRyhma Ryhmä, johon vaihdetaan
     * @param kurssi Kurssi, jossa ryhmää vaihdetaan
     * @param ryhmanNumero Vanha ryhmä
     * @param opiskelijanumero Ryhmää vaihtavan opiskelijan opiskelijanumero
     * @param request
     */
    public static void vaihdaOpiskelijanRyhmaa(int uusiRyhma, Kurssi kurssi, int ryhmanNumero, String opiskelijanumero, HttpServletRequest request) {
        HttpSession istunto = request.getSession();
        Lokalisaatio bundle = Lokalisaatio.bundle(request);

        try {
            String virheilmoituksenAlku = bundle.getString("ryhmanVEO") + " " + palautaOpiskelijanNimi(opiskelijanumero) + " " + bundle.getString("ryhmanVEO2") + ": ";
            int tulos = vaihdaOpiskelijanRyhmaa(uusiRyhma, kurssi, ryhmanNumero, opiskelijanumero);
            tarkastaVaihdonTulos(tulos, istunto, bundle, uusiRyhma, opiskelijanumero, virheilmoituksenAlku);
        } catch (SQLException poikkeus) {
            String virhe = bundle.getString("tkvirhe") + ": " + poikkeus.getLocalizedMessage();
            SessioApuri.annaVirhe(istunto, virhe);
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
    private static int vaihdaOpiskelijanRyhmaa(int uusiRyhma, Kurssi kurssi, int ryhmanNumero, String opiskelijanumero) throws SQLException {
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

    /**
     * Metodi tarkastaa SQL-Proseduurin ryhmavaihto palautusarvon ja asettaa
     * onnistumisviestin tai virheilmoituksen asianmukaisesti.
     *
     * @param tulos Tarkastettava palautusarvo
     * @param istunto Käyttäjän istunto
     * @param bundle Lokalisaatio työkalu
     * @param uusiRyhma Ryhmä, johon opiskelija vaihdetaan SQL-Proseduurissa
     * @param opiskelijanumero SQL-Proseduurissa ryhmää vaihtavan opiskelijan
     * opiskelijanumero
     * @throws SQLException Tietokantavirhe
     */
    private static void tarkastaVaihdonTulos(int tulos, HttpSession istunto, Lokalisaatio bundle, int uusiRyhma, String opiskelijanumero, String virheilmoituksenAlku) throws SQLException {
        String virhe;

        if (tulos == -1) {
            virhe = virheilmoituksenAlku + bundle.getString("ryhmaa") + uusiRyhma + bundle.getString("eiMaaritelty");
        } else if (tulos == -2) {
            virhe = virheilmoituksenAlku + bundle.getString("opiskelijanVoimassaolevaa");
        } else if (tulos == -3) {
            virhe = virheilmoituksenAlku + bundle.getString("opiskelijalaskuri");
        } else if (tulos == -4) {
            virhe = virheilmoituksenAlku + bundle.getString("opiskelijalaskuriKasvattaminen");
        } else {
            String viesti = bundle.getString("ryhmanVO") + " " + palautaOpiskelijanNimi(opiskelijanumero) + " " + bundle.getString("ryhmanVO2");
            SessioApuri.annaViesti(istunto, viesti);
            return;
        }

        SessioApuri.annaVirhe(istunto, virhe);
    }

    /**
     * Metodi palauttaa opiskelijanumeroa vastaavan opiskelijan etu- ja
     * sukunimen.
     *
     * @param opiskelijanumero Palautettavan opiskelijan opiskelijanumero
     * @return Palautettavan opiskelijan etunimi ja sukunimi
     * @throws SQLException Tietokantavirhe
     */
    public static String palautaOpiskelijanNimi(String opiskelijanumero) throws SQLException {
        Opiskelija palautettava = OpiskelijaKyselyt.opiskelijaHetulla(opiskelijanumero);
        return palautettava.getEtunimi() + " " + palautettava.getSukunimi();
    }
}
