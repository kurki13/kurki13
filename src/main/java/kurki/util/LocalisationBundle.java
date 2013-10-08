/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kurki.util;

import java.util.ResourceBundle;
import kurki.Session_;

/**
 *
 * @author mkctammi
 */
public class LocalisationBundle {
    public static String getString(String request) {
        return ResourceBundle.getBundle("localisationBundle", Session_.locale).getString(request);
    }
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("localisationBundle", Session_.locale);
    }
}
