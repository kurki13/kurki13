/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.util;

import debug.SessioApuri;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author mkctammi
 */
public class LocalisationBundle {

    private Locale locale = new Locale("fi");
    private HttpServletRequest rq;

    public void setRequest(HttpServletRequest request) {
        this.rq = request;
    }

    public LocalisationBundle(Locale locale) {
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
                SessioApuri.annaVirhe(rq.getSession(), "Missing resource key for: " + request);
                return "missingkey(" + request + ")";
            }
        }
    }

    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle("localisationBundle", locale);
    }
}
