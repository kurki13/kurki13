import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.velocity.Template;
import org.apache.velocity.servlet.VelocityServlet;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import javax.servlet.http.*;
import kurki.Session;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

public class HelloTest extends VelocityServlet {

    @Override
    public Template handleRequest( HttpServletRequest request,
                                   HttpServletResponse response,
                                   Context context ) {
        Template template = null;
        context.put("pipe", new Pipe());
        context.put("bundle", ResourceBundle.getBundle("localisationBundle", Session.locale));

        try {
            int a=0;
            context.put("name", "" + a++);
            template = Velocity.getTemplate("hello.vm");
        } catch( Exception e ) {
          System.err.println("Exception caught: " + e.getMessage());
        }

        return template;
    }
}