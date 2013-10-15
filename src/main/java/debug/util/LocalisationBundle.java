/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author mkctammi
 */
public class LocalisationBundle {

    private Locale locale = new Locale("fi");

    public LocalisationBundle(Locale locale) {
        this.locale = locale;
    }

    public String getString(String request) {
        return ResourceBundle.getBundle("localisationBundle", locale).getString(request);
    }

    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle("localisationBundle", locale);
    }
}
