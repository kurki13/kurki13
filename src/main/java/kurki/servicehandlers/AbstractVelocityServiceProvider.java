package kurki.servicehandlers;

import kurki.*;

import javax.servlet.http.*;
import org.apache.velocity.context.*;

public abstract class AbstractVelocityServiceProvider{
    
    /**
       Käsittelee http-pyynnön kutsumalla handleService-metodia ja
       huolehtimalla Templaten käsittelyyn liittyvistä 
       virhetilanteista.
    */
    public abstract String handleRequest( Session_ session, 
				   HttpServletRequest req, 
				   HttpServletResponse res, 
				   Context ctx ) throws Exception;

    protected String nullIfEmpty(String str) {
	if ( str == null ) return null;
	else {
	    str = str.trim();
	    if ( str.equals("") ) return null;
	    else return str;
	}
    }
}
