/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.osasuoritukset;

import debug.model.Kurssi;
import debug.model.Osallistuminen;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author esaaksvu
 */
public class MuotoilijaTest {

    public MuotoilijaTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testStringToIntArray() {
        System.out.println("stringToIntArray");
        String s = "3,5,??,??,??-??,??,??,??,??-??,??,??,??,??-??,??,??";
        int[] expResult = {3, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] result = Muotoilija.stringToIntArray(s);
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testIntArrayToString() {
        System.out.println("intArrayToString");
        int[] ints = {1,2,3};
        String expResult = "1,2,3";
        String result = Muotoilija.intArrayToString(ints);
        assertEquals(expResult, result);
    }

    @Test
    public void testTyhjaTaulu() {
        System.out.println("tyhjaTaulu");
        int[] expResult = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] result = Muotoilija.tyhjaTaulu();
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testTietokantaString() {
        String max = "10,??,??,??,??-??,??,??,??,??-??,??,??,??,??-??,??,??";
        String done = "5,??,??,??,??-??,??,??,??,??-??,??,??,??,??-??,??,??";
        
        
        Kurssi k = new Kurssi();
        k.setValue(Kurssi.laskarikerta_lkm, 10);
        k.setValue(Kurssi.laskaritehtava_lkm, max);
        Osallistuminen os = new Osallistuminen();
        os.setKurssi(k);
        os.setValue(Osallistuminen.laskarisuoritukset, done);
        List<Laskarit.Laskari> laskarit = os.getLaskarit().laskarit;
        String expResult = done;
        String result = Muotoilija.tietokantaString(laskarit);
        assertEquals(expResult, result);
    }
}