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

public class ListMaker extends VelocityServlet implements Log, Serializable {

    public static final String RANGE_OP = "..";
    
    protected void doRequest( HttpServletRequest req, 
			      HttpServletResponse res )
	throws ServletException, IOException {
	

	String ts = null;
	Session session = null;
	Context ctx = null;
	AbstractVelocityServiceProvider serviceProvider = null;
	Template template = null;

	// Opiskelijasuodattimen tulkinta
	String studentFilterDesc = null;
	HttpSession s = null;
        try {
	    
	    /*
	     *  Http-parametrit 
	     */
	    String ltype   = req.getParameter( "ltype" );
	    String ctype   = nullIfEmpty( req.getParameter( "ctype" ) );
	    String comment = nullIfEmpty( req.getParameter( "comment" ) );
	    String useinc  = nullIfEmpty( req.getParameter( "useinc"  ) );

	    // annettu opiskelijasuodatin
	    String filter   = req.getParameter( "filter" );
	    
	    // Luodaan template-konteksti 
            ctx = createContext( req, res );

	    int mime = ltype.indexOf("_");
	    if (mime > 0) {
		ctype = "text/"+ltype.substring(mime+1, ltype.length());
		ltype = ltype.substring(0, mime);
	    }

	    if ( ctype == null ) {
		setContentType( req, res );
	    }
	    else {
		res.setContentType( ctype );
	    }

	    /*
	     *  Onko kyseessä jo joskus aloitettu, kenties vanhentunut sessio.
	     */ 
	    boolean oldSession = req.getRequestedSessionId() != null;

	    /*
	     *  Hae voimassaoleva sessio
	     */
	    s = req.getSession();
	    Object tmpSession = s.getAttribute( Index.KURKI_SESSION );

	    // Ei käsitellä kahta saman session http-pyyntöä yhtä aikaa
	    if ( !ServletMonitor.getMonitor().lock( s ) ) {
		s = req.getSession( true );
		ServletMonitor.getMonitor().lock( s );
	    }

	    if ( oldSession && !s.isNew() && tmpSession != null && ltype != null ) {
		session = (Session)tmpSession;

		Course course = session.getSelectedCourse();
		
		if ( course == null ) {
		    template = getTemplate( "checklist_error.vm" );
		} else {
		    template = getTemplate( "checklist"+ltype+".vm" );
		    /*
		     *  Opiskelijat: ryhmän tai sukunimem mukaan.
		     */
		    course.newSearch();
               //   listausjärjestys      08/8/1  HL / 
                    if (ltype.equals("99")) {
                       String inc_ssn= req.getParameter("inc_ssn");
                       String inc_name=req.getParameter("inc_name");
                       if (inc_ssn!=null && inc_name==null) course.orderBy(Course.ORDER_BY_STNUMBER);
                    }
                    if (ltype.equals("8")) course.orderBy(Course.ORDER_BY_STNUMBER);  
		    if ( ltype.equals("6") ) course.orderBy( Course.ORDER_BY_ENROLMENT_TIME );
                    
		    // Opiskelijasuotimen tulkinta
		    StringTokenizer st = new StringTokenizer( filter, ",\t\n\r\f" );
		    while ( st.hasMoreTokens() ) {
			String term = st.nextToken().trim();
			
			if ( term.length() == 0 ) continue;
			
			// Haku ryhmän mukaan, jos annettu kokonaisluku...
			try {
			    int g = Integer.parseInt( term );
			    course.findByGroup( g );
			}
			
			// ...muuten haetaan sukunimen tai henkilötunnuksen mukaan
			catch ( NumberFormatException nfe ) {
			    // Haetaan sukunimiväliä
			    int r = term.indexOf( RANGE_OP );
			    if ( r >= 0 ) {
				String from = term.substring(0, r).trim();
				String to = term.substring(r + RANGE_OP.length(),
							   term.length()).trim();
				
				course.findByLastNameRange( nullIfEmpty(from),
							    nullIfEmpty(to) );
				
			    } 
			    // Haetaan sukunimeä tai henkilötunnusta
			    else {
                                if (term.charAt(0) == '$') {
                                   course.findByStudentID(term.substring(1,term.length()));
                                }
                                else {
				   term = term.replace('*', '%')+"%";
				   if (term.charAt(0) == '#') {
				       course.findBySSN(term.substring(1, term.length()));
				   }
				   else {
				       course.findByLastName( term );
				   }
                                }
			    }
			}
		    }

		    if ( useinc != null ) {		    
			for (Enumeration e = req.getParameterNames(); e.hasMoreElements() ; ) {
			    String par = (String)e.nextElement();
			    if ( par.indexOf("inc_") == 0 ) {
				ctx.put( par, req.getParameter( par ) );
			    }
			}
		    }

		    ctx.put( "selectedCourse", session.getSelectedCourse() );
		    ctx.put( "students", course.getStudents() );
		    ctx.put( "comment", comment );

		    Calendar calendar = Calendar.getInstance();
		    ctx.put( "sysdate", 
			     +calendar.get(Calendar.DAY_OF_MONTH)+"."
			     +(calendar.get(Calendar.MONTH)+1)+"."
			     +calendar.get(Calendar.YEAR) );

		    String minutes = "00";
		    int m = calendar.get(Calendar.MINUTE);
		    if ( m < 10 ) minutes = "0"+m;
		    else minutes = ""+m;
		 
		    ctx.put( "time", 
			     +calendar.get(Calendar.HOUR_OF_DAY)+"."
			     +minutes );

		    studentFilterDesc = course.getSelectDescription();
		    ctx.put( "studentFilterDesc",
			     ( nullIfEmpty( studentFilterDesc ) == null
			       ? "kaikki kurssin opiskelijat"
			       : studentFilterDesc ) );
		}
	    } // if ( oldSession && !s.isNew() && tmpSession != null && ltype != null )
	    else {
		template = getTemplate( "checklist_error.vm" );
	    }
		
	    /*
	     *  Uusi aikaleima.
	     */
	    ts = ""+System.currentTimeMillis();
	    s.setAttribute( Index.TIMESTAMP, ts );
	    
	    /*
	     *  bail if we can't find the template
	     */
	    if ( template == null ) {
		return;
	    }
		
	    /*
	     *  now merge it
	     */
	    
	    mergeTemplate( template, ctx, res );
	    
	    /*
	     *  call cleanup routine to let a derived class do some cleanup
	     */
	    
	    requestCleanup( req, res, ctx );
	}
	catch (Exception e) {
	    /*
	     *  call the error handler to let the derived class
	     *  do something useful with this failure.
	     */
	    ServletOutputStream out;
	    res.setContentType("text/html");
	    out= res.getOutputStream();

	    out.println( "<html><head>\n<title>Kurki: virheilmoitus</title>\n"
			 +"<link rel='stylesheet' href='../kurki.css' title='kurki'>\n</head><body>\n"
			 +"<div class='error' style='text-align:center;width=500px'>\n"
			 +"<h2>Virheilmoitus</h2>\n<hr>\n<pre align='left'>\n" );
            e.printStackTrace( new PrintStream( out ) );
	    out.println( "\n</pre>\n</div></body></html>" ); 
	    
	    e.printStackTrace();
// 	    error( req, res, e);
	} finally {
	    if ( s != null ) 
		ServletMonitor.getMonitor().unlock( s );
	    else {
		ServletMonitor.getMonitor().notifyAll();
	    }
	}
    }

    protected String nullIfEmpty(String str) {
	if ( str == null || str.equals("") ) return null;
	else return str;
    }
}



