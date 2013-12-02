/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.osasuoritukset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import model.Kurssi;
import model.Osallistuminen;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author esaaksvu
 */
public class OsasuoritusIteratorTest {

    int max_lkm = 10;
    String max = "10,??,??,??,??-??,??,??,??,??-??,??,??,??,??-??,??,??";
    String done = "5,??,??,??,??-??,??,??,??,??-??,??,??,??,??-??,??,??";
    Kurssi k;
    Osallistuminen os;

    public OsasuoritusIteratorTest() {
    }

    @Before
    public void setUp() {
        k = new Kurssi();
        k.setValue(Kurssi.laskarikerta_lkm, max_lkm);
        k.setValue(Kurssi.laskaritehtava_lkm, max);
        os = new Osallistuminen();
        os.setKurssi(k);
        os.setValue(Osallistuminen.laskarisuoritukset, done);
    }

    @Test
    public void testHasNext() {
        List<Osasuoritus> l = new ArrayList();
        l.add(new Osasuoritus(1, 2));
        l.add(new Osasuoritus(2, 2));
        l.add(new Osasuoritus(2, 2));
        OsasuoritusIterator osi = new OsasuoritusIterator(l, 1);
        int i = 0;
        for (Iterator<Osasuoritus> it = osi; it.hasNext(); osi.next()) {
            i++;
        }
        assertEquals(1, i);
    }
}