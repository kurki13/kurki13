/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.osasuoritukset;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author esaaksvu
 */
public class OsasuoritusTest {
    
    @Test
    public void testSetPisteet() {
        int pisteet = 10;
        Osasuoritus instance = new Osasuoritus(0, 100);
        instance.setPisteet(pisteet);
        assertEquals(pisteet, instance.getPisteet());
    }
    
    @Test(expected=debug.ApplicationException.class)
    public void testSetSuurempiKuinMax() {
        int pisteet = 100;
        Osasuoritus instance = new Osasuoritus(0, 10);
        instance.setPisteet(pisteet);
        assertEquals(0, instance.getPisteet());
    }

    @Test(expected=debug.ApplicationException.class)
    public void testSetNegat() {
        int pisteet = -1;
        Osasuoritus instance = new Osasuoritus(0, 10);
        instance.setPisteet(pisteet);
        assertEquals(0, instance.getPisteet());
    }

    @Test
    public void testPoistaPisteet() {
        Osasuoritus instance = new Osasuoritus(0, 10);
        instance.poistaPisteet();
        assertEquals(instance.getPisteet(), -1);
    }
   
}