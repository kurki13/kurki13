/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.osasuoritukset;

import java.util.Iterator;
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
public class LaskaritTest {
    
    public LaskaritTest() {
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
    public void testLaskari() {
        System.out.println("laskari");
        int laskariNumero = 0;
        Laskarit instance = null;
        Laskarit.Laskari expResult = null;
        Laskarit.Laskari result = instance.laskari(laskariNumero);
        assertEquals(expResult, result);
    }

    @Test
    public void testTietokantaString() {
        System.out.println("tietokantaString");
        Laskarit instance = null;
        String expResult = "";
        String result = instance.tietokantaString();
        assertEquals(expResult, result);
    }

    @Test
    public void testIterator() {
        System.out.println("iterator");
        Laskarit instance = null;
        Iterator expResult = null;
        Iterator result = instance.iterator();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Laskarit.main(args);
        fail("The test case is a prototype.");
    }

}