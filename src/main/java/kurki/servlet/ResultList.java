package kurki.servlet;

import kurki.*;
import service.*;

import javax.servlet.http.*;
import org.apache.velocity.context.*;

public class ResultList extends AbstractVelocityServiceProvider {

//     public static final String RANGE_OP = "..";

    public ResultList( Service service ) {
 	super( service );
    }

    public String handleRequest( Session session, 
				 HttpServletRequest req, 
				 HttpServletResponse res, 
				 Context ctx ) throws Exception {

	Log log         = (Log)Configuration.getProperty( "log" );
	String template = "resultlist.vm";

        return template;
    }
}

