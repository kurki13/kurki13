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

import javax.mail.*; 
import javax.mail.internet.*;

public class Freeze extends AbstractVelocityServiceProvider {


   public static final String KURKIMAIL = "<a href=\"mailto:tktl-kurki@cs.helsinki.fi\">tktl-kurki@cs.helsinki.fi</a>";
  
   public Freeze( service.Service service ) {
 	super( service );
    }

    public String handleRequest( kurki.Session session, 
				 HttpServletRequest req, 
				 HttpServletResponse res, 
				 Context ctx ) throws Exception {

	Log log         = (Log)Configuration.getProperty( "log" );
	String template = "freeze.vm";
	Course course = session.getSelectedCourse();
	String freeze = req.getParameter("freeze");
	String error = "";
	String result = "";

	if ( freeze != null ) { // if ( freeze != null )
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
		error += "<li>Anna suoritusp�iv�m��r�!</li>";
	    }

	    if ( course.getExamDate() != null ) { // if ( course.getExamDate() != null )
		CourseInfo ci = course.getCourseInfo();
		boolean isFirstTime = false;
		String resultList = "";

		String oodiAddress = (String)Configuration.getProperty("oodi");
		String subject = "";
		String msg = "";

		String userToAddress = req.getRemoteUser()+"@cs.helsinki.fi";

		/*
		 *  Tuloslista j��dytyksen tekij�lle sek� Oodi-siirroista vastaavalle.
		 */
		subject = "KURKI: "+ci.getName();
		if (course.isFrozen()) {
		    subject += " korjaukset";
		}
		else {
		    subject += " tulokset";
		}

		VelocityContext results = new VelocityContext();

		// Kaikki opiskelijat
		course.newSearch();
		results.put( "students", course.getStudents() );
		results.put( "selectedCourse", session.getSelectedCourse() );

		// Listaan mukaan otettavat tiedot
		results.put( "inc_ssn", "true" ); // Opiskelijanro (HL08/8/)
                results.put( "inc_name", "true"); // Nimi
// 		results.put( "inc_lhsum", "true" ); // Laskaripisteet
// 		results.put( "inc_lhsumname", "LH-pisteet" ); //  Laskaripisteiden sarakeotsake
// 		results.put( "inc_htsum", "true" ); // Harjoitusty�pisteet
// 		results.put( "inc_htsumname", "HT-pisteet" ); //  Harjoitusty�pisteiden sarakeotsake
// 		results.put( "inc_koesum", "true" ); // Koepisteet 
// 		results.put( "inc_koesumname", "Koepisteet" ); //  Koepisteiden sarakeotsake
// 		results.put( "inc_sum", "true" ); // Yhteispisteet
// 		results.put( "inc_sumname", "Yhteispisteet" ); // Yhteispisteiden sarakeotsake
// 		results.put( "inc_signature", "true" ); // Hyv�ksyj�n allekirjoitus

		results.put( "inc_statistics", "true" ); // Tilastot
		results.put( "inc_grade", "true" ); // Arvosana
		results.put( "inc_gradename", "Arvosana" ); // Arvosanan sarakeotsake
		results.put( "inc_accepted", "true" ); // Hyv�ksytyt
		results.put( "inc_failed", "true" ); // Hyl�tyt
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
		Velocity.mergeTemplate("checklist99.vm", results, w );
		msg = w.toString();

		// J��dytys onnistui, l�hetet��n s�hk�postia...
		if  ( course.freeze( 0 ) ) {
		    Properties props = new Properties();
		    props.put("mail.smtp.host", "localhost");
		    javax.mail.Session s = javax.mail.Session.getInstance(props,null);
		    InternetAddress from = new InternetAddress( (String)Configuration.getProperty("webmaster") );
		    MimeMessage message = null;
	    
		    // J��dytysilmoitus Oodisiirroista vastaavalle ja j��dytyksen suorittajalle
		    message = new MimeMessage(s);
		    message.setFrom(from);
		    message.addRecipient(Message.RecipientType.TO, new InternetAddress( oodiAddress ) );  
		    message.addRecipient(Message.RecipientType.TO, new InternetAddress( userToAddress ) );
		    message.setSubject(subject);
		    message.setText(msg);
		    Transport.send(message);

		    result = "<center><h3>Kurssi on j��dytetty</h3></center>\n"
			+"<ul>\n";

		    if (isFirstTime ) {
			result += "<li>Jos haluat perua j��dytyksen, niin etteiv�t tulokset siirry Oodiin, "
			    +"ota pikaisesti yhteytt�: <a href='mailto:"+oodiAddress+"'>"+oodiAddress+"</a> "
			    +"sek� KurKi-j�rjestelm�n yll�pit�j��n "
			    +KURKIMAIL
                            +"</li>";
		    }
		    result += "<li>Saat j��dytyksest� kuittauksena s�hk�postitse tuloslistan.</li>";

		    result += "<li>Jos haluat korjata opiskelijan puutteellisia tai virheellisi� pisteit�, "
			+"voit tehd� sen, sulattamalla ensin kyseisen opiskelijan. T�m�n j�lkeen "
			+"kurssi on kuitenkin j��dytett�v� uudelleen.</li>"
			+"</ul>\n";

// 		    // Heitet��n k�ytt�j� pihalle j�rjestelm�st�
// 		    req.getSession().invalidate();
// 		    res.sendRedirect( "https://ilmo.cs.helsinki.fi/kurki/jaassa.html" );
		}
		else {
		    String errMsg = course.getMessage();
		    if (errMsg == null) {
			error = "Kurssin j��dytt�minen ei onnistunut. "
			    +"Asian korjaamiseksi ota yhteytt� KurKi-j�rjestelm�n yll�pit�j��n "
			    +KURKIMAIL
                            +"</li>";
		    }
		    else {
			error = "Kurssin j��dytt�minen ei onnistunut ("+errMsg+"). "
			    +"Asian korjaamiseksi ota yhteytt� KurKi-j�rjestelm�n yll�pit�j��n "
			    +KURKIMAIL
                            +"</li>";
		    }
		}
	    }
	}

	if ( nullIfEmpty(result) != null )
	    ctx.put( Index.RESULT, result );
	if ( nullIfEmpty(error) != null )
	    ctx.put( Index.ERROR, error );
	
        return template;
    }
}
