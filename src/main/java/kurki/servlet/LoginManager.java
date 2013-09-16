package kurki.servlet;

import kurki.*;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.velocity.*;
import org.apache.velocity.context.*;
import org.apache.velocity.exception.*;
import org.apache.velocity.servlet.*;

public class LoginManager extends VelocityServlet {

    private String LOGIN_SEPARATOR = ",";
    protected String lmanagers = LOGIN_SEPARATOR;
    
    @Override
    public synchronized void init()
	throws ServletException {
	ServletContext ctx = getServletContext();
	
	/*
	 *  Konfiguraatio...
	 */
	try {
	    Properties p = new Properties();
            //MKCT: Real tal..path
	    FileInputStream fis = new FileInputStream( ctx.getRealPath(ctx.getInitParameter("configurationFile")) );
	    p.load(fis);
	    
	    Enumeration enames = p.propertyNames();
	    while ( enames.hasMoreElements() ) {
		String key   = (String)enames.nextElement();
		String value = p.getProperty( key );
		Configuration.setProperty( key, value );
	    }
	    /*
	     *  Henkilöt, joilla päivitysoikeus
	     */
	    if ( Configuration.propertySet("loginManagers") ) {
		StringTokenizer st = new StringTokenizer( (String)Configuration.getProperty("loginManagers"), "," );

		while ( st.hasMoreTokens() ) {
		    String login = st.nextToken().trim();
		    if ( login.length() > 0 )
			this.lmanagers += login+LOGIN_SEPARATOR;
		}
	    }
	} catch ( Exception e ) {
	    throw new ServletException( "Virhetilanne!<br>"+e.getMessage() );
	}
    }
	
    @Override
    public Template handleRequest( HttpServletRequest req,
				   HttpServletResponse res,
				   Context ctx )
	throws Exception {
	Template template = null;
	String error = "";

	String ruser = req.getRemoteUser();

	if ( ruser != null && this.lmanagers.indexOf( LOGIN_SEPARATOR+ruser+LOGIN_SEPARATOR ) >= 0 ) {

	    Vector teachers = new Vector();
	    Connection con = DBConnectionManager.createConnection();
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
		stmt = con.createStatement();

		if ( req.getParameter("save") != null ) {
		    Enumeration pnames = req.getParameterNames();
		    while (pnames.hasMoreElements()) {
			String p = (String)pnames.nextElement();
			StringTokenizer st = new StringTokenizer(p, ":");

			if ( st.hasMoreTokens() && st.nextToken().equals("htunnus") ) {
			    String htunnus = st.nextToken();
			    String oktunnus = (st.hasMoreTokens() ? st.nextToken() : "");
			    String ktunnus  = req.getParameter(p);
			    // 			System.out.println(p+"--> >"+htunnus+"< : >"+oktunnus+"< = >"+ktunnus+"<");
			    if ( !ktunnus.equals(oktunnus) ) {
				if ( stmt.executeUpdate("UPDATE henkilo SET ktunnus='"+ktunnus+"'\n"
							+"  WHERE htunnus='"+htunnus+"'") != 1 ) {
				    error+="Ohjaajan "+htunnus+" käyttäjätunnuksen kirjaaminen ei onnistunut.<br>";
				}
			    }
			}
		    }
		}

		rs = stmt.executeQuery( "SELECT htunnus, etunimet, sukunimi, hetu, ktunnus\n"
					+"FROM henkilo\n"
					+"WHERE aktiivisuus='K'\n"
					+"ORDER BY sukunimi" );

		while (rs.next()) {
		    teachers.add( new Teacher( rs.getString("htunnus"),
					       rs.getString("etunimet"),
					       rs.getString("sukunimi"),
					       rs.getString("hetu"),
					       rs.getString("ktunnus") ) );
		}
	    } finally {
		try {
		    if ( rs != null ) rs.close();
		    if ( stmt != null ) stmt.close();
		} catch ( Exception e ) {}
		DBConnectionManager.closeConnection( con );
	    }

	    ctx.put( "teachers", teachers );
	    ctx.put( "error", error );
	    
	    try {
		template = getTemplate("loginmanager.vm");
	    } catch ( ResourceNotFoundException rnfe ) {
		
	    } catch ( Exception e ) {
		
	    }
	}

	return template;
    }
}
