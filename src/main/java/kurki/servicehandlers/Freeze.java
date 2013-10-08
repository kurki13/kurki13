package kurki.servicehandlers;

import kurki.util.Configuration;
import kurki.util.Log;
import kurki.model.Course;
import kurki.model.CourseInfo;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;

import org.apache.velocity.*;
import org.apache.velocity.context.*;
import org.apache.velocity.app.*;

import javax.mail.*; 
import javax.mail.internet.*;
import kurki.servlet.Index;
import kurki.util.LocalisationBundle;

public class Freeze extends AbstractVelocityServiceProvider {


   public static final String KURKIMAIL = "<a href=\"mailto:tktl-kurki@cs.helsinki.fi\">tktl-kurki@cs.helsinki.fi</a>";
   @Override
    public String handleRequest( kurki.Session_ session, 
				 HttpServletRequest req, 
				 HttpServletResponse res, 
				 Context context ) throws Exception {
        
        //lokalisaatiobundlen lisääminen kontekstiin
        context.put("bundle", ResourceBundle.getBundle("localisationBundle", kurki.Session_.locale));

	Log log         = (Log)Configuration.getProperty( "log" );
	String template = "freeze.vm";
	Course course = session.getSelectedCourse();
	String freeze = req.getParameter("freeze");
	String error = "";
	String result = "";

	if ( freeze != null ) {
	    String examDate = nullIfEmpty( req.getParameter("examDate") );

	    if ( examDate != null ) {
		if ( course.setExamDate( examDate ) ) {
		    course.commitGradeDef();
		}
		else {
		    error += "<li>"+course.getMessage();
		}
	    }
	    else if ( course.getExamDate() == null ) {
		error += "<li>" + LocalisationBundle.getString("annaSuorPvm") + "</li>";
	    }

	    if ( course.getExamDate() != null ) {
		CourseInfo ci = course.getCourseInfo();
		boolean isFirstTime = false;
		String resultList = "";

		String oodiAddress = (String)Configuration.getProperty("oodi");
		String subject = "";
		String msg = "";

		String userToAddress = req.getRemoteUser()+"@cs.helsinki.fi";

		/*
		 *  Tuloslista jäädytyksen tekijälle sekä Oodi-siirroista vastaavalle.
		 */
		subject = "KURKI: "+ci.getName();
		if (course.isFrozen()) {
		    subject += " korjaukset";
		}
		else {
		    subject += " tulokset";
		}

		VelocityContext results = new VelocityContext();
                
                //lokalisaatiobundlen lisääminen results kontekstiin
                results.put("bundle", ResourceBundle.getBundle("localisationBundle", kurki.Session_.locale));

		// Kaikki opiskelijat
		course.newSearch();
		results.put( "students", course.getStudents() );
		results.put( "selectedCourse", session.getSelectedCourse() );

		// Listaan mukaan otettavat tiedot
		results.put("inc_ssn", "true" ); // Opiskelijanro (HL08/8/)
                results.put( "inc_name", "true"); // Nimi
// 		results.put( "inc_lhsum", "true" ); // Laskaripisteet
// 		results.put( "inc_lhsumname", "LH-pisteet" ); //  Laskaripisteiden sarakeotsake
// 		results.put( "inc_htsum", "true" ); // Harjoitustyöpisteet
// 		results.put( "inc_htsumname", "HT-pisteet" ); //  Harjoitustyöpisteiden sarakeotsake
// 		results.put( "inc_koesum", "true" ); // Koepisteet 
// 		results.put( "inc_koesumname", "Koepisteet" ); //  Koepisteiden sarakeotsake
// 		results.put( "inc_sum", "true" ); // Yhteispisteet
// 		results.put( "inc_sumname", "Yhteispisteet" ); // Yhteispisteiden sarakeotsake
// 		results.put( "inc_signature", "true" ); // Hyväksyjän allekirjoitus

		results.put( "inc_statistics", "true" ); // Tilastot
		results.put( "inc_grade", "true" ); // Arvosana
		results.put( "inc_gradename", "Arvosana" ); // Arvosanan sarakeotsake
		results.put( "inc_accepted", "true" ); // Hyväksytyt
		results.put( "inc_failed", "true" ); // Hylätyt
// 			results.put( "inc_", "true" );

		// Korjauslista
		if (course.isFrozen()) {
		    results.put( "inc_changes", "true" ); // vain muutokset
		}

		Calendar calendar = Calendar.getInstance();
		results.put( "sysdate", 
			     +calendar.get(Calendar.DAY_OF_MONTH)+"."
			     +(calendar.get(Calendar.MONTH)+1)+"."
			     +calendar.get(Calendar.YEAR) );

		StringWriter w = new StringWriter();
                Velocity.mergeTemplate("checklist99.vm", "utf-8", results, w);
		msg = w.toString();

		// Jäädytys onnistui, lähetetään sähköpostia...
		if  ( course.freeze( 0 ) ) {
		    Properties props = new Properties();
		    props.put("mail.smtp.host", "localhost");
		    javax.mail.Session s = javax.mail.Session.getInstance(props,null);
		    InternetAddress from = new InternetAddress( (String)Configuration.getProperty("webmaster") );
		    MimeMessage message;
	    
		    // Jäädytysilmoitus Oodisiirroista vastaavalle ja jäädytyksen suorittajalle
		    message = new MimeMessage(s);
		    message.setFrom(from);
		    message.addRecipient(Message.RecipientType.TO, new InternetAddress( oodiAddress ) );  
		    message.addRecipient(Message.RecipientType.TO, new InternetAddress( userToAddress ) );
		    message.setSubject(subject);
		    message.setText(msg);
		    Transport.send(message);

		    result = "<center><h3>" + LocalisationBundle.getString("jaadytysInfo") + "</h3></center>\n"
			+"<ul>\n";

		    if (isFirstTime ) {
			result += "<li>" + LocalisationBundle.getString("jaadytysInfo2")
			    +": <a href='mailto:"+oodiAddress+"'>"+oodiAddress+"</a> "
			    + LocalisationBundle.getString("jaadytysInfo2Jatkoa") + " "
			    +KURKIMAIL
                            +"</li>";
		    }
		    result += "<li>" + LocalisationBundle.getString("jaadytysInfo3") + ".</li>";

		    result += "<li>" + LocalisationBundle.getString("jaadytysInfo4") 
			+".</li></ul>\n";

// 		    // Heitetään käyttäjä pihalle järjestelmästä
// 		    req.getSession().invalidate();
// 		    res.sendRedirect( "https://ilmo.cs.helsinki.fi/kurki/jaassa.html" );
		}
		else {
		    String errMsg = course.getMessage();
		    if (errMsg == null) {
			error = LocalisationBundle.getString("jaadytysEpaonnistui") + ". "
			    +LocalisationBundle.getString("jaadytysEpaonnistuiInfo") + " "
			    +KURKIMAIL
                            +"</li>";
		    }
		    else {
			error = LocalisationBundle.getString("jaadytysEpaonnistui") + " ("+errMsg+"). "
			    +LocalisationBundle.getString("jaadytysEpaonnistuiInfo") + " "
			    +KURKIMAIL
                            +"</li>";
		    }
		}
	    }
	}

	if ( nullIfEmpty(result) != null )
	    context.put( Index.RESULT, result );
	if ( nullIfEmpty(error) != null )
	    context.put( Index.ERROR, error );
	
        return template;
    }
}

