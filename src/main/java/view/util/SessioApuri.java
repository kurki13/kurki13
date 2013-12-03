/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.util;

import database.Testikanta;
import model.Kurssi;
import model.SQLkyselyt.KurssiKyselyt;
import model.SQLkyselyt.OpiskelijaKyselyt;
import model.SQLkyselyt.OsallistuminenKyselyt;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.Table;

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
    public final static String Viesti = "message";
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
                Lokalisaatio bundle = Lokalisaatio.bundle(request);
                annaVirhe(session, bundle.getString("kurssinLataaminenEpäonnistui") + sq.getLocalizedMessage());
                session.setAttribute(KurssiLista, new ArrayList());
            }
        }
    }

    public static void annaVirhe(HttpSession session, String virhe) {
        if (session.getAttribute(Virhe) == null) {
            session.setAttribute(Virhe, new ArrayList());
        }
        List<String> virheet = (List) session.getAttribute(Virhe);
        virheet.add(virhe);
    }

    public static List<String> haeVirheet(HttpServletRequest request) {
        return (List) request.getSession().getAttribute(Virhe);
    }

    public static void annaViesti(HttpSession session, String virhe) {
        if (session.getAttribute(Viesti) == null) {
            session.setAttribute(Viesti, new ArrayList());
        }
        List<String> viestit = (List) session.getAttribute(Viesti);
        viestit.add(virhe);
    }

    public static List<String> haeViestit(HttpServletRequest request) {
        return (List) request.getSession().getAttribute(Viesti);
    }

    public static void auta(HttpServletRequest request) {
        HttpSession session = request.getSession();
        lataa_kurssit(request);
        kasitteleGetParametrit(request);

        Kurssi valittuKurssi = (Kurssi) session.getAttribute(ValittuKurssi);
        if (valittuKurssi != null) {
            lataaKurssinOpiskelijat(request, valittuKurssi);
            lataaKurssinOsallistumiset(request, valittuKurssi);
        }
    }

    private static void lataaKurssinOpiskelijat(HttpServletRequest rqst, Kurssi valittuKurssi) {
        HttpSession session = rqst.getSession();
        try {
            session.setAttribute(
                    KurssinOpiskelijat,
                    OpiskelijaKyselyt.kurssinOpiskelijat(valittuKurssi));
        } catch (SQLException ex) {
            Lokalisaatio bundle = Lokalisaatio.bundle(rqst);
            annaVirhe(session, bundle.getString("opiskelijoidenLataamisVirhe") + ex.getLocalizedMessage());
            Logger.getLogger(SessioApuri.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void lataaKurssinOsallistumiset(HttpServletRequest rqst, Kurssi valittuKurssi) {
        HttpSession session = rqst.getSession();
        try {
            session.setAttribute(
                    KurssinOsallistumiset,
                    OsallistuminenKyselyt.osallistumisetKurssilla(valittuKurssi));
        } catch (SQLException ex) {
            Lokalisaatio bundle = Lokalisaatio.bundle(rqst);
            annaVirhe(session, bundle.getString("osallistumisLataamisVirhe") + ex.getLocalizedMessage());
            Logger.getLogger(SessioApuri.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void kasitteleGetParametrit(HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (request.getParameter(LokaaliGet) != null) {
            session.setAttribute(LokaaliTunnus, request.getParameter(LokaaliGet));
        }
        if (request.getParameter(KurssiGet) != null) {
            session.setAttribute(ValitunKurssinId, request.getParameter(KurssiGet));
            session.setAttribute(ValittuKurssi,
                    etsiKurssiListasta((List<Kurssi>) session.getAttribute(KurssiLista),
                    request.getParameter(KurssiGet), request));
        }
        if (request.getParameter(ToimintoGet) != null) {
            session.setAttribute(Toiminto, request.getParameter(ToimintoGet));
        }
    }

    private static Kurssi etsiKurssiListasta(List<Kurssi> kurssit, String id, HttpServletRequest rqst) {
        if (id == null) {
            return null;
        }
        HttpSession session = rqst.getSession();
        String kurssikoodi, tyyppi, lukukausi;
        int lukuvuosi, kurssi_nro;
        try {
            kurssikoodi = id.split("\\.")[0];
            lukukausi = id.split("\\.")[1];
            lukuvuosi = Integer.parseInt(id.split("\\.")[2]);
            tyyppi = id.split("\\.")[3];
            kurssi_nro = Integer.parseInt(id.split("\\.")[4]);
        } catch (Exception ex) {
            Lokalisaatio bundle = Lokalisaatio.bundle(rqst);
            annaVirhe(session, bundle.getString("getParametriVrihe") + id);
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
        Lokalisaatio bundle = Lokalisaatio.bundle(rqst);
        annaVirhe(session, bundle.getString("kurssiEiLoytynytKayttajalle"));
        return null;
    }

    public String print(HttpServletRequest rq) {
        Lokalisaatio bundle = Lokalisaatio.bundle(rq);
        HttpSession sesh = rq.getSession();
        String ret = "";
        ret += bundle.getString("valittuKurssi") + sesh.getAttribute(ValittuKurssi);
        return ret;
    }

    public static String lisaysLause(Table taulu) {
        if (taulu == null) {
            return "";
        }
        return Testikanta.taulunLisaysLause(taulu).replaceAll("(\r\n|\n\r|\r|\n)", "<br />");
    }

    public static Kurssi valittuKurssi(HttpServletRequest request) {
        return (Kurssi) request.getSession().getAttribute(ValittuKurssi);
    }
}
