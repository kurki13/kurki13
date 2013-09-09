import org.apache.velocity.Template;
import org.apache.velocity.servlet.VelocityServlet;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import javax.servlet.http.*;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

public class HelloTest extends VelocityServlet {

    @Override
    public Template handleRequest( HttpServletRequest request,
                                   HttpServletResponse response,
                                   Context context ) {
        Template template = null;

        try {
            context.put("name", "Velocity Test");
            template = Velocity.getTemplate("hello.vm");
        } catch( Exception e ) {
          System.err.println("Exception caught: " + e.getMessage());
        }

        return template;
    }
}