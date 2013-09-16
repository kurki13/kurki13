package kurki.servlet;

import java.util.ResourceBundle;
import kurki.*;
import service.*;

import javax.servlet.http.*;
import org.apache.velocity.context.*;

public class ResultList extends AbstractVelocityServiceProvider {

//     public static final String RANGE_OP = "..";

    public ResultList( Service service ) {
 	super( service );
    }

    @Override
    public String handleRequest( Session session, 
				 HttpServletRequest req, 
				 HttpServletResponse res, 
				 Context context ) throws Exception {

	Log log         = (Log)Configuration.getProperty( "log" );
	String template = "resultlist.vm";
        
        //lokalisaatiobundlen lisääminen kontekstiin
        context.put("bundle", ResourceBundle.getBundle("localisationBundle", Session.locale));

        return template;
    }
}

