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

public class Index extends VelocityServlet implements Log, Serializable {

    public static final String RESULT = "result";
    public static final String ERROR = "error";
    public static final String INDEX = "index";

    public static final String SERVICE = "service";
    public static final String COURSE = "course";
    
    private boolean initialized = false;
    
    private final String BASIC_TEMPLATE = "index.vm";
    private final String SYNCHRONIZATION_ERROR = "syncerror.vm";

    public static final String TIMESTAMP = "TS";

    private int sessionLen = 3600;

    public static final String KURKI_SESSION = "session";

    public static final Hashtable handlers = new Hashtable();

    static {
	Session.initialize();
    }
    
    public synchronized void init() {
	if ( !initialized ) {
	    try {
		ServletContext ctx = getServletContext();

		/*
		 *  Konfiguraatio...
		 */
                
                //MKCT: Tähän piti lisätä getRealPath jotta web.xml:ssä voidaan käyttää relatiivista polkua!
		Configuration.setPropertiesFile( new File( ctx.getRealPath(ctx.getInitParameter("configurationFile") ) ));

		/*
		 *  Lisätään tieto lokista konfiguraatioon.
		 */
		Configuration.setProperty( "log", this );

		/*
		 *  "SUPER USERS"
		 */ 
		if ( Configuration.propertySet("superUsers") )
		    Session.setSuperUsers( (String)Configuration.getProperty("superUsers") );

		/*
		 *  Palveluiden käsittelijöiden määritys
		 */
		ServiceManager serviceManager = Session.getServiceManager();
		
		handlers.put(Session.ENTRY, 
			     new Entry( serviceManager.getService( Session.ENTRY ) ) );
		handlers.put(Session.PARTICIPANTS, 
			     new Participants( serviceManager.getService( Session.PARTICIPANTS ) ) );
		handlers.put(Session.COURSE_BASICS, 
			     new CourseBasics( serviceManager.getService( Session.COURSE_BASICS ) ) );
 		handlers.put(Session.CHECKLIST, 
 			     new Checklist( serviceManager.getService( Session.CHECKLIST ) ) );
 		handlers.put(Session.GRADES, 
 			     new Grades( serviceManager.getService( Session.GRADES ) ) );
 		handlers.put(Session.RESULT_LIST, 
 			     new ResultList( serviceManager.getService( Session.RESULT_LIST ) ) );
                //MKCT: tää yks piti kommentoida pois
//                handlers.put(Session.FREEZE, 
//  			     new Freeze( serviceManager.getService( Session.FREEZE ) ) );

//  		handlers.put(Session.LOGOUT, 
//  			     new Logout( serviceManager.getService( Session.LOGOUT ) ) );
		initialized = true;

	    } catch ( Exception e ) {
		e.printStackTrace();
		System.exit(0);
	    }
	}
    }

    protected void doRequest( HttpServletRequest req, 
			      HttpServletResponse res )
	throws ServletException, IOException {

	Vector vec = new Vector();
	String ts = null;
	String error = "";
	String result = "";
	CourseInfo course = null;
	Session session = null;
	Context ctx = null;
	AbstractVelocityServiceProvider serviceProvider = null;
	Template template = null;
	HttpSession s = null;

// 	Calendar cal = Calendar.getInstance();

// 	res.setHeader( "Last-Modified",
// 		       cal.get(Calendar.DAY_OF_WEEK)+", "
// 		       +cal.get(Calendar.DAY_OF_MONTH)+"."
// 		       +(cal.get(Calendar.MONTH)+1);

// 	res.setDateHeader( "Last-Modified", System.currentTimeMillis() );
// 	res.setDateHeader( "Expires", System.currentTimeMillis()-6000000 );
// 	res.setHeader( "Cache-Control", "no-cache, must-revalidate" );
// 	res.setHeader( "Pragma", "no-cache" );

        try {
	    // Luodaan template-konteksti 
            ctx = createContext( req, res );

            setContentType( req, res );

	    /*
	     *  Onko kyseessä jo joskus aloitettu, kenties vanhentunut sessio.
	     */ 
	    boolean oldSession = req.getRequestedSessionId() != null;

	    /*
	     *  Hae voimassaoleva sessio tai aloita uusi
	     */
	    s = req.getSession();
	    s.setMaxInactiveInterval( sessionLen );

	    // Ei käsitellä kahta saman session http-pyyntöä yhtä aikaa
	    if ( !ServletMonitor.getMonitor().lock( s ) ) {
		s = req.getSession( true );
		ServletMonitor.getMonitor().lock( s );
	    }

	    if ( oldSession && s.isNew() ) {
		result += "Istuntosi on vanhentunut.\n"
		    +"Uusi istunto aloitettiin automaattisesti.\n"
		    +"Jos sait tämän ilmoituksen painettuasi \"tallenna\"-nappia,\n"
		    +"ei tekemiäsi muutoksi ole voitu tallentaa tietokantaan.";
	    }

	    Object tmpSession = s.getAttribute( KURKI_SESSION );

	    if ( tmpSession != null ) {
		session = (Session)tmpSession;
	    }
	    else {
		session = Session.getInstance( req.getRemoteUser() );
		s.setAttribute( KURKI_SESSION, session );
	    }

	    /*
	     *  Millä kursseilla käyttäjä on ohjaajana tai luennoitsijana
	     *  --> courses
	     */
	    CourseInfo[] cinfos = session.getCourseInfos();
	    ctx.put( "courseInfos", (cinfos.length > 0 ? cinfos : null) );


	    /*
	     *  Selvitä valittu kurssi
	     */
	    try {
		// Onko kurssivalinta tullut http-parametrina?
		course = session.getCourseInfo( nullIfEmpty( req.getParameter( COURSE ) ) );
	
    } catch ( Exception e ) {
		course = null;
	    }

	    // kurssi http-parametrina
	    if ( course != null ) {
		session.setSelectedCourse( course );
	    }

	    // kurssi valittu aikaisemmin
	    else if ( session.courseSelected() ) {
		course = session.getSelectedCourseInfo();
	    }

// 	    // kurssia ei jostain syystä ole valittu (sessio päättynyt tms.)
// 	    else if ( !this.function.getId().equals( INDEX ) ) {
// 		res.sendRedirect( "index" );
// 		return;
// 	    }

	    ctx.put( "selectedCourse", session.getSelectedCourse() );

	    /*
	     *  Selvitä mihin toimintoihin käyttäjä on oikeutettu 
	     *  suhteessa valittuun kurssiin
	     */
	    if ( session.courseSelected() ) {
 		ctx.put( "services",
			 session.getValidServices() );
	    }


	    /*
	     *  Pyytääkö käyttäjä uutta palvelua?
	     */
	    String service = nullIfEmpty( req.getParameter( SERVICE ) );
	
	    if ( service != null &&
		 session.courseSelected() ) {
		
		// palvelua ei ole määritelty tai käyttäjän oikeudet eivät riitä
		if ( !session.setService( service ) ) {
		}
	    }

	    /*
	     *  Aikaleiman tarkistus
	     */
	    String reqts = req.getParameter( TIMESTAMP );
	    Object tmp = s.getAttribute( TIMESTAMP );
	    if ( tmp != null )
		ts  = (String)tmp;
	    else ts = null;

	    /*
	     *  Palvelin ja käyttöliittymä eivät ole synkronissa - kriittinen toiminto
	     */
	    if ( reqts != null && ts != null && !reqts.equals( ts ) ) {
		template = getTemplate( SYNCHRONIZATION_ERROR );
	    }

	    /*
	     *  Onko palvelu valittu jo, jos niin nakita pyytö palvelun
	     *  toteuttavalle käsittelijälle
	     */
	    else if ( session.courseSelected() &&
		 session.serviceSelected() ) {

		// NAKITUS
		serviceProvider =
		    (AbstractVelocityServiceProvider)getHandlerFor( session.getSelectedService() );

		ctx.put( "selectedService", serviceProvider );

		String tmpl = serviceProvider.handleRequest( session, req, res, ctx );

		if ( tmpl != null )
		    template = getTemplate( tmpl );
	    }

	    // muuten pyydä valitsemaan palvelu
	    else {
		template = getTemplate( BASIC_TEMPLATE );
	    }

	    try {
		/*
		 *  Uusi aikaleima.
		 */
		ts = ""+System.currentTimeMillis();
		s.setAttribute( TIMESTAMP, ts );
		ctx.put( TIMESTAMP, ts );
		
		/*
		 *  Automaattinen välitallennus - päällä vai poissa?
		 */
		tmp = nullIfEmpty( req.getParameter("asToggle") );
		if ( tmp != null ) {
		    ctx.put( "asToggle", tmp);
		    s.setAttribute( "asToggle", tmp );

		}
		else if ( (tmp = s.getAttribute("asToggle")) != null ) {
		    ctx.put( "asToggle", tmp);
		}

		ctx.put( "sessionLen", ""+sessionLen );
	    } catch ( IllegalStateException ise ) {}


	    ctx.put( "webmaster", (String)Configuration.getProperty("webmaster") );

	    if ( nullIfEmpty(error) != null )
		ctx.put( Index.ERROR, error );
	    if ( nullIfEmpty(result) != null ) 
		ctx.put( Index.RESULT, result );

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
	    out.println( "\n</pre>\n<hr>\n<a href=\"mailto:tktl-kurki@cs.Helsinki.FI\">tktl-kurki@cs.Helsinki.FI</a></div></body></html>" ); 
	    
	    e.printStackTrace();
// 	    error( req, res, e);
	}
	finally {
	    if ( s != null ) 
		ServletMonitor.getMonitor().unlock( s );
	    else {
		ServletMonitor.getMonitor().notifyAll();
	    }
	}
    }

//     private int getAction( String act ) {
// 	if ( act == null || act.equals("") ) {
// 	    return -1;
// 	} else {
// 	    try {
// 		return Integer.parseInt( act );
// 	    } catch (NumberFormatException nfe) {
// 		return -1;
// 	    }
// 	}
//     }

    protected String nullIfEmpty(String str) {
	if ( str == null || str.equals("") ) return null;
	else return str;
    }

    public AbstractVelocityServiceProvider getHandlerFor( Service service ) {
	return (AbstractVelocityServiceProvider)handlers.get( service.getId() );
    }

    public static String asNotify( String target ) {
	Calendar calendar = Calendar.getInstance();
	int minute = calendar.get(Calendar.MINUTE);

	return target+" tallennettu automaattisesti "
	    +calendar.get(Calendar.DAY_OF_MONTH)
	    +"."+(calendar.get(Calendar.MONTH) + 1)
	    +"."+calendar.get(Calendar.YEAR)+" klo " 
	    +calendar.get(Calendar.HOUR_OF_DAY)+":"
	    +(minute < 10 ? "0"+minute : ""+minute)+".";
    }
}



