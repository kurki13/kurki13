/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

public class OpetusKyselyt {
    // Tätä ei välttämättä tarvita hh
    public static final String lataaRyhmat = "SELECT ilmo_jnro FROM opetus WHERE \n" 
            + "kurssikoodi = ? \n"
            + "AND lukukausi = ? \n"
            + "AND lukuvuosi = ? \n"
            + "AND tyyppi = ? \n"
            + "AND kurssi_nro = ? \n";
}
