/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug;

import java.util.ResourceBundle;

/**
 *
 * @author mkctammi
 */
public class Konfiguraatio {

    private static ResourceBundle prop = ResourceBundle.getBundle("kurki");
    
    public static String getString(String property) {
        return prop.getString(property);
    }
}
