/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug;

import debug.model.Kurssi;
import debug.model.SQLkyselyt.KurssiKyselyt;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mkctammi
 */
public class SessioApuri {

    public static void auta(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("remote_user", request.getRemoteUser());
        
        if (session.getAttribute("courses_loaded") == null) {
            try {
                session.setAttribute("user_courses", KurssiKyselyt.kurssitYllapitajalle());
                session.setAttribute("selected_course_id", null);
            } catch (SQLException sq) {
                session.setAttribute("Error", sq.getLocalizedMessage());
            }
            session.setAttribute("courses_loaded", true);
        }
    }
}
