/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mkctammi
 */
public class Lokalisaatio {

    public static Lokalisaatio bundle(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute(SessioApuri.LokaaliTunnus) == null) {
            session.setAttribute(SessioApuri.LokaaliTunnus, "fi");
        }
        Lokalisaatio lb = new Lokalisaatio();
        lb.setLocale(new Locale((String) session.getAttribute(SessioApuri.LokaaliTunnus)));
        lb.setRequest(request);
        return lb;
    }

    private Locale locale = new Locale("fi");
    private HttpServletRequest rq;

    public void setRequest(HttpServletRequest request) {
        this.rq = request;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getString(String request) {
        try {
            return ResourceBundle.getBundle("localisationBundle", locale).getString(request);
        } catch (MissingResourceException me) {
            ResourceBundle.clearCache();
            try {
                return ResourceBundle.getBundle("localisationBundle", locale).getString(request);
            } catch (MissingResourceException me2) {
                SessioApuri.annaVirhe(rq.getSession(), "Missing resource key for: " + request + " (" + locale.getLanguage() + ")");
                return "missingkey(" + request + "," + locale.getLanguage() + ")";
            }
        }
    }
}
