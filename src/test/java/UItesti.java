
import net.sourceforge.jwebunit.junit.*;
import net.sourceforge.jwebunit.util.*;
import org.junit.Before;
import org.junit.Test;

public class UItesti {

    private WebTester tester;
    private TestContext context;

    @Before
    public void prepare() {
        tester = new WebTester();
        tester.setBaseUrl("http://localhost:8080/kurki13");
        context = new TestContext();
        context.setAuthorization("admin", "admin");
        tester.setTestContext(context);
    }

    @Test
    public void title() {
        tester.beginAt("kurki13");
        tester.assertTitleEquals("KURKI");
    }

    @Test
    public void kurssivalikkoAluksi() {
        tester.beginAt("kurki13");
        tester.assertFormElementPresent("courseId");
        tester.assertSelectOptionPresent("courseId", "-- valitse kurssi --");
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertSelectOptionNotPresent("courseId", "-- valitse kurssi --");
    }

    @Test
    public void toimintovalikkoAluksi() {
        tester.beginAt("kurki13");
        tester.assertFormElementPresent("courseId");
        tester.assertFormElementNotPresent("serviceId");        
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertFormElementPresent("serviceId");   
    }

    @Test
    public void logout() {
        tester.beginAt("kurki13");
        tester.assertLinkPresentWithText("Logout");
        tester.clickLinkWithText("Logout");
        tester.assertTextPresent("Session has been reset");
    }

    @Test
    public void kurssinVaihtaminen() {
        tester.beginAt("kurki13");
        tester.assertFormElementPresent("courseId");
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertTextPresent("Data Mining [2010-03-15] K");
        tester.assertTextNotPresent("AI for Games II [2010-05-10] K");
    }

    @Test
    public void suoritteidenKirjaus() {
        tester.beginAt("kurki13");
        tester.assertFormElementPresent("courseId");
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertFormElementPresent("serviceId");
        tester.selectOption("serviceId", "1. Suoritteiden kirjaus");
        tester.assertFormPresent("suoritteidenKirjaus");
        tester.assertFormElementPresent("type");
        tester.assertFormElementPresent("kerta");
        tester.assertFormElementPresent("ryhma");
        tester.assertSubmitButtonPresent();
    }

    @Test
    public void osallistujatietojenMuutokset() {
        tester.beginAt("kurki13");
        tester.assertFormElementPresent("courseId");
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertFormElementPresent("serviceId");
        tester.selectOption("serviceId", "2. Osallistujatietojen muutokset");
        tester.assertFormPresent("osallistujatiedot");
        tester.assertFormElementPresent("filterRyhma");
        tester.assertFormElementPresent("toiminto");
        tester.assertSubmitButtonPresent();
    }

    @Test
    public void perustietojenMuutokset() {
        tester.beginAt("kurki13");
        tester.assertFormElementPresent("courseId");
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertFormElementPresent("serviceId");
        tester.selectOption("serviceId", "3. Kurssin perustietojen muutokset");
        tester.assertFormPresent("kurssinPerustiedot");
        tester.assertSubmitButtonPresent();
    }
    
    @Test
    public void listat() {
        tester.beginAt("kurki13");
        tester.assertFormElementPresent("courseId");
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertFormElementPresent("serviceId");
        tester.selectOption("serviceId", "4. Listat");
        tester.assertFormPresent("suoritevalinta");
        tester.assertFormElementPresent("listtype");
        tester.assertFormElementPresent("kommentti");
        tester.assertFormElementPresent("ryhma");
        tester.assertFormElementPresent("filter");
        tester.assertSubmitButtonPresent();
    }
    
    @Test
    public void arvostelu() {
        tester.beginAt("kurki13");
        tester.assertFormElementPresent("courseId");
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertFormElementPresent("serviceId");
        tester.selectOption("serviceId", "5. Arvostelu");
        tester.assertFormPresent("arvostelu");
        tester.assertFormElementPresent("arvostelutapa");
        tester.assertSubmitButtonPresent();
    }
    
    @Test
    public void tuloslistat() {
        tester.beginAt("kurki13");
        tester.assertFormElementPresent("courseId");
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertFormElementPresent("serviceId");
        tester.selectOption("serviceId", "6. Tuloslistat");
        tester.assertFormPresent("suoritevalinta");
        tester.assertFormElementPresent("inc_accepted");
        tester.assertFormElementPresent("inc_failed");
        tester.assertSubmitButtonPresent();
    }
    
     @Test
    public void jaadytys() {
        tester.beginAt("kurki13");
        tester.assertFormElementPresent("courseId");
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertFormElementPresent("serviceId");
        tester.selectOption("serviceId", "7. Kurssin jäädytys");
        tester.assertFormElementPresent("jaadyta");
        tester.assertSubmitButtonPresent();
    }
}