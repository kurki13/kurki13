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

public class Entry extends AbstractVelocityServiceProvider {

    public static final String RANGE_OP = "..";

    public Entry( Service service ) {
 	super( service );
    }

    public String handleRequest( Session session, 
				 HttpServletRequest req, 
				 HttpServletResponse res, 
				 Context ctx ) throws Exception {

	Log log         = (Log)Configuration.getProperty( "log" );
	String template = "entry.vm";
	Course course   = session.getSelectedCourse();
	String error = "";
	String result = "";

	// Opiskelijasuodattimen tulkinta
	String studentFilter = "";
	String studentFilterDesc = "";

	/*
	 *  Http-parametrit 
	 */
	String ptype = req.getParameter( "ptype" );
	String offering    = req.getParameter( "offering" );
	String chtype  = nullIfEmpty( req.getParameter( "chtype" ) );
	String commit = nullIfEmpty( req.getParameter( "commit" ) );

	String scoredef   = req.getParameter( "scoredef" );
	String defreeze   = nullIfEmpty( req.getParameter( "defreeze" ) );
	String maxscore   = nullIfEmpty( req.getParameter( "maxscore" ) );
	String minscore   = nullIfEmpty( req.getParameter( "minscore" ) );

	// annettu opiskelijasuodatin
 	String filter   = req.getParameter( "filter" );
	
	/*
	 *  Toiminnot.
	 */
       ctx.put("pt",ptype);
       //aseta puuttuva offering valinta
       if (offering==null)
          offering="0";
	/*
	 *  Kirjattavan osasuoritteen valinta.
	 */
	if ( chtype == null && 
	     ptype != null && 
	     ptype.equals( ""+course.getPartType() ) ) {

	    String err = "";
	    try {
		err = "suoritteen tyyppi (ptype)";
		course.selectPart( Integer.parseInt( ptype ) );

		err = "suoritteen kerta (offering)";
		course.selectOffering( Integer.parseInt( offering ) );

		err = "";
	    } catch ( NumberFormatException nfe ) {
		nfe.printStackTrace();
		log.log( "Virheellinen http-parametri: "+err, nfe );
	    }

	    /*
	     *  Opiskelijat: ryhm�n, henkil�tunnuksen tai sukunimem mukaan.
	     */
	    course.newSearch();
	    StringTokenizer st = new StringTokenizer( filter, ",\t\n\r\f" );
	    while ( st.hasMoreTokens() ) {
		String term = st.nextToken().trim();

		if ( term.length() == 0 ) continue;
		
		if ( studentFilter.length() > 0 ) studentFilter += ", ";

		// Haku ryhm�n mukaan, jos annettu kokonaisluku...
		try {
		    int g = Integer.parseInt( term );
		    course.findByGroup( g );
		    studentFilter += g;
		}

		// ...muuten haetaan sukunimen tai henkil�tunnuksen mukaan
		catch ( NumberFormatException nfe ) {
		    // Haetaan sukunimiv�li�
		    int r = term.indexOf( RANGE_OP );
		    if ( r >= 0 ) {
			String from = term.substring(0, r).trim();
			String to = term.substring(r + RANGE_OP.length(),
						   term.length()).trim();

			course.findByLastNameRange( nullIfEmpty(from),
						    nullIfEmpty(to) );

			studentFilter += from+RANGE_OP+to;
		    } 
		    // Haetaan sukunime� tai henkil�tunnusta
		    else {
			studentFilter += term;
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

	    if ( course.getSelectedPart().maxScoreDefined() )
		ctx.put( "autosave", "document.scores" );
	    ctx.put( "students", course.getStudents() );
	}

	/*
	 *  Vaihdetaan kirjattavan osasuoritteen tyyppi�.
	 */
	else if ( ptype != null ) {
	    try {
		int newPType = Integer.parseInt( ptype );
		course.selectPart( newPType );

		if ( newPType >= Part.ARVOSANA ) {
		    course.getSelectedPart().selectOffering( 0 );
		}
		
		// Ota kommentti pois, jos haluat, ettei opiskelijoita n�ytet�
		// heti kirjattavan suoritteen tyyppi� muutettaessa.
// 		course.getSelectedPart().selectOffering( Part.UNDEF );
		studentFilter = filter;
	    } catch ( NumberFormatException nfe ) {
		log.log( "Virheellinen http-parametri: ", nfe );
	    }

	    // Kommentoi pois, jos haluat, ettei opiskelijoita n�ytet�
	    // heti kirjattavan suoritteen tyyppi� muutettaessa.
	    if ( course.getSelectedPart().maxScoreDefined() )
		ctx.put( "autosave", "document.scores" );
	    ctx.put( "students", course.getStudents() );
	}

	/*
	 *  Kirjattavan osasuoritteen perustietojen m��ritys.
	 */
	else if ( scoredef != null ) {
	    int maxs = -1;
	    int mins = -1;
	    try {
		Offering selectedOffering = course.getSelectedPart().getSelectedOffering();

		if ( maxscore != null ) {
		    maxs = Integer.parseInt( maxscore );
		    selectedOffering.setMaxScore( maxs );
		}

		if ( minscore != null ) {
		    mins = Integer.parseInt( minscore );
		    selectedOffering.setMinScore( mins );
		}

		if ( course.getSelectedPart().maxScoreDefined() )
		    ctx.put( "autosave", "document.scores" );
		ctx.put( "students", course.getStudents() );
	    } catch ( NumberFormatException nfe ) {
		if ( maxs == -1 ) error += "<li>Osasuorituksen maksimipisteet annettu virheellisesti.</li>";
		else if ( mins == -1 ) error += "<li>Osasuorituksen hyv�ksymisraja annettu virheellisesti.</li>";
	    }
	    course.commitCourseInfo();

	    studentFilter = null;
	}

	/*
	 *  Kirjataan annetut pisteet.
	 */
	else if ( commit != null ) {
	    Vector students = course.getStudents();
            boolean pointErrors= false;

	    for ( int i=0; i < students.size(); i++ ) {
		Student s = (Student)students.get(i); // K�sitelt�v� opiskelija
		String textPoints = req.getParameter( "t"+s.getId() ); // Pisteet tekstikent�st� (t)
		String radioPoints = req.getParameter( "r"+s.getId() ); // Pisteet radiosta
                
                   
		if ( textPoints != null ) {
                    if (radioPoints != null && !textPoints.equals(radioPoints)) {
			error += "<li>Oppilaan "+s.getLabel()+" pisteet ristiriitaiset, ei p�ivitetty \n</li>";
                        pointErrors=true;
		    }
		    else if ( !s.setScore( course.getSelectedPart(), textPoints.toUpperCase() ) )
			error += "<li>Oppilaan "+s.getLabel()+" pisteet annettu virheellisesti: "+textPoints+"\n</li>";
		}
	    }
            if (pointErrors)
		error += "<li><font color=\"red\">JavaScript ei toimi</font></li>";
	    if ( !course.commitScores( req.getRemoteUser() ) ) {
		error += course.getMessage();
	    }
	    if ( course.getSelectedPart().maxScoreDefined() )
		ctx.put( "autosave", "document.scores" );
	    ctx.put( "students", course.getStudents() );

	    studentFilter = null;
	}
	/*
	 * Opiskelijan sulatus
	 */
	else if ( defreeze != null ) {
	    try {
		int sid = Integer.parseInt( defreeze );
		Student s = course.getStudent( sid );
		if ( !course.defreezeStudent( s ) ) {
		    error += "<li>Sulatus ei onnistunut: "+course.getMessage()+"</li>";
		}
		else {
		    result += "<li>Opiskelija <i>"+s.getLabel()+"</i> sulatettu. \n"
			+"<b>Huom!</b> Jotta tehdyt muutokset kirjautuisivat my�s Oodiin,\n"
			+"t�ytyy kurssi j��dytt�� uudelleen.</li>";
		}
	    } catch ( NumberFormatException nfe ) {
		error += "<li>J��dytys ei onnistunut: Tuntematon opiskelija.</li>";
	    }
	    
	    if ( course.getSelectedPart().maxScoreDefined() )
		ctx.put( "autosave", "document.scores" );
	    ctx.put( "students", course.getStudents() );
	}
	else {
	    course.selectOffering( Part.UNDEF );
	}

	String par = nullIfEmpty( req.getParameter("studentFilter") );
	if ( par != null && studentFilter == null ) {
	    studentFilter = par;
	}

	studentFilterDesc = course.getSelectDescription();
	
	if ( nullIfEmpty( req.getParameter("asNotify") ) != null ) {
	    ctx.put( Index.RESULT, Index.asNotify("Pisteet/arvosanat") );
	}
	
// 	System.out.println("here");

// 	// Annettu opiskelijasuodatin
// 	ctx.put( "filter", nullIfEmpty( filter ) );

	// Opiskelija suodattimen tulkinta
	ctx.put( "studentFilter",
		 ( nullIfEmpty( studentFilter ) == null
		   ? ""
		   : studentFilter ) );
	ctx.put( "studentFilterDesc",
		 ( nullIfEmpty( studentFilterDesc ) == null
		   ? "kaikki kurssin opiskelijat"
		   : studentFilterDesc ) );

	ctx.put( Index.ERROR, nullIfEmpty( error ) );
	ctx.put( Index.RESULT, nullIfEmpty( result ) );	
/*
	 *  Laskuharjoitus/koe/harjoitusty� kerrat.
	 */
	if ( course.getSelectedPart() != null )
	    ctx.put( "offerings", course.getSelectedPart().getOfferings() );

        return template;
    }
}

