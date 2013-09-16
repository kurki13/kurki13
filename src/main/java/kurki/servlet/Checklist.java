package kurki.servlet;

import kurki.*;
import service.*;

import javax.servlet.http.*;
import org.apache.velocity.context.*;

public class Checklist extends AbstractVelocityServiceProvider {

//     public static final String RANGE_OP = "..";

    public Checklist( Service service ) {
 	super( service );
    }

    @Override
    public String handleRequest( Session session, 
				 HttpServletRequest req, 
				 HttpServletResponse res, 
				 Context ctx ) throws Exception {
	String template = "checklist.vm";
	return template;
    }
}

