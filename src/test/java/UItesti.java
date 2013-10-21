
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
        tester.beginAt("kurki13/hello.vm");
        tester.assertTitleEquals("KURKI");
    }

    @Test
    public void kurssivalikkoAluksi() {
        tester.beginAt("kurki13/hello.vm");
        tester.assertFormElementPresent("courseId");
        tester.assertSelectOptionPresent("courseId", "-- valitse kurssi --");
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertSelectOptionNotPresent("courseId", "-- valitse kurssi --");
    }

    @Test
    public void toimintovalikkoAluksi() {
        tester.beginAt("kurki13/hello.vm");
        tester.assertFormElementPresent("courseId");
        tester.assertFormElementNotPresent("serviceId");        
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertFormElementPresent("serviceId");   
    }

    @Test
    public void logout() {
        tester.beginAt("kurki13/hello.vm");
        tester.assertLinkPresentWithText("Logout");
        tester.clickLinkWithText("Logout");
        tester.assertTextPresent("Session has been reset");
    }

    @Test
    public void kurssinVaihtaminen() {
        tester.beginAt("kurki13/hello.vm");
        tester.assertFormElementPresent("courseId");
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertTextPresent("Data Mining [2010-03-15] K");
        tester.assertTextNotPresent("AI for Games II [2010-05-10] K");
    }

    @Test
    public void suoritteidenKirjaus() {
        tester.beginAt("kurki13/hello.vm");
        tester.assertFormElementPresent("courseId");
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertFormElementPresent("serviceId");
        tester.selectOption("serviceId", "Suoritteiden kirjaus");
        tester.assertFormPresent("suoritteidenKirjaus");
        tester.assertFormElementPresent("type");
        tester.assertFormElementPresent("kerta");
        tester.assertFormElementPresent("ryhma");
        tester.assertSubmitButtonPresent();
    }

    @Test
    public void osallistujatietojenMuutokset() {
        tester.beginAt("kurki13/hello.vm");
        tester.assertFormElementPresent("courseId");
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertFormElementPresent("serviceId");
        tester.selectOption("serviceId", "Osallistujatietojen muutokset");
        tester.assertFormPresent("osallistujatiedot");
        tester.assertFormElementPresent("ryhma");
        tester.assertFormElementPresent("toiminto");
        tester.assertSubmitButtonPresent();
    }

    @Test
    public void perustietojenMuutokset() {
        tester.beginAt("kurki13/hello.vm");
        tester.assertFormElementPresent("courseId");
        tester.assertSelectOptionPresent("courseId", "Data Mining [2010-03-15] K");
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");
        tester.assertFormElementPresent("serviceId");
        tester.selectOption("serviceId", "Kurssin perustietojen muutokset");
        tester.assertFormPresent("kurssinPerustiedot");
        tester.assertSubmitButtonPresent();
    }
}