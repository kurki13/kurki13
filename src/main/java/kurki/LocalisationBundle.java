/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kurki;

import java.util.ResourceBundle;

/**
 *
 * @author mkctammi
 */
public class LocalisationBundle {
    public static String getString(String request) {
        return ResourceBundle.getBundle("localisationBundle", Session.locale).getString(request);
    }
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("localisationBundle", Session.locale);
    }
}
