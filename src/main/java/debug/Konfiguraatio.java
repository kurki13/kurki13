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

    private static ResourceBundle prop;
    
    public static void test() {
        prop = ResourceBundle.getBundle("kurki");
        System.out.println(prop.getString("webmaster"));
    }
}
