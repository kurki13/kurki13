/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.osasuoritukset;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author esaaksvu
 */
public class OsasuorituksetTest {

    String max = "99, 4, 5, 4, 4-00,00,00,00,00-00,00,00,00,00-00,00,00";
    String cur = " 1, 3, 4, 2, 1-00,00,00,00,00-00,00,00,00,00-00,00,00";
    int akt = 13;
    Osasuoritukset os;

    @Before
    public void setUp() {
        os = new Osasuoritukset(max, cur, akt);
    }

    @Test
    public void testGetYhteispisteet() {
        assertEquals(11, os.getYhteispisteet());
    }

    @Test
    public void testLuoVirheellinenPisteet() {
        os = new Osasuoritukset(max, "hei", akt);
        assertEquals("??,??,??,??,??-??,??,??,??,??-??,??,??,??,??-??,??,??", os.pisteetTietokantamuodossa());
    }

    @Test
    public void testLuoVirheellinenMax() {
        os = new Osasuoritukset("01,hei", cur, akt);
        assertEquals(" 1,??,??,??,??-??,??,??,??,??-??,??,??,??,??-??,??,??", os.pisteetTietokantamuodossa());
    }
   
    @Test
    public void testLuoPitkaString() {
        String s = " 1, 3, 4, 2, 1-00,00,00,00,00-00,00,00,00,00-00,00,00, 1";
        os = new Osasuoritukset(max, cur, akt);
        assertEquals(" 1, 3, 4, 2, 1- 0, 0, 0, 0, 0- 0, 0, 0, 0, 0- 0, 0, 0", os.pisteetTietokantamuodossa());
    }
    
    @Test
    public void testLiianSuuriPiste() {
        os = new Osasuoritukset(max, " 1, 5", akt);
        assertEquals(" 1, 4,??,??,??-??,??,??,??,??-??,??,??,??,??-??,??,??", os.pisteetTietokantamuodossa());
    }

    @Test
    public void testPisteetTietokantamuodossa() {
        String exp = " 1, 3, 4, 2, 1- 0, 0, 0, 0, 0- 0, 0, 0, 0, 0- 0, 0, 0";
        assertEquals(exp, os.pisteetTietokantamuodossa());
    }

    @Test
    public void testOsa_int() {
        assertEquals(4, os.osa(3).getPisteet());
        assertEquals(5, os.osa(3).getMaxPisteet());
    }

    @Test
    public void testOsa_intFailaa() {
        assertNull(os.osa(19));
        assertNull(os.osa(19));
    }

    @Test
    public void testOsa_String() {
        assertEquals(4, os.osa("3").getPisteet());
        assertEquals(5, os.osa("3").getMaxPisteet());
    }

    @Test
    public void testOsa_StringFailaa() {
        assertNull(os.osa("Pööpöö"));
        assertNull(os.osa("Höhöh"));
    }
}