package kurki;

import service.*;

import java.util.*;
import java.sql.*;

public class Session implements java.io.Serializable {
    
    private static int rolehlp = 0;

    // Tunnistetun käyttäjäryhmät. Huom! Lueteltava oikeuksien mukaisessa
    // järjestyksessä, alkaen siitä, jolla vähiten oikeuksia.
    public static final int OUTSIDER   = rolehlp++;
    public static final int TUTOR      = rolehlp++; 
    public static final int PRIVILEGED = rolehlp++; // käytännöllinen vastuuhlö 
    public static final int SUPER      = rolehlp++; // kaikkiin kursseihin oikeudet

    public static final int NO_OF_ROLES = rolehlp;

    public static final int STUDENT_ON_COURSE = 0;
    public static final int STUDENT_NOT_ON_COURSE = 1;
    public static final int STUDENT_REMOVED = 2;

    private static int namehlp = 0;
    public static final String ENTRY =         ++namehlp+"entry";
    public static final String PARTICIPANTS =  ++namehlp+"participants";
    public static final String COURSE_BASICS = ++namehlp+"coursebasics";
    public static final String CHECKLIST =     ++namehlp+"checklist";
    public static final String GRADES =        ++namehlp+"grades";
    public static final String RESULT_LIST =   ++namehlp+"resultlist";
    public static final String FREEZE =        ++namehlp+"freezing";
    public static final String LOGOUT =        ++namehlp+"logout";

    public static final String LOGIN_SEPARATOR = "*";

    /**
     ** Kuinka pitkään kurssin tietoihin saa tehdä muutoksia.
     */
    public static final int MONTHS_OPEN = 12;
    public static final int SUPER_OPEN = 48;

    public static final int DEFAULT_RS_MAX_SIZE = 50;

    private static boolean initialized = false;

    protected String ruser = null;

    private static String superUsers = LOGIN_SEPARATOR;

    protected static ServiceManager serviceManager = null;

    protected static final String COURSE_INFOS = 

	// LUENTO- (JA LABORATORIOKURSSIT)
	"SELECT DISTINCT k.kurssikoodi, k.lukuvuosi, k.lukukausi, k.tyyppi, k.tila,\n"
	+"    k.kurssi_nro, k.nimi AS nimi, k.alkamis_pvm AS alkamis_pvm, k.paattymis_pvm,\n"
	+"    decode(k.tila, 'J', 'X', 'A') AS orderBy\n"
	+"  FROM kurssi k, opetus o, opetustehtavan_hoito oh, henkilo h\n"
	+"  WHERE k.paattymis_pvm > to_date('31.12.2001','DD.MM.YYYY')\n"
	+"    AND ADD_MONTHS( k.paattymis_pvm, "+MONTHS_OPEN+" ) >= SYSDATE\n"
//	+"    AND k.alkamis_pvm <= SYSDATE+30\n"
	+"    AND k.tila not in ('S', 'O')\n"
// 	+"    AND k.tila not in ('S', 'O', 'J')\n" // ei jäädytettyjä kursseja
//	+"    AND k.tyyppi in ('K', 'S')\n" // vain luentokurssit ja seminaarit
 	+"    AND k.tyyppi in ('K', 'S', 'A')\n" // luento- ja laboratoriokurssit sekä seminaarit
	+"    AND o.kurssikoodi = k.kurssikoodi AND oh.kurssikoodi = k.kurssikoodi\n"
	+"    AND o.lukukausi = k.lukukausi AND oh.lukukausi = k.lukukausi\n"
	+"    AND o.lukuvuosi = k.lukuvuosi AND oh.lukuvuosi = k.lukuvuosi\n"
	+"    AND o.tyyppi = k.tyyppi AND oh.tyyppi = k.tyyppi\n"
	+"    AND o.kurssi_nro = k.kurssi_nro AND oh.kurssi_nro = k.kurssi_nro\n"
	+"    AND oh.htunnus = h.htunnus\n"
	+"    AND h.ktunnus = ?\n"

	// KOKEET
	+"UNION\n"
	+"(SELECT DISTINCT ku.kurssikoodi, ku.lukuvuosi, ku.lukukausi, ku.tyyppi, ku.tila,\n"
	+"    ku.kurssi_nro, ku.nimi || ', koe ' || TO_CHAR(ku.paattymis_pvm, 'DD.MM.YY'),\n"
	+"    ku.alkamis_pvm, ku.paattymis_pvm, decode(ku.tila, 'J', 'X', 'A') AS orderBy\n"
	+"  FROM kurssi ku, koe ko, henkilo h\n"
	+"  WHERE ku.paattymis_pvm > to_date('31.12.2001','DD.MM.YYYY')\n"
	+"    AND ADD_MONTHS( ku.paattymis_pvm, "+MONTHS_OPEN+" ) >= SYSDATE\n"
//	+"    AND ku.paattymis_pvm <= SYSDATE+30\n"
	+"    AND ku.tyyppi = 'L'\n" // vain kokeet
	+"    AND ku.tila not in ('S', 'O')\n"
// 	+"    AND ku.tila not in ('S', 'O', 'J')\n" // ei jäädytettyjä kursseja
	+"    AND ko.kurssikoodi = ku.kurssikoodi\n"
	+"    AND ko.lukukausi = ku.lukukausi\n"
	+"    AND ko.lukuvuosi = ku.lukuvuosi\n"
	+"    AND ko.tyyppi = ku.tyyppi\n"
	+"    AND ko.kurssi_nro = ku.kurssi_nro\n"
	+"    AND h.htunnus = ko.htunnus\n"
	+"    AND h.ktunnus = ?)\n"
	+"ORDER BY orderBy ASC, nimi ASC, alkamis_pvm ASC";

    protected static final String SUPER_INFOS = 
	"SELECT DISTINCT k.kurssikoodi, k.lukuvuosi, k.lukukausi, k.tyyppi, k.tila,\n"
	+"    k.kurssi_nro, k.nimi AS nimi, k.alkamis_pvm AS alkamis_pvm, k.paattymis_pvm,\n"
	+"    decode(k.tila, 'J', 'X', 'A') AS orderBy\n"
	+"  FROM kurssi k\n"
	+"  WHERE k.paattymis_pvm > to_date('31.12.2001','DD.MM.YYYY')\n"
	+"    AND ADD_MONTHS( k.paattymis_pvm, "+SUPER_OPEN+" ) >= SYSDATE\n"
//	+"    AND k.alkamis_pvm <= SYSDATE+30\n"
//      +"    AND k.tyyppi in ('K', 'S')\n" // vain luentokurssit ja seminaarit
 	+"    AND k.tyyppi in ('K', 'S', 'A')\n" // luento- ja laboratoriokurssit sekä seminaarit
	+"    AND k.tila not in ('S', 'O')\n"
// 	+"    AND k.tila not in ('S', 'O', 'J')\n" // ei jäädytettyjä kursseja

	// KOKEET
	+"UNION\n"
	+"(SELECT DISTINCT ku.kurssikoodi, ku.lukuvuosi, ku.lukukausi, ku.tyyppi, ku.tila,\n"
	+"    ku.kurssi_nro, ku.nimi || ', koe ' || TO_CHAR(ku.paattymis_pvm, ' DD.MM.YY'),\n"
	+"    ku.alkamis_pvm, ku.paattymis_pvm, decode(ku.tila, 'J', 'X', 'A') AS orderBy\n"
	+"  FROM kurssi ku\n"
	+"  WHERE ku.paattymis_pvm > to_date('31.12.2001','DD.MM.YYYY')\n"
	+"    AND ADD_MONTHS( ku.paattymis_pvm, "+SUPER_OPEN+" ) >= SYSDATE\n"
//	+"    AND ku.paattymis_pvm <= SYSDATE+30\n"
	+"    AND ku.tila not in ('S', 'O')\n"
// 	+"    AND ku.tila not in ('S', 'O', 'J')\n" // ei jäädytettyjä kursseja
	+"    AND ku.tyyppi = 'L')\n" // vain kokeet
	+"ORDER BY orderBy ASC, nimi ASC, alkamis_pvm ASC";

    static {
	initialize();
    }


    /**
     **  Toteutettujen toimintojen perustietojen määrittely:
     **  <toiminnon id>, <kuka saa suorittaa>, <kuvaus>.
     */
    public static void initialize() {
	if (initialized) return;

	initialized = true;
	try {
	    ServiceManager.setNoOfRoles( NO_OF_ROLES );
	    
	    ServiceManager.defineService( ENTRY,         TUTOR,    "Suoritteiden kirjaus" );
	    ServiceManager.defineService( PARTICIPANTS,  TUTOR,    "Osallistujatietojen muutokset" );
	    ServiceManager.defineService( COURSE_BASICS, TUTOR,    "Kurssin perustietojen muutokset" );
  	    ServiceManager.defineService( CHECKLIST,     TUTOR,    "Listat" );
    	    ServiceManager.defineService( GRADES,        TUTOR,    "Arvostelu" );
  	    ServiceManager.defineService( RESULT_LIST,   TUTOR,    "Tuloslistat" );
   	    ServiceManager.defineService( FREEZE,        TUTOR,    "Kurssin jäädytys" );
//   	    ServiceManager.defineService( LOGOUT,        OUTSIDER, "Kirjaudu ulos" );
	    
	    ServiceManager.lockServices();

	    serviceManager = ServiceManager.getInstance();
	} catch ( Exception e ) {
	    e.printStackTrace();
	    System.exit(0);
	}
    }

    /*
     *  SUPER USERS
     */
    public static void setSuperUsers( String susers ) {
	if ( susers != null ) {
	    StringTokenizer st = new StringTokenizer( susers, "," );
	
	    while ( st.hasMoreTokens() ) {
		String login = st.nextToken().trim();
		if ( login.length() > 0 )
		    superUsers += login+LOGIN_SEPARATOR;
	    }
	}
    }
    /**
     **   Käsittelyssä oleva kurssi.
     */
    protected Course selectedCourse = null;


    /**
     **  Kurssit (CourseInfo), joita käyttäjä on oikeutettu päivittämään.
     */
    protected Hashtable courses = new Hashtable();

    protected Vector coursesList = new Vector();

    /**
     **  Valittu palvelu.
     */ 
    protected Service selectedService = null;


    /**
     **  Estetään ilmentymien luonti ulkopuolisilta.
     */
    protected Session( String ruser )
	throws InitFailedException, SQLException, NullIdException, ClassNotFoundException {
	//MKCT: commented if ( ruser == null || ruser.length() == 0 ) throw new InitFailedException("Käyttäjän tunnistus epäonnistui.");
	this.ruser = ruser;
	init();
    }


    /**
     **  Onko käyttäjä jo valinnut käsiteltävän kurssin.
     */
    public boolean courseSelected() {
	return this.selectedCourse != null;
    }

    public synchronized Vector findStudents( String  lname,
				String  fname,
				int     idtype,
				String  idvalue,
				int     group,
				int     state,
				int     resultLimit )
	throws SQLException, NullIdException, NullParameterException, ClassNotFoundException {

	CourseInfo courseInfo = this.selectedCourse.getCourseInfo();

	boolean onCourse = state != STUDENT_NOT_ON_COURSE;
	String sstate = null;	

	switch ( state ) {

	    // kurssilla 
	case STUDENT_ON_COURSE:
	    sstate = "'K'";
	    break;

	    // ei kurssilla (..not in..)
	case STUDENT_NOT_ON_COURSE:
	    sstate = "'K', 'P'";
	    break;

	    // poistettu kurssilta
	case STUDENT_REMOVED:           
	    sstate = "'P'";
	    break;
	default:
	    sstate = "'K'";
	}
	

	String select = "SELECT DISTINCT op.hetu,\n"
	    +"    INITCAP(op.sukunimi) AS sukunimi,\n"
	    +"    NLS_INITCAP(op.etunimi) AS etunimi,\n"
	    +"    op.personid,\n"
	    +"    op.sahkopostiosoite AS sposti"
	    +( onCourse
	       ? ",\n    os.ilmo_jnro,\n    os.ryhma_nro,\n    os.jaassa,\n"
	        +"    os.laskarisuoritukset,\n    os.harjoitustyopisteet,\n    os.koepisteet\n"
	       : "\n" )
	    +"  FROM opiskelija op"
	    +( onCourse ? ", osallistuminen os\n" : "\n" )
	    +"  WHERE ";

	String courseWhere =
	    "os.kurssikoodi = '"+courseInfo.ccode+"'\n"
	    +"      AND os.lukukausi = '"+courseInfo.term+"'\n"
	    +"      AND os.lukuvuosi = "+courseInfo.year+"\n"
	    +"      AND os.tyyppi = '"+courseInfo.type+"'\n"
	    +"      AND os.kurssi_nro = "+courseInfo.cno+"\n"
	    +"      AND os.voimassa in ("+sstate+")";

	if ( onCourse ) {
	    select += "op.hetu = os.hetu\n";

	    if ( group > 0 ) {
		select += "    AND os.ilmo_jnro = "+group+"\n";
	    }

	    select += "    AND "+courseWhere+"\n";
	}
	else {
	    select += "op.hetu NOT IN\n"
		+"    (SELECT hetu FROM osallistuminen os WHERE\n"
		+"      "+courseWhere+")\n";
	}

	if ( lname != null ) {
	    select += "    AND UPPER(sukunimi) LIKE '"
		+Course.SQLApostrophe( lname.toUpperCase() )+"'\n";
	}
	
	if ( fname != null ) {
	    select += "    AND UPPER(etunimi) LIKE '"
		+Course.SQLApostrophe( fname.toUpperCase() )+"'\n";
	}
	
	if ( idvalue != null ) {
	    if ( idtype == 0 ) {
               //hae sarakkeen hetu avulla = opnro
                select += "   AND op.hetu LIKE '"+ Course.SQLApostrophe( idvalue)+"'\n";
//
//		idvalue = Course.SQLApostrophe( Student.checkSSN( idvalue, false ) );
//		select += "    AND op.hetu LIKE '"+idvalue+"'\n";
	    }
	    else if ( idtype == 1 ) {
                // hae henkilötunnuksella
        	idvalue = Course.SQLApostrophe( Student.checkSSN( idvalue, false ) );
		select += "    AND op.personid LIKE '"+idvalue+"'\n";
	     
//		select += "    AND op.opnro LIKE '"+Course.SQLApostrophe( idvalue )+"'\n";
            }	
      }

	select += "  ORDER BY sukunimi ASC, etunimi ASC";

// 	if ( resultLimit > 0 )  {
// 	    select += "SELECT * FROM ("
// 		+select
// 		+")\nWHERE ROWNUM < "+(resultLimit+1);
// 	}

	Connection con = DBConnectionManager.createConnection();
	Statement stmt = null;
	ResultSet rs = null;
	Vector students = new Vector();

	try {
	    stmt = con.createStatement();
	    rs = stmt.executeQuery( select );
	    String[] scorecols = {"laskarisuoritukset", "harjoitustyopisteet", "koepisteet"};

	    int sid = 0;
	    while ( rs.next()
		    && (resultLimit < 0 || sid < resultLimit ) ) {
		Student s = new Student( sid,
					 rs.getString("hetu"),
					 rs.getString("sukunimi"),
					 rs.getString("etunimi"),
					 rs.getString("sposti"),
					 ( onCourse ? rs.getInt("ilmo_jnro") : Student.UNDEF_GROUP ) );

		s.setSNO( rs.getString("personid") );

		if ( onCourse ) {
		    s.setGroupID( rs.getInt("ryhma_nro") );
		    s.setState( rs.getString("jaassa") );
		    boolean hasScores = false;
		    for (int i=0; i < scorecols.length && !hasScores; i++) {
			String scores = rs.getString(scorecols[i]);
			if (scores != null &&
			    !scores.startsWith("??,??,??,??,??-??,??,??,??,??-??,??,??,??,??-??,??,??")) {
			    hasScores = true;
			}
		    }
		    s.setHasScores(hasScores);
		}
		students.add( sid, s );
		sid++;
	    }
	} finally {
	    try {
		if ( rs != null ) rs.close();
		if ( stmt != null ) stmt.close();
	    } catch ( Exception e ) {}
	    DBConnectionManager.closeConnection( con );
	}

	return students;
    }

    /**
     **   Suorittaa tarvittavat alustukset ja palauttaa uuden ilmentymän 
     **   tästä luokasta.
     */
    public static Session getInstance(String ruser)
	throws InitFailedException, SQLException, NullIdException, ClassNotFoundException  {
	return new Session( ruser );
    }


//     protected void updateValidActions () {
// 	for (int a = 0; a < validActions.length; a++) {
	    
// 	    if ( isValidAction(a) ) {
// 		validActions[ a ] = actions[ a ];
// 	    }
	    
// 	}
//     }


    /**
     **  Käsiteltävissä olevan kurssin perustiedot.
     */
    public CourseInfo getSelectedCourseInfo() throws CourseNotDefinedException {
	if ( this.selectedCourse == null ) { throw new CourseNotDefinedException(); }
	else 
	    return this.selectedCourse.getCourseInfo();
    }

    public Course getSelectedCourse() {
	return this.selectedCourse;
    }


    /**
     **  Kaikkien niiden kurssien perustiedot, joilla käyttäjä ohjaajana.
     */
    public CourseInfo[] getCourseInfos() {
	return (CourseInfo[])this.coursesList.toArray( new CourseInfo[this.coursesList.size()] );
    }


    /**
     **   Kurssin <tt>cid</tt> perustiedot.
     */
    public CourseInfo getCourseInfo( String cid ) throws CourseNotDefinedException {
	if ( cid == null ) throw new CourseNotDefinedException();
	
	return (CourseInfo)courses.get( cid );
    }


//     /**
//      **  Kurssin laskariryhmien perustiedot.
//      */
//     public Vector getGroups()  throws CourseNotDefinedException {
// 	if ( this.course == null ) { throw new CourseNotDefinedException(); }
// 	return this.course.getGroups();
//     }


    /**
     **   Käyttäjän päivitysoikeudet.
     */
    public Service[] getValidServices() throws ServicesNotDefinedException {
	if ( this.selectedCourse == null ) { return null; }

	Service[] rv = null;
	rv = this.selectedCourse.getCourseInfo().getValidServices();
	return rv;
    }


    /**
     **  Selvitetään, millä kurssilla käyttäjä on luennoitsijana,
     **  laskariohjaajana tai käytännön vastuuhenkilönä sekä
     **  mitä päivityksiä kyseinen käyttäjä saa kurssille tehdä.
     */
    protected void init()
	throws InitFailedException, SQLException, NullIdException, ClassNotFoundException {
	CourseInfo cinfo;

	Connection con = DBConnectionManager.createConnection();
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
//	System.out.println(COURSE_INFOS);
	try {
	    if ( superUsers.indexOf( LOGIN_SEPARATOR+this.ruser+LOGIN_SEPARATOR ) >= 0 ) {
		pstmt = con.prepareStatement( SUPER_INFOS );
	    } else {
		pstmt = con.prepareStatement( COURSE_INFOS );
		pstmt.setString( 1, this.ruser );
		pstmt.setString( 2, this.ruser );
	    }
            //MKCT:
            pstmt = con.prepareStatement( SUPER_INFOS );
	    rs = pstmt.executeQuery();
	
	    while ( rs.next() ) {
		int lukuvuosi = rs.getInt("lukuvuosi");
		String lukukausi = rs.getString("lukukausi");
		String tila = rs.getString("tila");
		String nameaux  = " ["+lukukausi+(""+lukuvuosi).substring(2, 4)+"]";

		cinfo = new CourseInfo( rs.getString("kurssikoodi"),
					lukuvuosi,
					lukukausi,
					rs.getString("tyyppi"),
					rs.getInt("kurssi_nro"),
					rs.getString("nimi")+nameaux,
					SUPER ); // kurssin perustiedot
                if (tila != null && tila.equals("J")) 
                    cinfo.freeze();
		this.courses.put( cinfo.getId(), cinfo );
		this.coursesList.add( cinfo );
	    }
	} finally {
	    try {
		if ( rs != null ) rs.close();
		if ( pstmt != null ) pstmt.close();
	    } catch ( Exception e ) {}
	    DBConnectionManager.closeConnection( con );
	}
    }



    /**
     **   Onko käyttäjä oikeutettu suorittamaan <tt>action</tt>-päivityksen.
     */
    public boolean isValidService( String service )
	throws CourseNotDefinedException, ServicesNotDefinedException {

	if ( this.selectedCourse == null ) { throw new CourseNotDefinedException(); }

	return this.selectedCourse.getCourseInfo().isValidService( service );
    }


    public boolean isValidService( Service service ) 
	throws CourseNotDefinedException, ServicesNotDefinedException {
	
	if ( this.selectedCourse == null ) { throw new CourseNotDefinedException(); }

	return this.selectedCourse.getCourseInfo().isValidService( service );
    }


    /**
     **  Valitaan kurssi <tt>cinfo</tt> käsittelyyn.
    */
    public boolean setSelectedCourse( CourseInfo cinfo )
	throws SQLException, NullIdException, ClassNotFoundException, NullPointerException {
	try {
	    this.selectedCourse = new Course( (CourseInfo)courses.get( cinfo.getId() ) );
	} catch ( NullPointerException npe ) {
	    npe.printStackTrace();
	    this.selectedCourse = null;
	    return false;
	}
	return true;
    }


    /**
     **  Valitaan kurssi <tt>cid</tt> käsittelyyn.
     */
    public boolean setSelectedCourse( String cid )
	throws SQLException, NullIdException, ClassNotFoundException, NullPointerException {
	try {
	    return setSelectedCourse(new CourseInfo(cid, null, -1) );
	} catch ( NullIdException nie ) {
	    return false;
	}
    }


    public Service getSelectedService() {
	return this.selectedService;
    }

    public static ServiceManager getServiceManager() {
	return serviceManager;
    }

    public boolean setService( String serviceId ) {
	Service service = serviceManager.getService( serviceId );

	if ( service != null &&
	     courseSelected() &&
	     service.isValidServiceFor( this.selectedCourse.getUserRole() ) ) {

	    this.selectedService = service;
	    return true;	
	}
	else {
	    this.selectedService = null;
	    return false;
	}
    }

    public boolean serviceSelected() { return this.selectedService != null; }

    public String toString() {
	return
	    "\nKäsitelävä kurssi: "+selectedCourse;
    }
}

