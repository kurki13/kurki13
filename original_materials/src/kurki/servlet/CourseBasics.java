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

public class CourseBasics extends AbstractVelocityServiceProvider 
    implements Serializable {

    private String SEPARATOR = "_";

    public CourseBasics( Service service ) {
 	super( service );
    }

    public String handleRequest( Session session, 
				 HttpServletRequest req, 
				 HttpServletResponse res, 
				 Context ctx ) throws Exception {

	Log log         = (Log)Configuration.getProperty( "log" );
	String template = "coursebasics.vm";
	Course course   = session.getSelectedCourse();
	String error = "";
	String result = "";

	String doBasic = req.getParameter("doBasic");
	String doScoreDef = req.getParameter("doScoreDef");
	// 	String  = req.getParameter("");

	if ( doBasic != null ) {
	    Part part = null;

	    for ( int p=0; p < Part.ARVOSANA; p++ ) {
		int nbr = 0;
		int required = 0;
		int xtr = 0;

		String par = null;		
		try {
		    par = nullIfEmpty( req.getParameter( "no"+p + SEPARATOR+"nbr") );
		    if ( par != null ) {
			nbr = Integer.parseInt( par );
			part = course.getPart( p, true );
			if (!part.setNbrOfOfferings( nbr )) {
			    part = null;
			    throw new NumberFormatException();
			}
		    }
		} catch ( NumberFormatException nfe ) {
		    String pstring = null;
		    switch (p) {
		    case 0: pstring = "Laskuharjoitusten "; break;
		    case 1: pstring = "Harjoitust�iden "; break;
		    case 2: pstring = "Kokeiden "; break;
		    }
		    error += "<li>"+pstring+" lkm annettu virheellisesti: \""+par+"\". (0-18)</li>\n";
		}
		if ( part != null ) {
		    try {
			par = nullIfEmpty( req.getParameter( "no"+p + SEPARATOR+"req") );
			if ( par != null ) {
			    required = Integer.parseInt( par );
			    if (!part.setRequiredOfferings( required )) 
				throw new NumberFormatException();
			}
		    } catch ( NumberFormatException nfe ) {
			String pstring = null;
			switch (p) {
			case 0: pstring = "Laskuharjoitusten "; break;
			case 1: pstring = "Harjoitust�iden "; break;
			case 2: pstring = "Kokeiden "; break;
			}
			error += "<li>"+pstring+" pakollisten kertojen lkm annettu virheellisesti: \""+par+"\". (0 - lkm)</li>\n";
		    }

		    try {
			par = nullIfEmpty( req.getParameter( "no"+p + SEPARATOR+"xtr") );
			if ( par != null ) {
			    xtr = Integer.parseInt( par );
			    if ( !part.setXtrScore( xtr ) )
				throw new NumberFormatException();
			}
		    } catch ( NumberFormatException nfe ) {
			String pstring = null;
			switch (p) {
			case 0: pstring = "Laskuharjoitusten "; break;
			case 1: pstring = "Harjoitust�iden "; break;
			}
			error += "<li>"+pstring+" lis�pisteet annettu virheellisesti: \""+par+"\". (0 - 60)</li>\n";
		    }
		}
	    }
	    if ( error.length() > 0 ) error = "<ul>\n"+error+"</ul>\n";

	    course.commitCourseInfo();
	}
	else if ( doScoreDef != null ) {
	    Vector parts = course.getParts();
	    for ( int p=0; p < parts.size(); p++ ) {
		Part part = (Part)parts.get( p );
		int pid = part.getType();
		Offering[] offerings = part.getOfferings();

		for ( int o=0; o < offerings.length; o++ ) {
		    int oid = offerings[o].getId();
		    int max = 0;
		    int min = 0;

		    String par = null;
		    String errcause = null;
		    try {
			par = nullIfEmpty( req.getParameter( "no"+pid+SEPARATOR+oid+SEPARATOR+"max") );
			if ( par != null ) {
			    if (!offerings[o].setMaxScore( Integer.parseInt( par ) ))
				throw new NumberFormatException();
			}
		    } catch ( NumberFormatException nfe ) {
			String pstring = null;
			switch (pid) {
			case 0: pstring = "laskuharjoitukset"; break;
			case 1: pstring = "harjoitusty�"; break;
			case 2: pstring = "koe"; break;
			}
			error += "<li>"+(oid+1)+". "+pstring
			    +": maksimipisteet annettu virheellisesti: \""+par+"\". (0-99)</li>\n";
		    }

		    try { 		
			par = nullIfEmpty( req.getParameter( "no"+pid+SEPARATOR+oid+SEPARATOR+"min") );
			if ( par != null ) {
			    if (!offerings[o].setMinScore( min = Integer.parseInt( par ) ))
				throw new NumberFormatException();
			}
		    } catch ( NumberFormatException nfe ) {
			String pstring = null;
			switch (pid) {
			case 0: pstring = "laskuharjoitukset"; break;
			case 1: pstring = "harjoitusty�"; break;
			case 2: pstring = "koe"; break;
			}
			error += "<li>"+(oid+1)+". "+pstring
			    +": hyv�ksymisraja annettu virheellisesti: \""+par+"\". (0-maximi)</li>\n";
		    }
		}
	    }
	    if ( error.length() > 0 ) error = "<ul>\n"+error+"</ul>\n";
	    course.commitCourseInfo();
	}

	if ( nullIfEmpty(error) != null )
	    ctx.put( Index.ERROR, error );
	// 	if ( nullIfEmpty(result) != null ) 
	// 	    ctx.put( Index.RESULT, result );


// 	ctx.put( "course", course );
	return template;
    }
}
