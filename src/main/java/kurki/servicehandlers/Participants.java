package kurki.servicehandlers;

import kurki.*;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.http.*;
import kurki.servlet.Index;
import org.apache.velocity.context.*;

public class Participants extends AbstractVelocityServiceProvider 
    implements Serializable {

    public static final String STUDENTS = "students";
    @Override
    public String handleRequest( Session session, 
				 HttpServletRequest req, 
				 HttpServletResponse res, 
				 Context context ) throws Exception {
        
        //lokalisaatiobundlen lisääminen kontekstiin
        context.put("bundle", ResourceBundle.getBundle("localisationBundle", Session.locale));

	Log log         = (Log)Configuration.getProperty( "log" );
	String template = "participants.vm";
	Course course   = session.getSelectedCourse();
	String error = null;
	String result = null;
	String view  = null;
	HttpSession ses = req.getSession();
	Object tmpUseSNO = ses.getAttribute( "useSNO" );
	Boolean useSNO = (tmpUseSNO != null ? (Boolean)tmpUseSNO : false );

	// toimintaa ohjaavat parametrit
	String doSearch  = nullIfEmpty( req.getParameter("doSearch") );
	String doEdit    = nullIfEmpty( req.getParameter("doEdit")   );
	String doAdd     = nullIfEmpty( req.getParameter("doAdd")    );
	String doReturn  = nullIfEmpty( req.getParameter("doReturn") );
// 	String doNext    = nullIfEmpty( req.getParameter("doNext")   );
// 	String doPrev    = nullIfEmpty( req.getParameter("doPrev")   );
	String doList    = nullIfEmpty( req.getParameter("doList")   );
	String doSave    = nullIfEmpty( req.getParameter("doSave")   );
	String defreeze  = nullIfEmpty( req.getParameter( "defreeze" ) );

	String doSaveAll = nullIfEmpty( req.getParameter("doSaveAll"));
// 	String     = nullIfEmpty( req.getParameter("") );
// 	String  = req.getParameter("");
	
	if ( doSearch != null ) {
	    String lname   = nullIfEmpty( req.getParameter("lname") );
	    String fname   = nullIfEmpty( req.getParameter("fname") );
	    String sgroup  = nullIfEmpty( req.getParameter("group") );
	    String idvalue = nullIfEmpty( req.getParameter("idvalue") );
	    int idtype     = Integer.parseInt( req.getParameter("idtype") );

	    boolean ok = true;
	    int sstate = 0;
	    int group  = -1;
	    int resultLimit = -1;
	    try {
		if ( doSearch.equals("1") ) {
		    resultLimit = Session.DEFAULT_RS_MAX_SIZE;
		    sstate = Session.STUDENT_NOT_ON_COURSE;
		    if ( lname == null && fname == null && idvalue == null ) {
			ok = false;
		    }
		    else {
			view = "add";
		    }
		}
		else if ( doSearch.equals("2") ) {
		    sstate = Session.STUDENT_REMOVED;
		    view = "return";
		}
		else {
		    sstate = Session.STUDENT_ON_COURSE;
		    view = "select";
		}

		if ( ok ) {
		    if ( lname != null ) {
			lname = lname.replace('*', '%')+"%";
		    }
		    if ( fname != null ) {
			fname = fname.replace('*', '%')+"%";
		    }
		    if ( sgroup != null ) {
			try {
			    group = Integer.parseInt( sgroup );
			} catch ( NumberFormatException nfe ) {
			    group = -1;
			}
		    }
		    if ( idvalue != null ) {
			idvalue = idvalue.replace('*', '%')+"%";
		    }
		    Vector students = session.findStudents( lname,
							    fname,
							    idtype,
							    idvalue,
							    group,
							    sstate,
							    resultLimit );
		    context.put( "students", students );
		    useSNO = new Boolean( idtype == 1 );

		    if ( resultLimit > 0 && students.size() >= resultLimit ) {
			error = "Tarkenna hakua! Listassa aakkosjärjestyksessä "
			    +resultLimit+" ensimmäistä hakukriteerejä vastaavaa opiskelijaa.";
		    }
		    if ( sstate == Session.STUDENT_ON_COURSE || sstate == Session.STUDENT_REMOVED ) {
			ses.setAttribute( STUDENTS, students );
			ses.setAttribute( "useSNO", useSNO );
		    }
		}
		
	    } catch ( NumberFormatException nfe ) {
		nfe.printStackTrace();
	    }
	}

	/*
	 *  Opiskelijan tietojen muokkauspyyntö.
	 */
	else if ( doEdit != null ) {
	    view = "edit";
	    try {
		int sid = -1;
		String tmpsid = nullIfEmpty( req.getParameter("sid") );
		Object tmp = ses.getAttribute( STUDENTS );

		if ( tmp != null && tmpsid != null ) {
		    sid = Integer.parseInt( tmpsid );
		    Vector students = (Vector)tmp;
		    course.newSearch( true );
		    
		    int sn = students.size();
		    if ( sn > 0 ) {
			if ( sid < 0 ) sid = sn - 1;
			else if ( sid >= sn ) sid = 0;
			
			course.findBySSN( ((Student)students.get(sid)).getSSNID() );

			if ( sn > 1 ) 
			    context.put( "sid", new Integer(sid) );

			// Automaattinen välitallennus
// 			ctx.put( "autosave", "document.scores" );
		    }
		    else {
			error = "Pyynnön suorittaminen ei onnistunut. "
			    +"Suorita opiskelijoiden haku uudelleen.";
		    }
		}
	    } catch ( NumberFormatException nfe ) {
		nfe.printStackTrace();
	    }
	}

	else if ( doSaveAll != null ) {
	    error = result = "";
	    view = "select";
	    Object tmp = ses.getAttribute( STUDENTS );
		
	    if ( tmp != null ) {
		Vector students = (Vector)tmp;
		Student student = null; 

		for ( int sid=students.size()-1; sid >= 0; sid-- ) {
		    student = (Student)students.get( sid );
			
		    // opiskelijan poisto kurssilta
		    if ( nullIfEmpty( req.getParameter("rmv_"+sid) ) != null ) {
			try {
			    if ( course.removeStudent( student ) ) {
				students.remove( sid );
				result = "<li>Opiskelija "+student.getLabel()+" poistettu kurssilta.</li>\n"
				    +result;
			    }
			    else {
				error = "<li>Opiskelijan poistaminen kurssilta ei onnistunut: "
				    +course.getMessage()+"</li>\n"+error;
			    }
			} catch ( SQLException e ) {
			    error = "<li>Opiskelijan poistaminen kurssilta ei onnistunut: "
				+e.getMessage()+"</li>\n"+error;
			}
		    }

		    // opiskelijan ryhmän muuttaminen
		    else {			    
			int group = -1;
			String tmpgrp = nullIfEmpty( req.getParameter("grp_"+sid) );
			if ( tmpgrp != null ) {
			    try {
				group = Integer.parseInt( tmpgrp );
			    } catch ( NumberFormatException nfe ) {
				group = -1;
			    }
			}
			if ( group >= 0 && group < 100 ) {
			    try {
				if ( !course.chgrpStudent( student, group ) ) {
				    error = "<li>Opiskelijan "+student.getLabel()
					+" ryhmän vaihto ei onnistunut: "+course.getMessage()
					+error;
				}
			    } catch ( SQLException e ) {
				log.log("", e);
				error = "<li>Virhetilanne: "
				    +e.getMessage()+"</li>\n"
				    +error;
			    }
			} else if ( tmpgrp != null ) {
			    error = "<li>Opiskelijan "+student.getLabel()
				+" ryhmän vaihto ei onnistunut: virheellinen ryhmä '"+
				tmpgrp+"' </li>"+error;
			}
		    }
		}
		context.put( "students", students );
	       
		if ( error.length() > 0 ) error = "<ul>\n"+error+"</ul>\n";
		else error = null;
		if ( result.length() > 0 ) result = "<ul>\n"+result+"</ul>\n";
		else result = null;
	    }
	    else {
		error = "Toiminnon suorittaminen ei onnistunut: opiskelijoita ei löytynyt.";
	    }
	}

	else if ( doList != null ) {
	    view = "select";
	    Object tmp = ses.getAttribute( STUDENTS );
	    
	    if ( tmp != null ) {
		Vector students = (Vector)tmp;
		context.put( "students", students );
	    }
	}

	else if ( doSave != null ) {
	    try {	
		view = "edit";
		int sid = -1;
		String tmpsid = nullIfEmpty( req.getParameter("sid") );

		if ( tmpsid != null )
		    sid = Integer.parseInt( tmpsid );

		Vector parts = course.getParts();
		Student student = course.getStudent( 0 );
		
		if ( student == null ) {
		    error = "Opiskelija tuntematon.";
		}
		else {
		    error = result = "";
		    for ( int i=0; i < parts.size(); i++ ) {
			kurki.Part part = (kurki.Part)parts.get( i );
			Offering[] offerings = part.getOfferings();
			
			for ( int j=0; j < offerings.length; j++ ) {
			    Offering offering = offerings[ j ];
			    String score = req.getParameter("s"+part.getId()
							    +"_"
							    +offering.getId() );
			    
			    if ( score != null && !student.setScore( part, offering, score ) ) {
				error += "<li>"
				    +part.getLabel()+" "+(offering.getId()+1)
				    +" annettu virheellisesti: \""+score+"\".\n</li>";
			    }
			}
		    }
		    if ( !course.commitScores( req.getRemoteUser() ) ) {
			error = course.getMessage();
		    }

		    if ( sid >= 0 )
			context.put( "sid", new Integer( sid ) );
// 		    ctx.put( "autosave", "document.scores" );
		    context.put( "students", course.getStudents() );
		    
		    if ( error.length() > 0 ) error = "<ul>\n"+error+"</ul>\n";
		    else error = null;
		    if ( result.length() > 0 ) result = "<ul>\n"+result+"</ul>\n";
		    else result = null;
		}
	    } catch ( NumberFormatException nfe ) {
		nfe.printStackTrace();
	    }
	}

	/*
	 *  OPISKELIJAN LISÄÄMINEN KURSSILLE (ILMOITTAUTUMINEN/ILMOITUS)
	 */
	else if ( doAdd != null ) {
	    try {
		String ssn = nullIfEmpty( req.getParameter("ssn") );
		int group = Integer.parseInt( req.getParameter("group") );

// 		System.out.println(ssn +"-->"+group);
		if ( !course.addOnCourse( ssn, group ) ) {
		    error = "Opiskelijan lisäys kurssille ei onnistunut: "
			+course.getMessage();
		}
		else {
		    view = "select";
		    Vector students = session.findStudents( null,
							    null,
							    0,
							    ssn,
							    -1,
							    Session.STUDENT_ON_COURSE,
							    1 );
		    result = "Opiskelija lisätty kurssille.";
		    context.put( "students", students );

 		    ses.setAttribute( STUDENTS, students );
		}
	    } catch ( NumberFormatException nfe ) {
		nfe.printStackTrace();
	    }
	}

	/*
	 *  OPISKELIJAN LISÄÄMINEN KURSSILLE (ILMOITTAUTUMINEN/ILMOITUS)
	 */
	else if ( doReturn != null ) {
	    Object tmp = ses.getAttribute( STUDENTS );
	    error = result = "";

	    if ( tmp != null ) {
		Vector students = (Vector)tmp;
		Student student = null; 
		view = "return";
		    
		for ( int sid=students.size()-1; sid >= 0; sid-- ) {
		    student = (Student)students.get( sid );

		    if ( nullIfEmpty( req.getParameter("rtn_"+sid) ) != null ) { 

			// 		System.out.println(ssn +"-->"+group);
			if ( course.returnStudent( student ) ) {
			    students.remove(sid);
			    result = "<li>Opiskelija "+student.getLabel()+" palautettu kurssille.</li>\n"
				+result;
			}
			else {
			    error = "<li>Opiskelijan palauttaminen kurssille ei onnistunut: "
				+course.getMessage()+"</li>\n"+error;
			}
		    }
		}
		context.put( "students", students );
		
		if ( error.length() > 0 ) error = "<ul>\n"+error+"</ul>\n";
		else error = null;
		if ( result.length() > 0 ) result = "<ul>\n"+result+"</ul>\n";
		else result = null;
	    }
	}	 

	// Sulatus
	else if ( defreeze != null ) {
	    view = "edit";
	    int sid = -1; 
	    
	    try {
		sid = Integer.parseInt( defreeze );
		Student student = course.getStudent( 0 );
		
		if ( !course.defreezeStudent( student ) ) {
		    error = "<li>Opiskelijan <i>"+student.getLabel()+"</i>"
			+" sulatus ei onnistunut: "+course.getMessage()+"</li>";
		}
		else {
		    Object tmp = ses.getAttribute( STUDENTS );
		    if ( tmp != null ) {
			Vector students = (Vector)tmp;
			Student studentCopy = (Student)students.get( sid );
			studentCopy.defreeze();

			result = "<li>Opiskelija <i>"+student.getLabel()+"</i> sulatettu. \n"
			    +"<b>Huom!</b> Jotta tehdyt muutokset kirjautuisivat myös Oodiin,\n"
			    +"täytyy kurssi jäädyttää uudelleen.</li>";
		    }
		}
		if ( sid >= 0 )
		    context.put( "sid", new Integer( sid ) );
	    } catch ( NumberFormatException nfe ) {
		error = "<li>Sulatus ei onnistunut: Tuntematon opiskelija.</li>";
	    }
	}
	
// 	if ( nullIfEmpty( req.getParameter("asNotify") ) != null ) {
// 	    if ( result == null ) result = "";
// 	    result += Index.asNotify("Pisteet/arvosana");
// 	}

	context.put( "useSNO", useSNO );
	context.put( Index.ERROR, error );
	context.put( Index.RESULT, result );
	context.put( "view", view );
// 	System.out.println(error);

	return template;
    }
}
