/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.util;

import static org.mockito.Mockito.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.Kurssi;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class SuorituspaivamaaraTest {
    Kurssi kurssi;
    HttpServletRequest request;
    HttpSession istunto;
    Lokalisaatio bundle;
    SimpleDateFormat muotoilu;
    
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
        Mockito.when(istunto.getAttribute(SessioApuri.Virhe)).thenReturn(new ArrayList());
        muotoilu = new SimpleDateFormat("dd.MM.yyyy"); 
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void vaihdaPvmnMuotoaToimiiOikein() {
        assertEquals(Suorituspaivamaara.vaihdaPvmnMuotoa("21.01.2012"), "2012-1-21");
        assertEquals(Suorituspaivamaara.vaihdaPvmnMuotoa("21.11.2012"), "2012-11-21");
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
        assertEquals(Suorituspaivamaara.palautaSuorituspvmnYlaraja(kurssi), muotoilu.format(ylaraja.getTime()));
        kurssi.setPaattymis_pvm(null);
        assertNull(Suorituspaivamaara.palautaSuorituspvmnYlaraja(kurssi));
    }
    
    @Test
    public void tarkastaSuorituspvmPalauttaaNullJosSuoritusPvmAnnettuVirheellisessaMuodossa() {
        kurssi.setPaattymis_pvm(new Date(111, 11, 24));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "24x12.2013", istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "24.1262013", istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "24.12.20133", istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "24.12.201", istunto, bundle));
    }
    
    @Test
    public void tarkastaSuorituspvmPalauttaaNullJosSuoritusPaivaAnnettuVirheellisesti() {
        kurssi.setPaattymis_pvm(new Date(111, 11, 24));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "x3.12.2013", istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "3.12.2013", istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "00.12.2013", istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "32.12.2013", istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "31.11.2013", istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "30.02.2013", istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "29.02.2013", istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "033.12.2013", istunto, bundle));
    }
    
    @Test
    public void tarkastaSuorituspvmPalauttaaNullJosSuoritusKuukausiAnnettuVirheellisesti() {
        kurssi.setPaattymis_pvm(new Date(111, 11, 24));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "03.13.2013", istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "03.1.2013", istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "03.x1.2013", istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "03.00.2013", istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "03.111.2013", istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "03.13.2013", istunto, bundle));
    }
    
    @Test 
    public void tarkastaSuorituspvmPalauttaaNullJosSuoritusVuosiAnnettuVirheellisesti() {
        kurssi.setPaattymis_pvm(new Date(111, 11, 24));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, "03.13.-111", istunto, bundle));
    }
    
    @Test
    public void tarkastaSuorituspvmPalauttaaNullJosKurssilleEiOleAsetettuPaattymisPvmKantaan() {
        Calendar suoritusPvm = Calendar.getInstance();
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, muotoilu.format(suoritusPvm.getTime()), istunto, bundle));
    }
    
    @Test
    public void tarkastaSuorituspvmPalauttaaNullJosSuoritusPvmEiTaytaRajaehtoja() {
        kurssi.setPaattymis_pvm(new Date(111, 11, 24));
        Calendar alaraja = Calendar.getInstance();
        alaraja.add(Calendar.MONTH, -6);
        alaraja.add(Calendar.DATE, -1);
        Calendar ylaraja = Suorituspaivamaara.asetaSuorituspvmlleYlaraja(kurssi);
        ylaraja.add(Calendar.DATE, 1);
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, muotoilu.format(alaraja.getTime()), istunto, bundle));
        assertNull(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, muotoilu.format(ylaraja.getTime()), istunto, bundle));
    }
    
    @Test
    public void tarkastaSuorituspvmPalauttaaSuoritusPvmnAsianmukaisestiKunSuoritusPvmOnKelvollinen() {
        kurssi.setPaattymis_pvm(new Date(111, 11, 24));
        Calendar suoritusPvm = Calendar.getInstance();
        assertEquals(Suorituspaivamaara.tarkastaSuorituspvm(kurssi, muotoilu.format(suoritusPvm.getTime()), istunto, bundle), Suorituspaivamaara.vaihdaPvmnMuotoa(muotoilu.format(suoritusPvm.getTime())));
    }
}