/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.util;

import static org.mockito.Mockito.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.Kurssi;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuorituspaivamaaraTest {
    Kurssi kurssi;
    HttpServletRequest request;
    HttpSession istunto;
    Lokalisaatio bundle;
    
    public SuorituspaivamaaraTest() {
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
        request = mock(HttpServletRequest.class);
        istunto = mock(HttpSession.class);
        bundle = mock(Lokalisaatio.class);
        bundle.setLocale(new Locale("fi"));
        bundle.setRequest(request);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void onkoSuorituspvmNullToimiiOikein() {
        kurssi.setSuoritus_pvm(null);
        assertTrue(Suorituspaivamaara.onkoSuorituspvmNull(kurssi));
        kurssi.setSuoritus_pvm(new Date(112, 11, 24));
        assertFalse(Suorituspaivamaara.onkoSuorituspvmNull(kurssi));
    }
    
    @Test
    public void palautaSuorituspvmToimiiOikein() {
        kurssi.setSuoritus_pvm(null);
        assertEquals(Suorituspaivamaara.palautaSuorituspvm(kurssi), "");
        kurssi.setSuoritus_pvm(new Date(112, 11, 24));
        assertEquals(Suorituspaivamaara.palautaSuorituspvm(kurssi), "24.12.2012");
    }
    
    @Test
    public void palautaSuorituspvmnYlarajaToimiiOikein() {
        kurssi.setPaattymis_pvm(new Date(113, 11, 24));
        assertEquals(Suorituspaivamaara.palautaSuorituspvmnYlaraja(kurssi), "24.02.2014");
        kurssi.setPaattymis_pvm(new Date(111, 11, 24));
        Calendar ylaraja = Calendar.getInstance();
        ylaraja.add(Calendar.MONTH, 2);
        SimpleDateFormat muotoilu = new SimpleDateFormat("dd.MM.yyyy");
        assertEquals(Suorituspaivamaara.palautaSuorituspvmnYlaraja(kurssi), muotoilu.format(ylaraja.getTime()));
        kurssi.setPaattymis_pvm(null);
        assertNull(Suorituspaivamaara.palautaSuorituspvmnYlaraja(kurssi));
    }
    
    /*
    @Test
    public void tarkastaSuorituspvmPalauttaaNullJosSuoritusPvmAnnettuVirheellisessaMuodossa() {
        kurssi.setSuoritus_pvm(null);
        Mockito.when(istunto.getAttribute(SessioApuri.Virhe)).thenReturn(new ArrayList());
        //assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "24x12.2013", istunto, bundle));
    }
    */
}