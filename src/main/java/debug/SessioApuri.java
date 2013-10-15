/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug;

import debug.model.SQLkyselyt.KurssiKyselyt;
import debug.util.LocalisationBundle;
import java.sql.SQLException;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mkctammi
 */
public class SessioApuri {

    /**
     * Ladataan kurssilistat sessiomuuttujaan courses_loaded, jos ei ole jo
     * tehty. Mikäli kirjautunut käyttäjä löytyy ylläpitäjälistasta, ladataan
     * iso lista kursseja.
     *
     * @param request
     */
    public static void lataa_kurssit(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("remote_user", request.getRemoteUser());

        if (session.getAttribute("courses_loaded") == null) {
            try {
                if (request.getRemoteUser().equals("admin")) {
                    session.setAttribute("user_courses", KurssiKyselyt.kurssitYllapitajalle());
                } else {
                    session.setAttribute("user_courses", KurssiKyselyt.kurssitKayttajalle(request.getRemoteUser()));
                }

            } catch (SQLException sq) {
                session.setAttribute("Error", sq.getLocalizedMessage());
            }
            session.setAttribute("courses_loaded", true);
        }
    }

    public static LocalisationBundle bundle(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String locale = (String) session.getAttribute("selected_locale");
        if (locale == null) {
            locale = "fi";
            session.setAttribute("selected_locale", locale);
        }
        return new LocalisationBundle(new Locale(locale));
    }

    public static void auta(HttpServletRequest request) {
        lataa_kurssit(request);
    }
}
