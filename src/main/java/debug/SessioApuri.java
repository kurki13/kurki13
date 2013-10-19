/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug;

import debug.model.Kurssi;
import debug.model.SQLkyselyt.KurssiKyselyt;
import debug.model.SQLkyselyt.OpiskelijaKyselyt;
import debug.model.SQLkyselyt.OsallistuminenKyselyt;
import debug.util.LocalisationBundle;
import debug.util.QuerystringParser;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessioApuri {

    //Sessiomuuttujien avaimet
    public final static String KurssiLista = "user_courses";
    public final static String ValittuKurssi = "selected_course";
    public final static String ValitunKurssinId = "selected_course_id";
    public final static String Toiminto = "selected_service_id";
    public final static String LokaaliTunnus = "selected_locale";
    public final static String KurssinOpiskelijat = "selected_course_students";
    public final static String KurssinOsallistumiset = "selected_course_parts";
    public final static String Virhe = "error";
    //GET -Parametrien avaimet
    public final static String KurssiGet = "courseId";
    public final static String LokaaliGet = "locale";
    public final static String ToimintoGet = "serviceId";

    /**
     * Ladataan kurssilistat sessiomuuttujaan courses_loaded, jos ei ole jo
     * tehty. Mikäli kirjautunut käyttäjä löytyy ylläpitäjälistasta, ladataan
     * iso lista kursseja.
     *
     * @param request
     */
    private static void lataa_kurssit(HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (session.getAttribute(KurssiLista) == null) {
            try {
                if (request.getRemoteUser().equals("admin")) {
                    session.setAttribute(KurssiLista, KurssiKyselyt.kurssitYllapitajalle());
                } else {
                    session.setAttribute(KurssiLista, KurssiKyselyt.kurssitKayttajalle(request.getRemoteUser()));
                }

            } catch (SQLException sq) {
                session.setAttribute(Virhe, "Kurssien lataaminen kannasta epäonnistui!");
                session.setAttribute(KurssiLista, new ArrayList());
            }
        }
    }

    public static LocalisationBundle bundle(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute(LokaaliTunnus) == null) {
            session.setAttribute(LokaaliTunnus, "fi");
        }
        return new LocalisationBundle(new Locale((String) session.getAttribute(LokaaliTunnus)));
    }

    public static void auta(HttpServletRequest request) {
        HttpSession session = request.getSession();
        lataa_kurssit(request);
        kasitteleGetParametrit(session, request.getQueryString());
        
        Kurssi valittuKurssi = (Kurssi) session.getAttribute(ValittuKurssi);
        if (valittuKurssi != null) {
            lataaKurssinOpiskelijat(session, valittuKurssi);
            lataaKurssinOsallistumiset(session, valittuKurssi);
        }
    }

    private static void lataaKurssinOpiskelijat(HttpSession session, Kurssi valittuKurssi) {
        try {
            session.setAttribute(
                    KurssinOpiskelijat,
                    OpiskelijaKyselyt.kurssinOpiskelijat(valittuKurssi));
        } catch (SQLException ex) {
            session.setAttribute(Virhe, "Kurssin opiskelijoiden lataamisessa tapahtui virhe: " + ex.getLocalizedMessage());
            Logger.getLogger(SessioApuri.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void lataaKurssinOsallistumiset(HttpSession session, Kurssi valittuKurssi) {
        try {
            session.setAttribute(
                    KurssinOsallistumiset,
                    OsallistuminenKyselyt.osallistumisetKurssilla(valittuKurssi));
        } catch (SQLException ex) {
            session.setAttribute(Virhe, "Kurssin osallistumisten lataamisessa tapahtui virhe: " + ex.getLocalizedMessage());
            Logger.getLogger(SessioApuri.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void kasitteleGetParametrit(HttpSession session, String queryString) {
        if (queryString == null) {
            return;
        }
        Map<String, String> params;
        try {
            params = QuerystringParser.split(queryString);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SessioApuri.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (MalformedURLException ex) {
            Logger.getLogger(SessioApuri.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        if (params.containsKey(LokaaliGet)) {
            session.setAttribute(LokaaliTunnus, params.get(LokaaliGet));
        }
        if (params.containsKey(KurssiGet)) {
            session.setAttribute(ValitunKurssinId, params.get(KurssiGet));
            session.setAttribute(ValittuKurssi,
                    etsiKurssiListasta((List<Kurssi>) session.getAttribute(KurssiLista),
                    params.get(KurssiGet), session));
        }
        if (params.containsKey(ToimintoGet)) {
            session.setAttribute(Toiminto, params.get(ToimintoGet));
        }
    }

    private static Kurssi etsiKurssiListasta(List<Kurssi> kurssit, String id, HttpSession session) {
        String kurssikoodi, tyyppi, lukukausi;
        int lukuvuosi, kurssi_nro;
        try {
            kurssikoodi = id.split("\\.")[0];
            lukukausi = id.split("\\.")[1];
            lukuvuosi = Integer.parseInt(id.split("\\.")[2]);
            tyyppi = id.split("\\.")[3];
            kurssi_nro = Integer.parseInt(id.split("\\.")[4]);
        } catch (Exception ex) {
            session.setAttribute(Virhe, "Get-parametrissa annetun kurssitunnuksen muoto virheellinen: " + ex.getLocalizedMessage());
            return null;
        }
        for (Kurssi kurssi : kurssit) {
            if (kurssi.getKurssikoodi().equals(kurssikoodi)
                    && kurssi.getLukukausi().equals(lukukausi)
                    && kurssi.getLukuvuosi() == lukuvuosi
                    && kurssi.getTyyppi().equals(tyyppi)
                    && kurssi.getKurssi_nro() == kurssi_nro) {
                return kurssi;
            }
        }
        session.setAttribute(Virhe, "Annetulla kurssitunnuksella ei löytynyt kurssia tälle käyttäjälle");
        return null;
    }
    
    public String print(HttpServletRequest rq) {
        HttpSession sesh = rq.getSession();
        String ret = "";
        ret += "Valittu kurssi: " + sesh.getAttribute(ValittuKurssi);
        return ret;
    }
}
