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
    public static String getString(String request) {
        return ResourceBundle.getBundle("localisationBundle", new Locale("fi")).getString(request);
    }
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("localisationBundle", new Locale("fi"));
    }
}
