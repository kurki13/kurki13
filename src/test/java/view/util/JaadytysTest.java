/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.util;

import java.sql.Date;
import model.Kurssi;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JaadytysTest {
    Kurssi kurssi;
    
    public JaadytysTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        kurssi = new Kurssi();
    }
    
    @After
    public void tearDown() {
    }
    
    
    @Test
    public void tarkastaSuorituspvmToimiiOikein() {
        kurssi.setSuoritus_pvm(new Date(112, 11, 24));
        assertTrue(Jaadytys.tarkastaSuorituspvm(kurssi));
        kurssi.setSuoritus_pvm((new Date(114, 11, 24)));
        assertFalse(Jaadytys.tarkastaSuorituspvm(kurssi));
    }
    
}