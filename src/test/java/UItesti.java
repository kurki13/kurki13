
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
    public void nappulat() {
        tester.beginAt("kurki13/hello.vm");
        tester.assertButtonPresentWithText("kurssihaku");
        tester.assertButtonPresentWithText("laskarit");
        tester.assertButtonPresentWithText("opiskelija");
        tester.assertButtonPresentWithText("osallistujaLista");
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
        tester.assertFormPresent("kurssinPisteet");
        tester.assertSubmitButtonPresent();
    }
}