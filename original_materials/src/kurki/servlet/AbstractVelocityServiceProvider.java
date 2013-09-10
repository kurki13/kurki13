package kurki.servlet;

import kurki.*;
import service.*;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.URLEncoder;

import org.apache.velocity.*;
import org.apache.velocity.context.*;
import org.apache.velocity.app.*;
import org.apache.velocity.exception.*;
import org.apache.velocity.servlet.*;

public abstract class AbstractVelocityServiceProvider
    extends ServiceAdapter {

    public AbstractVelocityServiceProvider( Service service ) { 
	super( service );
    }


    /**
       Käsittelee http-pyynnön kutsumalla handleService-metodia ja
       huolehtimalla Templaten käsittelyyn liittyvistä 
       virhetilanteista.
    */
    public abstract String handleRequest( Session session, 
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
