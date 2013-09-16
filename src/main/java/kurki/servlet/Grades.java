package kurki.servlet;

import kurki.*;
import service.*;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import org.apache.velocity.context.*;

public class Grades extends AbstractVelocityServiceProvider 
    implements Serializable {

    public Grades( Service service ) {
 	super( service );
    }

    @Override
    public String handleRequest( Session session, 
				 HttpServletRequest req, 
				 HttpServletResponse res, 
				 Context ctx ) throws Exception {

	Log log         = (Log)Configuration.getProperty( "log" );
	String template = "grades.vm";
	Course course   = session.getSelectedCourse();
	String error = "";
	String result = "";
	boolean ok = true;

	String[] partNames = {"LH", "HT", "KOE", "Arvosana"};

	String saveBasic = nullIfEmpty( req.getParameter("saveBasic") );
	String makeAssessment = nullIfEmpty( req.getParameter("makeAssessment") );
	String modBoundaries = nullIfEmpty( req.getParameter("modBoundaries") );
	String saveBoundaries  = nullIfEmpty( req.getParameter("saveBoundaries") );

	/*
	 * Tallenna lomakkeen tiedot
	 */
	if ( !course.isFrozen()
	     && ( saveBasic != null || modBoundaries != null || makeAssessment != null ) ) {
	    
	    Vector parts = course.getParts();
	    String examDate = nullIfEmpty( req.getParameter("examDate") );

	    if ( examDate != null ) {
		if ( !course.setExamDate( examDate ) ) {
		    error += "<li>Suorituspäivä ei kelpaa: '"
			+examDate+"'. (pp.kk.vvvv) ["+course.getMessage()+"]";
		}
	    }
	    else if ( course.getExamDate() == null ) {
		error += "<li>Anna suorituspäivämäärä!</li>";
		ok = false;
	    }
	
	    String scale = nullIfEmpty( req.getParameter("scale") );
	    course.setScale( scale );

	    try {
		course.setGradingConvention( Integer.parseInt( req.getParameter("convention") ) );
	    } catch ( NumberFormatException nfe ) {}

	    for ( int i=0; i < parts.size()-2; i++ ) {
		kurki.Part part = (kurki.Part)parts.get( i );
		int ptype = part.getType();
		String val = null;
		boolean valOK = true;

		val = nullIfEmpty( req.getParameter( "reqo_"+ptype ) );
		if ( val != null ) {
		    try {
			if ( !part.setRequiredOfferings( Integer.parseInt( val ) ) )
			    valOK = false;
		    } catch ( NumberFormatException nfe ) {
			valOK = false;
		    }
		    if ( !valOK )
			error += "<li>"+partNames[ptype]
			    +": pakollisten osasuoritusten lkm annettu virheellisesti.</li>";
		    valOK = true;
		}
		val = nullIfEmpty( req.getParameter( "reqs_"+ptype ) );
		if ( val != null ) {
		    try {
			if ( !part.setRequiredScore( Integer.parseInt( val ) ) )
			    valOK = false;
		    } catch ( NumberFormatException nfe ) {
			valOK = false;
		    }
		    if ( !valOK )
			error += "<li>"+partNames[ptype]
			    +": osuuden hyväksymisraja  annettu virheellisesti.</li>";
		    valOK = true;
		}
		val = nullIfEmpty( req.getParameter( "xtr_"+ptype ) );
		if ( val != null && ptype != kurki.Part.KOE ) {
		    try {
			if ( !part.setXtrScore( Integer.parseInt( val ) ) )
			    valOK = false;
		    } catch ( NumberFormatException nfe ) {
			valOK = false;
		    }
		    if ( !valOK )
			error += "<li>"+partNames[ptype]+": lisäpistemaksimi annettu virheellisesti.</li>";
		    valOK = true;
		}
		val = nullIfEmpty( req.getParameter( "xtr1st_"+ptype ) );
		if ( val != null && ptype != kurki.Part.KOE ) {
		    try {
			if ( !part.setFirstXtrScore( Integer.parseInt( val ) ) )
			    valOK = false;
		    } catch ( NumberFormatException nfe ) {
			    valOK = false;
		    }
		    if ( !valOK )
			error += "<li>"+partNames[ptype]
			    +": ensimmäisen lisäpisteen raja annettu virheellisesti.</li>";
		    valOK = true;
		}
		val = nullIfEmpty( req.getParameter( "step_"+ptype ) );
		if ( val != null && ptype != kurki.Part.KOE ) {
		    try {
			if ( !part.setXtrStep( Double.parseDouble( val ) ) )
			    valOK = false;
		    } catch ( NumberFormatException nfe ) {
			valOK = false;
		    }
		    if ( !valOK )
			error += "<li>"+partNames[ptype]
			    +": lisäpisteiden kartuntaväli annettu virheellisesti.</li>";
		    valOK = true;
		}
	    }
	    if ( !course.commitGradeDef() ) {
		ok = false;
		error = course.getMessage();
	    }
	}

	/*
	 *  Pisterajojen muokkauspyyntö.
	 */

	if ( modBoundaries != null ) {
	    template =  "grades_boundaries.vm";
	}

	/*
	 *   Pisterajojen tallennus.
	 */
	else if ( saveBoundaries != null && ok ) {
	    Vector parts = course.getParts();
	    
	    for ( int i=0; i < parts.size(); i++ ) {
		kurki.Part part = (kurki.Part)parts.get( i );
		int ptype = part.getType();
		int xtrScore = part.getXtrScore();
		boolean borderOK = true;
		String val = null;		
		
		for ( int s=0; s < xtrScore; s++ ) {
		    borderOK = true;
		    val = nullIfEmpty( req.getParameter( "sb_"+ptype+"_"+s ) );
		    
		    if ( val != null ) {
			try {
			    if ( !part.setScoreBoundary( s, Integer.parseInt( val ) ) )
				borderOK = false;
			} catch ( NumberFormatException nfe ) {
			    borderOK = false;
			}
			if ( !borderOK )
			    error += "<li>"+partNames[ptype]
				+": "+s+":n pisteen raja annettu virheellisesti: '"
				+val+"'. (1-"+part.getMaxScoreCount()+")</li>";
		    }
		}
		
	    }

	    if ( !course.commitGradeDef() ) {
		error += course.getMessage();
	    }
	    else {
		result += "Piste- ja arvosanarajat tallennettu. ";
	    }
	}


	/*
	 *  Arvostelu
	 */
	else if ( makeAssessment != null && ok ) {
	    if ( course.makeAssessment( req.getRemoteUser() ) ) {
		Calendar calendar = Calendar.getInstance();
		int minute = calendar.get(Calendar.MINUTE);

		result = "Arvostelu suoritettu "
		    +calendar.get(Calendar.DAY_OF_MONTH)
		    +"."+(calendar.get(Calendar.MONTH) + 1)
		    +"."+calendar.get(Calendar.YEAR)+" klo " 
		    +calendar.get(Calendar.HOUR_OF_DAY)+":"
		    +(minute < 10 ? "0"+minute : ""+minute)+".";

		// Kaikki opiskelijat
		course.newSearch(true);
		ctx.put( "students", course.getStudents() );
		ctx.put( "selectedCourse", session.getSelectedCourse() );
		ctx.put( "resultList", "true" );
		ctx.put( "sysdate", 
			     +calendar.get(Calendar.DAY_OF_MONTH)+"."
			     +(calendar.get(Calendar.MONTH)+1)+"."
			     +calendar.get(Calendar.YEAR) );
	    }
	    else {
		error += "<li>Virhe: "+course.getMessage()+"</li>";
	    }
	}

	ctx.put( Index.ERROR, nullIfEmpty( error ) );
	ctx.put( Index.RESULT, nullIfEmpty( result ) );
	return template;
    }
}
