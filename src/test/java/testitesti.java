
import net.sourceforge.jwebunit.junit.*;
import net.sourceforge.jwebunit.util.*;
import org.junit.Before;
import org.junit.Test;

public class testitesti {

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
        tester.selectOption("courseId", "Data Mining [2010-03-15] K");        
        tester.assertTextPresent("kurssi vaihdettu");
    }
}