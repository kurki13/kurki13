/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug;

import debug.model.SQLkyselyt.KurssiKyselyt;
import debug.model.SQLkyselyt.OsallistuminenKyselyt;
import java.sql.SQLException;
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
                if (request.getRemoteUser().equals("admin")) {
                    session.setAttribute("user_courses", KurssiKyselyt.kurssitYllapitajalle());
                } else {
                    session.setAttribute("user_courses", KurssiKyselyt.kurssitKayttajalle(request.getRemoteUser()));
                }
               
                session.setAttribute("selected_course_id", null);
                session.setAttribute("selected_course_students", null);
                session.setAttribute("selected_service_id", null);

            } catch (SQLException sq) {
                session.setAttribute("Error", sq.getLocalizedMessage());
            }
            session.setAttribute("courses_loaded", true);
        }
    }
}
