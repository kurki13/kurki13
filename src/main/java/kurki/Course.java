package kurki;

import kurki.util.LocalisationBundle;
import kurki.exception.NullParameterException;
import service.exception.NullIdException;

import java.util.*;
import java.sql.*;
import java.io.Serializable;
import kurki.util.DateParser;
import service.Option;

public class Course implements Serializable, Option {
    
    public static final String STR_TABLE_COL_UNDEF = "??";
    public static final int STR_TABLE_COL_LENGTH = STR_TABLE_COL_UNDEF.length();
    protected static final String ADD_ON_COURSE_SQL =
            "{ ? = call kurkiilmo (?, ?, ?, ?, ?, ?, ?) }";
    protected static final String COURSE_WHERE_SQL = //<editor-fold defaultstate="collapsed" desc="courseWhereSQL">
            
            "    kurssikoodi = ?\n"
            + "    AND lukukausi = ?\n"
            + "    AND lukuvuosi = ?\n"
            + "    AND tyyppi = ?\n"
            + "    AND kurssi_nro = ?\n";
            //</editor-fold>
    protected static final String DEFREEZE = //<editor-fold defaultstate="collapsed" desc="defreezeSQL">
            
            "UPDATE osallistuminen SET jaassa = 'S'\n"
            + "  WHERE \n"
            + COURSE_WHERE_SQL
            + "    AND jaassa = 'J'\n"
            + "    AND hetu = ?\n";
            //</editor-fold>
    public static final int MAX_GROUP = 99;
    protected static final int DEFAULT_CONVENTION = 1;
    protected static final String LOAD_COURSE_SQL =
            "SELECT * FROM kurssi WHERE\n"
            + COURSE_WHERE_SQL;
    protected static final String LOAD_GROUPS_SQL =
            "SELECT ilmo_jnro FROM opetus WHERE\n"
            + COURSE_WHERE_SQL;
    protected static final String[] ORDER_BY = {"sukunimi ASC, etunimi ASC",
        "ilmoittautumis_pvm ASC",
        "ilmo_jnro ASC, sukunimi ASC, etunimi ASC",
        "hetu ASC"};
    protected static final String PRECONDITION =
            " and kurssikoodi in (select kurssikoodi from esitiedot) "
            + " and (hetu, kurssikoodi) not in "
            + " (select (hetu,kurssikoodi) from ilmo_oikeus) ";
    public static final int ORDER_BY_NAME = 0;
    public static final int ORDER_BY_ENROLMENT_TIME = 1;
    public static final int ORDER_BY_GROUP_AND_NAME = 2;
    public static final int ORDER_BY_STNUMBER = 3;
    public static final String RETURN_ON_COURSE_SQL = //<editor-fold defaultstate="collapsed" desc="returnOnCourseSQL">
            "UPDATE osallistuminen\n"
            + "  SET voimassa = 'K'\n"
            + "  WHERE\n"
            + COURSE_WHERE_SQL
            + "    AND voimassa = 'P'\n"
            + "    AND hetu = ?";
            //</editor-fold>
    public static final String[] SET_MINMAX_SCORE_SQL = //<editor-fold defaultstate="collapsed" desc="setMinMaxSQL">
            
{"UPDATE kurssi\n"
        + "  SET laskaritehtava_lkm = \n"
        + "      SUBSTR(laskaritehtava_lkm, 0,  " + (STR_TABLE_COL_LENGTH + 1) + "*?)\n"
        + "      || ? ||\n"
        + "      SUBSTR(laskaritehtava_lkm, " + (STR_TABLE_COL_LENGTH + 1) + "+" + (STR_TABLE_COL_LENGTH + 1) + "*?),\n"
        + "    hyvaksytty_laskarilasnaolo = \n"
        + "      SUBSTR(hyvaksytty_laskarilasnaolo, 0,  " + (STR_TABLE_COL_LENGTH + 1) + "*?)\n"
        + "      || ? ||\n"
        + "      SUBSTR(hyvaksytty_laskarilasnaolo, " + (STR_TABLE_COL_LENGTH + 1) + "+" + (STR_TABLE_COL_LENGTH + 1) + "*?)\n"
        + "  WHERE tila != 'J' AND\n"
        + COURSE_WHERE_SQL,
        "UPDATE kurssi\n"
        + "  SET max_harjoitustyopisteet = \n"
        + "      SUBSTR(max_harjoitustyopisteet, 0,  " + (STR_TABLE_COL_LENGTH + 1) + "*?)\n"
        + "      || ? ||\n"
        + "      SUBSTR(max_harjoitustyopisteet, " + (STR_TABLE_COL_LENGTH + 1) + "+" + (STR_TABLE_COL_LENGTH + 1) + "*?),\n"
        + "    min_harjoitustyopisteet = \n"
        + "      SUBSTR(min_harjoitustyopisteet, 0,  " + (STR_TABLE_COL_LENGTH + 1) + "*?)\n"
        + "      || ? ||\n"
        + "      SUBSTR(min_harjoitustyopisteet, " + (STR_TABLE_COL_LENGTH + 1) + "+" + (STR_TABLE_COL_LENGTH + 1) + "*?)\n"
        + "  WHERE tila != 'J' AND\n"
        + COURSE_WHERE_SQL,
        "UPDATE kurssi\n"
        + "  SET max_koepisteet = \n"
        + "      SUBSTR(max_koepisteet, 0,  " + (STR_TABLE_COL_LENGTH + 1) + "*?)\n"
        + "      || ? ||\n"
        + "      SUBSTR(max_koepisteet, " + (STR_TABLE_COL_LENGTH + 1) + "+" + (STR_TABLE_COL_LENGTH + 1) + "*?),\n"
        + "    min_koepisteet = \n"
        + "      SUBSTR(min_koepisteet, 0,  " + (STR_TABLE_COL_LENGTH + 1) + "*?)\n"
        + "      || ? ||\n"
        + "      SUBSTR(min_koepisteet, " + (STR_TABLE_COL_LENGTH + 1) + "+" + (STR_TABLE_COL_LENGTH + 1) + "*?)\n"
        + "  WHERE tila != 'J' AND\n"
        + COURSE_WHERE_SQL};
            //</editor-fold>
    protected static final String[] SCORE_UPDATE_SQL = //<editor-fold defaultstate="collapsed" desc="scoreUpdateSQL">
            {"UPDATE osallistuminen \n"
        + "  SET laskarisuoritukset = \n"
        // ? = suorituskerta
        + "    SUBSTR(laskarisuoritukset, 0,  " + (STR_TABLE_COL_LENGTH + 1) + "*?)\n"
        // ? = pisteet 
        + "      || ? ||\n"
        // ? = suorituskerta
        + "      SUBSTR(laskarisuoritukset, " + (STR_TABLE_COL_LENGTH + 1) + "+" + (STR_TABLE_COL_LENGTH + 1) + "*?)\n"
        + "  WHERE\n"
        + COURSE_WHERE_SQL
        + "    AND voimassa = 'K'\n"
        + "    AND (jaassa != 'J' OR jaassa IS null)\n"
        + "    AND hetu = ?",
        "UPDATE osallistuminen \n"
        + "  SET harjoitustyopisteet = \n"
        + "    SUBSTR(harjoitustyopisteet, 0,  " + (STR_TABLE_COL_LENGTH + 1) + "*?)\n"
        + "      || ? ||\n"
        + "      SUBSTR(harjoitustyopisteet, " + (STR_TABLE_COL_LENGTH + 1) + "+" + (STR_TABLE_COL_LENGTH + 1) + "*?)\n"
        + "  WHERE\n"
        + COURSE_WHERE_SQL
        + "    AND voimassa = 'K'\n"
        + "    AND (jaassa != 'J' OR jaassa IS null)\n"
        + "    AND hetu = ?",
        "UPDATE osallistuminen \n"
        + "  SET koepisteet = \n"
        + "    SUBSTR(koepisteet, 0,  " + (STR_TABLE_COL_LENGTH + 1) + "*?)\n"
        + "      || ? ||\n"
        + "      SUBSTR(koepisteet, " + (STR_TABLE_COL_LENGTH + 1) + "+" + (STR_TABLE_COL_LENGTH + 1) + "*?)\n"
        + "  WHERE\n"
        + COURSE_WHERE_SQL
        + "    AND voimassa = 'K'\n"
        + "    AND (jaassa != 'J' OR jaassa IS null)\n"
        + "    AND hetu = ?",
        "UPDATE osallistuminen \n"
        + "  SET arvosana = ?,\n"
        + "    tenttija = tenttija ||?,\n"
        + "    kypsyys_pvm = SYSDATE,\n"
        + "    kommentti_2 = ?\n"
        + "  WHERE\n"
        + COURSE_WHERE_SQL
        + "    AND voimassa = 'K'\n"
        + "    AND (jaassa != 'J' OR jaassa IS null)\n"
        + "    AND hetu = ?",
        "UPDATE  osallistuminen \n"
        + "  SET laajuus_op = ?, \n"
        + "  tenttija =tenttija ||';l:'|| ?, \n"
        + "  kommentti_1= kommentti_1 ||?\n"
        + "  WHERE\n"
        + COURSE_WHERE_SQL
        + "    AND voimassa = 'K'\n"
        + "    AND (jaassa != 'J' OR jaassa IS null)\n"
        + "    AND hetu = ?",
        "UPDATE  osallistuminen \n"
        + "  SET kielikoodi = ?, \n"
        + "  tenttija = tenttija ||';k:'|| ?, \n"
        + "  kommentti_1= kommentti_1 ||?\n"
        + "  WHERE\n"
        + COURSE_WHERE_SQL
        + "    AND voimassa = 'K'\n"
        + "    AND (jaassa != 'J' OR jaassa IS null)\n"
        + "    AND hetu = ?"};
    protected final String SET_PART_DEF_SQL = "UPDATE kurssi\n"
            + "  SET laskarikerta_lkm = ?,\n"
            + "    pakolliset_laskarikerta_lkm  = ?,\n"
            + "    max_laskaripisteet = ?,\n"
            + "    harjoitustyo_lkm = ?,\n"
            + "    pakolliset_harjoitustyo_lkm = ?,\n"
            + "    harjoitustyopisteet = ?,\n"
            + "    valikokeet_lkm = ?,\n"
            + "    pakolliset_koe_lkm = ?\n"
            + "  WHERE tila != 'J' AND\n"
            + COURSE_WHERE_SQL;
    //</editor-fold>
    
    protected CourseInfo courseInfo;    
    protected String don = null;
    protected String donFName = null;
    protected String donLName = null;
    protected Calendar endDate = null;
    protected Calendar examDate = null;
    protected boolean examDateMod = false;
    protected boolean[] isNull = {false, false, false};
    protected int convention = DEFAULT_CONVENTION;
    protected boolean conventionMod = false;
    protected Vector conventions = new Vector();       {
    //<editor-fold defaultstate="collapsed" desc="Conventions">
 
        conventions.add(new Convention(1,
                "Normaali",
                "koe + laskarihyvitys+ harjoitustyöhyvitys "
                + "(kaikki osat vaikuttavat)."));
        conventions.add(new Convention(2,
                "Parempi ht korvaa laskarit",
                "koe + greatest(laskarihyvitys,harjoitustyöhyvitys) "
                + "(laskarit voi korvata harjoitustöillä - "
                + "parempi valitaan)."));
        conventions.add(new Convention(3,
                "Tehty ht korvaa laskarit",
                "koe + if exists harjoitustyohyvitys then "
                + "harjoitustyöhyvitys else laskarihyvitys "
                + "(laskarit voi korvata harjoitustöillä, "
                + "jos harjoitustöitä on tehty ne ajavat laskareiden "
                + "yli)."));
        conventions.add(new Convention(4,
                "Parempi lisäkoe korvaa laskarit",
                "koe(1..n-1) + greatest(laskarihyvitys, "
                + "koe(n)) laskarit voi korvata ylimääräisellä "
                + "koetehtävällä, joka kirjataan omaksi kokeekseen "
                + "- parempi valitaan."));
        conventions.add(new Convention(5,
                "Tehty lisäkoe korvaa laskarit",
                "koe(1..n-1) + if exists koe(n) then koe(n) "
                + "else laskarihyvitys laskarit voi korvata "
                + "ylimääräisellä koetehtävällä, joka kirjataan "
                + "omaksi kokeekseen - ylimääräinen tehtävä "
                + "ajaa laskareiden yli"));
        conventions.add(new Convention(6,
                "Parempi ht korvaa muut",
                "greatest(koe+laskarihyvitys,harjoitustyöhyvitys) "
                + "kokeen ja laskarit voi korvata harjoitustöillä."));
        conventions.add(new Convention(7,
                "Tehty ht korvaa muut",
                "if exists harjoitustyohyvitys then "
                + "harjoitustyohyvitys else koe+laskarihyvitys."));
        conventions.add(new Convention(8,
                "Ylösskaalattu",
                "greatest(koe+laskarihyvitys+harjoitustyöhyvitys, "
                + "koe*maksimipistemäärä/koemaksimi) "
                + "parempi vaihtoehdoista koe+laskarit+ht ja pelkkä koe "
                + "skaalattuna koe+laskarit+ht maksimipisteisiin."));
        conventions.add(new Convention(9,
                "Alaskaalattu",
                "greatest(koe, alasskaalattu(koe)+laskarihyvitys+"
                + "harjoitushyvitys) alasskaalattu(koe)= "
                + "(sairauspoissaolojen vanha kaava)."));
        
        

    //</editor-fold>
    }
    protected int credits = 1;
    protected int creditsMax = 1;         //new 05/08
    protected int newCredits = 1;         //new 05/08
    protected int newCreditsMax = 1;       //new 05/08
    /**
     * Groups to be searched for.
     */
    protected LinkedList group = new LinkedList();
    protected LinkedList groups = new LinkedList();
    protected LinkedList lname = new LinkedList();
    protected LinkedList ssn = new LinkedList();
    protected LinkedList sids = new LinkedList();
    protected String lnameFrom = null;
    protected String lnameTo = null;
    protected String lastStudentFilter = null;
    protected int modifyCount = 0;
    protected String msg = null;
    protected int orderBy = ORDER_BY_NAME;
    protected Vector parts = null;
    protected String scale = "K"; // K = 1-5, E = hyväksytty/hylätty
    protected boolean scaleMod = false;
    protected String specialCondition = null;  // HL.lis 28.8.08
    /**
     ** Tällä hetkellä käsittelyssä oleva osasuoritus.
     */
    protected Part selectedPart = null;
    protected Vector students = null;

    /**
     * Analysoi kurssin perustiedot.
     */
    public Course(CourseInfo courseInfo)
            throws SQLException, NullIdException, ClassNotFoundException {
        
        if (courseInfo == null) {
            throw new NullIdException();
        }
        
        this.courseInfo = courseInfo;
        
        load();
    }
    
    public synchronized boolean addOnCourse(String ssn, int group)
            throws SQLException, ClassNotFoundException {
        if (ssn == null) {
            this.msg = LocalisationBundle.getString("tuntemOpisk");
            return false;
        }
        
        Connection con = DBConnectionManager.createConnection();
        CallableStatement stm = null;
        
        try {
            stm = con.prepareCall(ADD_ON_COURSE_SQL);
            stm.registerOutParameter(1, java.sql.Types.VARCHAR);
            
            stm.setString(2, this.courseInfo.ccode);
            stm.setString(3, this.courseInfo.term);
            stm.setInt(4, this.courseInfo.year);
            stm.setString(5, this.courseInfo.type);
            stm.setInt(6, this.courseInfo.cno);
            stm.setInt(7, group);
            stm.setString(8, ssn);
            stm.executeUpdate();
            String rv = stm.getString(1);

// 	    System.out.println(rv);
            if (!rv.equals("ok")) {
                if (rv.indexOf("ORA-01400") >= 0 && rv.indexOf("RYHMA_NRO") >= 0) {
                    this.msg = LocalisationBundle.getString("ryhmaa") + group + LocalisationBundle.getString("eioleolem");
                } else {
                    this.msg = rv;
                }
                return false;
            } else {
                this.msg = null;
                return true;
            }
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                }
            }
            DBConnectionManager.closeConnection(con);
        }
    }
    
    public synchronized boolean commitCourseInfo()
            throws SQLException, ClassNotFoundException {
        if (isFrozen()) {
            this.msg = LocalisationBundle.getString("kurssiJaadytetty");
            return false;
        }
        
        boolean rv = true;
        String set = "";
        
        final String SEPARATOR = ",\n\t";
        
        final String[][] partSQLnames = {{"laskarikerta_lkm", "pakolliset_laskarikerta_lkm", "max_laskaripisteet"},
            {"harjoitustyo_lkm", "pakolliset_harjoitustyo_lkm", "harjoitustyopisteet"},
            {"valikokeet_lkm", "pakolliset_koe_lkm", null}};
        
        final String[][] minmaxSQLnames = {{"laskaritehtava_lkm", "hyvaksytty_laskarilasnaolo"},
            {"max_harjoitustyopisteet", "min_harjoitustyopisteet"},
            {"max_koepisteet", "min_koepisteet"}};
        
        Connection con = DBConnectionManager.createConnection();
        Statement stmt = null;        
        try {
            
            for (int p = 0; p < this.parts.size(); p++) {
                Part part = (Part) this.parts.get(p);
                int ptype = part.getType();
                Offering[] offerings = part.getOfferings();

                // ...jotta voidaan generoida String-taulukko, joka muuttaa vain
                // niitä osia, mitkä on muuttunut.
                LinkedList offeringUpdate = new LinkedList();
                
                if (part.partDefMod() && ptype < Part.ARVOSANA) {
                    set += SEPARATOR + partSQLnames[ptype][0] + "=" + part.getNbrOfOfferings();
                    set += SEPARATOR + partSQLnames[ptype][1] + "=" + part.getRequiredOfferings();
                    
                    if (ptype != Part.KOE) {
                        set += SEPARATOR + partSQLnames[ptype][2] + "=" + part.getXtrScore();
                    }

                    // Alusta tyhjät min/max-kentät
                    if (p < isNull.length && isNull[p] && part.getNbrOfOfferings() > 0) {
                        stmt = con.createStatement();
// 			System.out.println("Alustetaan tyhjät min/max-kentät...");
                        stmt.executeUpdate("UPDATE kurssi SET \n\t"
                                + minmaxSQLnames[p][0] + "='??,??,??,??,??-??,??,??,??,??-??,??,??,??,??-??,??,??',\n\t"
                                + minmaxSQLnames[p][1] + "='??,??,??,??,??-??,??,??,??,??-??,??,??,??,??-??,??,??'"
                                + "\nWHERE"
                                + "\tkurssikoodi = '" + this.courseInfo.ccode + "'\n"
                                + "\tAND lukukausi = '" + this.courseInfo.term + "'\n"
                                + "\tAND lukuvuosi = " + this.courseInfo.year + "\n"
                                + "\tAND tyyppi = '" + this.courseInfo.type + "'\n"
                                + "\tAND kurssi_nro = " + this.courseInfo.cno);
                    }
                    
                }

                // Tämän voisi hoitaa tehokkaamminkin --> keräämällä listaa 
                // muutoksista suoraan silloin kun muutokset tehdään
                for (int o = 0; o < offerings.length; o++) {
                    if (offerings[o].scoreDefMod() && ptype < Part.ARVOSANA) {
                        offeringUpdate.add(offerings[o]);
                    }
                }
                
                String max = "";
                String min = "";
                int maxPrev = 1;
                int minPrev = 1;
                
                Iterator itr = offeringUpdate.iterator();
                while (itr.hasNext()) {
                    Offering offering = (Offering) itr.next();
                    int start = (STR_TABLE_COL_LENGTH + 1) * offering.getId() + 1;
                    int len = start - maxPrev;

                    // maksimipisteet
                    max += (start == 1 ? ""
                            : "|| RPAD(NVL(SUBSTR(" + minmaxSQLnames[ptype][0] + ", "
                            + maxPrev + ", " + (len) + "), ' '), " + (len) + ")\n")
                            + "|| '" + getStrTableCell("" + offering.getMaxScore(), "00") + "'\n";


                    // hyväksymisraja
                    min += (start == 1 ? ""
                            : "|| RPAD(NVL(SUBSTR(" + minmaxSQLnames[ptype][1] + ", "
                            + minPrev + ", " + (len) + "), ' '), " + (len) + ")\n")
                            + "|| '" + getStrTableCell("" + offering.getMinScore(), "00") + "'\n";
                    
                    maxPrev = minPrev = start + STR_TABLE_COL_LENGTH;
                }
                if (maxPrev > 1) {
                    set += SEPARATOR
                            + minmaxSQLnames[ptype][0] + " = \n"
                            + max.substring(3, max.length())
                            + "|| SUBSTR(" + minmaxSQLnames[ptype][0] + ", " + maxPrev + ")";
                    
                    set += SEPARATOR
                            + minmaxSQLnames[ptype][1] + " = \n"
                            + min.substring(3, min.length())
                            + "|| SUBSTR(" + minmaxSQLnames[ptype][1] + ", " + minPrev + ")";
                }
                
                if (part.getNbrOfOfferings() == 0) {
                    this.parts.remove(p);
                    p--;
                }
            }
            if (set.length() > SEPARATOR.length()) {
                set = set.substring(SEPARATOR.length(), set.length());
                
                String sql = "UPDATE kurssi SET\n\t" + set + "\nWHERE"
                        + "\tkurssikoodi = '" + this.courseInfo.ccode + "'\n"
                        + "\tAND lukukausi = '" + this.courseInfo.term + "'\n"
                        + "\tAND lukuvuosi = " + this.courseInfo.year + "\n"
                        + "\tAND tyyppi = '" + this.courseInfo.type + "'\n"
                        + "\tAND kurssi_nro = " + this.courseInfo.cno;

// 		System.out.println(sql);
                stmt = con.createStatement();
                if (stmt.executeUpdate(sql) != 1) {
                    rv = false;
                } else {
                    rv = true;
                }
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            DBConnectionManager.closeConnection(con);
        }
        
        return rv;
    }
    
    public synchronized boolean commitGradeDef()
            throws SQLException, ClassNotFoundException {
        if (isFrozen()) {
            this.msg = LocalisationBundle.getString("kurssiJaadytetty");
            return false;
        }
        
        boolean rv = true;
        String set = null;
        Connection con = DBConnectionManager.createConnection();
        PreparedStatement pstmt = null;
        
        final String[] scoreBoundarySQLnames = {"lisapisterajat", "harjoitustyon_pisterajat", null, "arvosanarajat", null, null};
        
        final String[][] partSQLnames = // laskarit
                {{"pakolliset_laskarikerta_lkm", "pakolliset_laskaritehtava_lkm",
                "max_laskaripisteet", "lisapistealaraja", "lisapisteiden_askelkoko"},
            // harjoitustyöt
            {"pakolliset_harjoitustyo_lkm", "min_harjoitustyopisteet_summa",
                "harjoitustyopisteet", "ht_lisapistealaraja", "harjoitustoiden_askelkoko"},
            // kokeet
            {"pakolliset_koe_lkm", "min_koepisteet_summa", null, null, null},
            // yhteispisteet / arvosana
            {null, null, null, "min_yhteispisteet", "arvostelun_askelkoko"}};
        
        try {
            if (this.examDateMod && this.examDate != null) {
                if (set == null) {
                    set = "";
                } else {
                    set += ",\n";
                }
                set = "suoritus_pvm = TO_DATE('" + getExamDate() + "', 'DD.MM.YYYY')";
            }
            if (this.scaleMod) {
                if (set == null) {
                    set = "";
                } else {
                    set += ",\n";
                }
                set += "arvostellaanko = '" + this.scale + "'";
            }
            if (this.conventionMod) {
                if (set == null) {
                    set = "";
                } else {
                    set += ",\n";
                }
                set += "laskentakaava = " + this.convention;
            }
            
            for (int p = 0; p < this.parts.size() - 2; p++) {
                Part part = (Part) this.parts.get(p);
                int ptype = part.getType();
                
                if (part.partDefMod()) {
                    for (int i = 0; i < partSQLnames[ptype].length; i++) {
                        
                        if (partSQLnames[ptype][i] != null) {
                            if (set == null) {
                                set = "";
                            } else {
                                set += ",\n";
                            }
                            
                            int vali = -1;
                            double vald = -1;
                            
                            switch (i) {
                                case 0:
                                    vali = part.getRequiredOfferings();
                                    break;
                                case 1:
                                    vali = part.getRequiredScore();
                                    break;
                                case 2:
                                    vali = part.getXtrScore();
                                    break;
                                case 3:
                                    vali = part.getFirstXtrScore();
                                    break;
                                case 4:
                                    vald = part.getXtrStep();
                                    break;
                            }
                            
                            if (vali >= 0 | vald >= 0) {
                                set += partSQLnames[ptype][i] + " = "
                                        + (vali >= 0 ? "" + vali : "" + vald);
                            }
                        }
                    }
                }

                // Lisäpiste- ja arvosanarajat
                if ((ptype == Part.ARVOSANA || ptype == Part.LASKARI
                        || ptype == Part.HARJOITUSTYO) && part.scoreBoundariesMod()) {
                    String strTable = getStrTable(part.getScoreBoundaries(), "   ");
                    
                    if (strTable != null) {
                        if (set == null) {
                            set = "";
                        } else {
                            set += ",\n";
                        }
                        
                        set += scoreBoundarySQLnames[ptype] + " = '" + strTable + "'";
                    }
                }
            }
            
            if (set != null) {
                String sql = "UPDATE kurssi SET \n"
                        + set
                        + "\nWHERE " + COURSE_WHERE_SQL;

                // 	    System.out.println(sql);

                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, this.courseInfo.ccode);
                pstmt.setString(2, this.courseInfo.term);
                pstmt.setInt(3, this.courseInfo.year);
                pstmt.setString(4, this.courseInfo.type);
                pstmt.setInt(5, this.courseInfo.cno);
                
                if (pstmt.executeUpdate() < 1) {
                    this.msg = LocalisationBundle.getString("kurssiaEiLoydy");
                    rv = false;
                }
            }
            
            if (rv) {
                this.examDateMod = false;
                this.scaleMod = false;
                this.conventionMod = false;
            }
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                }
            }
            DBConnectionManager.closeConnection(con);
        }
        return rv;
    }
    
    public synchronized boolean commitScores(String ruser)
            throws SQLException, ClassNotFoundException {
        
        if (students == null) {
            return false;
        }
        
        int lastPType = -1;
        boolean rv = true;
        Student s = null;
        this.modifyCount = 0;
        Connection con = DBConnectionManager.createConnection();
        PreparedStatement pstmt = null;
        
        try {
            for (int i = 0; i < students.size(); i++) {
                s = (Student) students.get(i);
                
                Iterator su = s.getScoreUpdate();
                Score score = null;
                
                while (su.hasNext()) {
                    score = (Score) su.next();
                    
                    int ptype = score.getPart().getId();
                    int oid = score.getOffering().getId();
                    
                    if (ptype != lastPType || pstmt == null) {
                        lastPType = ptype;
                        pstmt = con.prepareStatement(SCORE_UPDATE_SQL[ptype]);
                        
                        pstmt.setString(4, this.courseInfo.ccode);
                        pstmt.setString(5, this.courseInfo.term);
                        pstmt.setInt(6, this.courseInfo.year);
                        pstmt.setString(7, this.courseInfo.type);
                        pstmt.setInt(8, this.courseInfo.cno);
                    }
                    
                    pstmt.setString(9, s.getSSNID());
                    
                    if (ptype < Part.ARVOSANA) {
                        pstmt.setInt(1, oid);
                        pstmt.setString(2, getStrTableCell(score.getScore()));
                        pstmt.setInt(3, oid);
                    } else {
                        String grade = score.getScore();
                        if (grade == null) {
                            grade = "";
                            pstmt.setNull(3, java.sql.Types.VARCHAR);
                        } else {
                            if (ptype == Part.ARVOSANA) {
                                pstmt.setString(3, "MANUAL");
                            } else {
                                pstmt.setString(3, " M:" + ptype);
                            }
                        }
                        pstmt.setString(1, grade);
                        pstmt.setString(2, ruser);
                    }
          /////// LOCALBUNDLE TEHTY          
                    if (pstmt.executeUpdate() < 1) {
                        this.msg = "Opiskelijan " + s.getLabel() + " pisteiden tallennus ei onnistunut.\n"
                                + "Joko hänen ilmoittautumisensa on peruuntunut tai kurssi on jäädytetty.";
                        rv = false;
                    } else {
                        this.modifyCount++;
                    }
                }
            }
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                }
            }
            DBConnectionManager.closeConnection(con);
        }
        return rv;
    }
    
    public synchronized boolean chgrpStudent(Student student, int newGroup)
            throws SQLException, ClassNotFoundException {
        if (student == null) {
            return false;
        }
        
        boolean rv = true;
        int oldGroup = student.getGroup();
        
        if (oldGroup == newGroup) {
            return true;
        }
        
        Connection con = DBConnectionManager.createConnection();
        CallableStatement stm = null;
        
        try {
            stm = con.prepareCall("{ ? = call ryhmavaihto (?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            stm.registerOutParameter(1, java.sql.Types.INTEGER);
            
            stm.setInt(2, newGroup);
            stm.setString(3, this.courseInfo.ccode);
            stm.setInt(4, this.courseInfo.year);
            stm.setString(5, this.courseInfo.term);
            stm.setString(6, this.courseInfo.type);
            stm.setInt(7, this.courseInfo.cno);
            stm.setInt(8, student.getGroupID());
            stm.setString(9, student.getSSNID());
            stm.setString(10, "KurKi");
            
            stm.executeUpdate();
            int result = stm.getInt(1);
            if (result >= 0) {
                student.setGroup(newGroup);
                student.setGroupID(result);
                this.msg = null;
                rv = true;
            } else {
                switch (result) {
                    case -1:
                        this.msg = "Ryhmää " + newGroup + " ei ole määritelty.";
                        break;
                    case -2:
                        this.msg = "Opiskelijan voimassaolevaa ilmoittautumista ei löytynyt.";
                        break;
                    case -3:
                        this.msg = "Ryhmän opiskelijalaskurin vähentäminen epäonnistui.";
                        break;
                    case -4:
                        this.msg = "Ryhmän opiskelijalaskurin kasvattaminen epäonnistui.";
                        break;
                    case -5:
                        this.msg = "Ryhmän vaihdon vahvistaminen epäonnistui.";
                        break;
                }
                rv = false;
            }
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                }
            }
            DBConnectionManager.closeConnection(con);
        }
        return rv;
    }
    
    public synchronized boolean defreezeStudent(Student student)
            throws SQLException, ClassNotFoundException {
        if (student == null) {
            this.msg = "Tuntematon opiskelija.";
            return false;
        }
        
        boolean rv = true;
        Connection con = DBConnectionManager.createConnection();
        PreparedStatement pstmt = null;
        
        try {
            pstmt = con.prepareStatement(DEFREEZE);
            
            pstmt.setString(1, this.courseInfo.ccode);
            pstmt.setString(2, this.courseInfo.term);
            pstmt.setInt(3, this.courseInfo.year);
            pstmt.setString(4, this.courseInfo.type);
            pstmt.setInt(5, this.courseInfo.cno);
            pstmt.setString(6, student.getSSNID());
            
            if (pstmt.executeUpdate() == 1) {
                student.defreeze();
                rv = true;
                this.msg = null;
            } else {
                rv = false;
                this.msg = "Opiskelijan sulattaminen ei onnistunut.";
            }
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                }
            }
            DBConnectionManager.closeConnection(con);
        }
        return rv;
    }
    
    public void findByGroup(int gno) {
        this.group.add(new Integer(gno));
    }
    
    public void findByLastName(String lname) {
        this.lname.add(lname);
    }
    
    public synchronized void findByLastNameRange(String from, String to) {        
        this.lnameFrom = from;
        this.lnameTo = to;
    }

// vahha hetuhaku , poistettu 08/7 HL    
// public void findBySSN( String ssn ) {
    public void findByStudentID(String sid) {
        if (sid != null) {
            // otetaan väliviiva pois
            sid = Student.checkSSN(sid, false);
            this.sids.add(sid);
        }
    }

    public void findBySSN(String ssn) {
        this.ssn.add(ssn);
    }
    
    public void findBySpecialCond(String cn) {
        this.specialCondition = cn;
    }    
    
    public synchronized boolean freeze(int group)
            throws SQLException, ClassNotFoundException {
// 	if ( isFrozen() ) {
// 	    this.msg = "Kurssi on jäädytetty.";
// 	    return false;
// 	}
        if (this.examDate == null) {
            this.msg = "Jäädytystä ei ole mahdollista suorittaa. "
                    + "Anna suorituspäivä.";
            return false;
        }
        if (this.getDonName().equals("TUNTEMATON")) {
            this.msg = "Jäädytystä ei voi suorittaa koska vastuuhenkilöä "
                    + "ei ole kunnolla määritelty. Ota yhteyttä opetushallintoon!";
            return false;
        } // Onko suorituspäivämäärä < jäädytyspäivämäärää (sysdate)
        else if (this.examDate.after(Calendar.getInstance())) {
            this.msg = "Jäädytystä ei voi suorittaan ennen kurssin päättymistä. "
                    + "Tarkista kurssin suorituspäivämäärä.";
            return false;
        }
        
        boolean rv = true;
        
        Connection con = DBConnectionManager.createConnection();
        CallableStatement stm = null;
        
        try {
            stm = con.prepareCall("{ ? = call jaadytys05 (?, ?, ?, ?, ?, ?) }");
            
            stm.registerOutParameter(1, java.sql.Types.VARCHAR);
            
            stm.setString(2, this.courseInfo.ccode);
            stm.setInt(3, this.courseInfo.year);
            stm.setString(4, this.courseInfo.term);
            stm.setInt(5, this.courseInfo.cno);
            stm.setString(6, this.courseInfo.type);
            stm.setInt(7, group);
            
            stm.executeUpdate();
            
            String result = stm.getString(1);
            
            if (!result.equals("OK")) {
                this.msg = result;
                rv = false;
            } else {
                if (group == 0) {
                    this.courseInfo.freeze();
                }
                // 	    else if ( group > 0 && group < MAX_GROUP ) {
                // 		this.frozenGrp[ group - 1 ] = true;
                // 	    }
                rv = true;
            }
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                }
            }
            DBConnectionManager.closeConnection(con);
        }
        
        return rv;
    }
    
    public int getCNO() {
        return this.courseInfo.cno;
    }

    public MultiValueCounter getCounter() {
        return new MultiValueCounter();
    }
    
    public float getCredits() {
        return this.credits;
    }

    public float getCreditsMax() {
        return this.creditsMax;
    }

    public float getNewCreditsMax() {
        return this.newCreditsMax;
    }

    public float getNewCredits() {
        return this.newCredits;
    }
    
    public String getDonName()
            throws SQLException, ClassNotFoundException {
        if (donFName == null && donLName == null && this.don != null) {
            String sql =
                    "SELECT sukunimi, kutsumanimi\n"
                    + "  FROM henkilo\n"
                    + "  WHERE htunnus = '" + this.don + "'";
            
            Connection con = DBConnectionManager.createConnection();
            Statement stmt = null;
            ResultSet rs = null;
            try {
                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                
                if (rs.next()) {
                    this.donLName = rs.getString("sukunimi");
                    this.donFName = rs.getString("kutsumanimi");
                }
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (Exception e) {
                }
                DBConnectionManager.closeConnection(con);
            }
        }
        if (this.donLName == null && this.donFName == null) {
            return "TUNTEMATON";
        } else {
            if (this.donLName == null) {
                this.donLName = "";
            }
            if (this.donFName == null) {
                this.donFName = "";
            }
            
            return this.donLName + ", " + this.donFName;
        }
    }
    
    public String getDonFName()
            throws SQLException, ClassNotFoundException {
        if (this.donFName == null) {
            getDonName();
        }
        return this.donFName;
    }
    
    public String getDonLName()
            throws SQLException, ClassNotFoundException {
        if (this.donLName == null) {
            getDonName();
        }
        return this.donLName;
    }
    
    public int getYear() {
        return this.courseInfo.year;
    }

    public String getCCode() {
        return this.courseInfo.ccode;
    }

    public String getTerm() {
        return this.courseInfo.term;
    }

    public String getType() {
        return this.courseInfo.type;
    }
    
    public String getExamDate() {
        String rv = null;
        
        if (this.examDate != null) {
            rv = this.examDate.get(Calendar.DATE) + "."
                    + (this.examDate.get(Calendar.MONTH) + 1) + "."
                    + this.examDate.get(Calendar.YEAR);
        }
        
        return rv;
    }
    
    public synchronized int getGradingConvention() {
        return this.convention;
    }
    
    public synchronized Vector getGradingConventions() {
        return this.conventions;        
    }
    
    public synchronized Offering getOffering() {
        
        if (this.selectedPart != null) {
            return this.selectedPart.getSelectedOffering();
        } else {
            return null;
        }
    }
    
    public synchronized Offering getOffering(int offering) {
        
        
        if (this.selectedPart != null) {
            return this.selectedPart.getOffering(offering);
        } else {
            return null;
        }
    }
    
    public int getPartType() {
        
        if (this.selectedPart != null) {
            return this.selectedPart.getType();
        } else {
            return -1;
        }
    }
    
    public LinkedList getGroups() {
        return this.groups;
    }
    
    public synchronized CourseInfo getCourseInfo() {
        return this.courseInfo;
    }
    
    public synchronized String getLabel() {
        return this.courseInfo.getLabel();
    }
    
    public synchronized String getMessage() {
        String rv = this.msg;
        this.msg = null;
        return rv;
    }
    
    public synchronized int getMinScore() {
        try {
            Part grade = (Part) this.getPart(Part.ARVOSANA, false);
            return grade.getFirstXtrScore();
        } catch (NullIdException nid) {
            return 1;
        }
    }
    
    public synchronized Vector getGradeBoundaries() {
        try {
            Part grade = (Part) this.getPart(Part.ARVOSANA, false);
            return grade.getScoreBoundaries();
        } catch (NullIdException nid) {
            return null;
        }
    }
    
    public synchronized double getGradeStep() {
        try {
            Part grade = (Part) this.getPart(Part.ARVOSANA, false);
            return grade.getXtrStep();
        } catch (NullIdException nid) {
            return 1;
        }
    }
    
    public int getModifyCount() {
        return this.modifyCount;
    }
    
    public synchronized Part getPart(int type, boolean newIfReq)
            throws NullIdException {
        if (type < 0 || type >= Part.NO_OF_TYPES) {
            return null;
        }
        
        int i = 0;
        for (; i < this.parts.size(); i++) {
            Part part = (Part) this.parts.get(i);
            int pid = part.getType();
            
            if (type == pid) {
                return part;
            } else if (type < pid) {
                break;
            }
        }
        
        if (newIfReq) {
            Part part = new Part(type, 0);
            this.parts.add(i, part);
            return part;
        } else {
            return null;
        }
    }
    
    public synchronized Vector getParts() {
        return this.parts;
    }
    
    public synchronized Vector getPartsOrdered() {
        Vector rv = new Vector();
        rv.setSize(Part.NO_OF_TYPES);
        
        for (int i = 0; i < this.parts.size(); i++) {
            Part p = (Part) this.parts.get(i);            
            rv.set(p.getId(), p);
        }
        return rv;
    }
    
    public synchronized String getScale() {
        return this.scale;
    }
    
    public synchronized String getSelectDescription() {
        String desc = null;
        String[][] names = {{"ryhmä", "ryhmät"}, {"sukunimi", "sukunimet"}, {"opiskelijanumero", "opiskelijanumerot"}};
        
        for (int type = 0; type < names.length; type++) {
            LinkedList list = null;
            switch (type) {
                case 0:
                    list = this.group;                    
                    break;
                case 1:
                    list = this.lname;                    
                    break;
                case 2:
                    list = this.ssn;                    
                    break;
            }
            
            if (list != null) {
                int n = 0;
                String tmp = null;
                String g = null;
                Iterator i = list.iterator();
                while (i.hasNext()) {
                    n++;
                    if (g != null) {
                        if (tmp == null) {
                            tmp = "" + g;
                        } else {
                            tmp += ", " + g;
                        }
                    }
                    g = "" + i.next();
                }
                
                if (g != null) {
                    if (tmp == null) {
                        tmp = "" + g;
                    } else {
                        tmp += " ja " + g;
                    }
                }
                
                if (n == 1) {
                    if (desc == null) {
                        desc = names[type][0] + " " + tmp;
                    } else {
                        desc += ", " + names[type][0] + " " + tmp;
                    }
                } else if (n > 1) {
                    if (desc == null) {
                        desc = names[type][1] + " " + tmp;
                    } else {
                        desc += ", " + names[type][1] + " " + tmp;
                    }
                }
            }
        }
        
        if (lnameFrom != null || lnameTo != null) {
            if (desc == null) {
                desc = "opiskelijat ";
            } else {
                desc += ", opiskelijat ";
            }
            
            desc +=
                    (lnameFrom == null ? "*" : lnameFrom)
                    + " - "
                    + (lnameTo == null ? "*" : lnameTo);
        }
        
        if (desc != null) {
            desc = desc.replace('%', '*');
        }
        
        return desc;
    }
    
    public synchronized Part getSelectedPart() {
        
        if (this.selectedPart == null && this.parts.size() > 0) {
            this.selectedPart = (Part) this.parts.get(0);
        }
        
        return this.selectedPart;
    }
    
    public static String getStrTable(Vector from, String undefCol) {
        return getStrTable(from, undefCol, ",");
    }
    
    public static String getStrTable(Vector from, String undefCol, String delim) {
        if (from == null || undefCol == null) {
            return null;
        }
        
        String rv = "";
        final String DELIM = delim;
        
        for (int i = 0; i < from.size(); i++) {
            Object tmp = from.get(i);
            
            if (tmp == null) {
                rv += DELIM + getStrTableCell(null, undefCol);
            } else {
                rv += DELIM + getStrTableCell(tmp.toString(), undefCol);
            }
        }

        // pilkku pois
        if (rv.length() > 0) {
            rv = rv.substring(DELIM.length(), rv.length());
        }
        
        return rv;
    }
    
    public static String getStrTableCell(int from) {
        return getStrTableCell("" + from);
    }
    
    public static String getStrTableCell(String from) {
        return getStrTableCell(from, STR_TABLE_COL_UNDEF);
    }
    
    public static String getStrTableCell(String from, String undefColCode) {
        if (from == null || from.trim().equals("") || undefColCode == null) {
            return undefColCode;
        }
        int strTableColLen = undefColCode.length();
        
        from = from.trim();
        int l = from.length();
        
        if (l > strTableColLen) {
            from = from.substring(0, strTableColLen);
        } else if (l < strTableColLen) {
            while (l++ < strTableColLen) {
                from = " " + from;
            }
        }
        
        return from;
    }

    public synchronized Vector getStudents()
            throws SQLException, NullIdException, NullParameterException, ClassNotFoundException {
        
        
        String sfilter = "";
        String cmpFilter = "";
        String or = " OR\n     ";

        /*
         *  Opiskelijoiden haku ryhmän perusteella.
         */
        Iterator itr = this.group.iterator();
        while (itr.hasNext()) {
            String arg = "" + itr.next();
            cmpFilter += arg;
            sfilter += or + "ilmo_jnro = " + arg;
        }

        /*
         *  Opiskelijoiden haku sukunimen perusteella.
         */
        itr = this.lname.iterator();
        while (itr.hasNext()) {
            String arg = SQLApostrophe(((String) itr.next()).toUpperCase());
            cmpFilter += arg;
            sfilter += or + "UPPER(sukunimi) LIKE '" + arg + "'";
        }
        /*
         *  Opiskelijoiden haku opiskelijanumeron perusteella
         */
        itr = this.ssn.iterator();
        while (itr.hasNext()) {
            String arg = "" + itr.next();
            cmpFilter += arg;
            sfilter += or + "op.hetu LIKE '" + arg + "'";
        }

        /*
         * Opiskelijoiden haku henkilötunnuksen  perusteella
         */
        itr = this.sids.iterator();
        while (itr.hasNext()) {
            String arg = "" + itr.next();
            cmpFilter += arg;
            sfilter += or + "op.personid ='" + arg + "'";
        }

        /*
         *  Opiskelijoiden haku sukunimen perusteella annetulta väliltä.
         */
        if (this.lnameFrom != null || this.lnameTo != null) {
            cmpFilter += this.lnameFrom + this.lnameTo;

            // tätyykö tämä osa laittaa sulkujen sisään?
            boolean usepar = sfilter.length() > 0;
            
            sfilter += or;
            
            if (usepar) {
                sfilter += "(";
            }
            
            if (this.lnameFrom != null) {
                sfilter += "UPPER(sukunimi) >= '" + SQLApostrophe(this.lnameFrom.toUpperCase()) + "'";
            }
            
            if (this.lnameFrom != null && this.lnameTo != null) {
                sfilter += " AND ";
            }
            
            if (this.lnameTo != null) {
                sfilter += "UPPER(sukunimi) <= '" + SQLApostrophe(this.lnameTo.toUpperCase()) + "'";
            }
            
            if (usepar) {
                sfilter += ")";
            }
            
        }

        // Järjestäminen (jos järjestämisperuste muuttuu, suoritetaan uusi haku)
        cmpFilter += this.orderBy;
        
        if (this.students == null
                || this.lastStudentFilter == null
                || !cmpFilter.equals(this.lastStudentFilter)) {
            
            if (sfilter.length() > or.length()) {
                sfilter = " AND\n    (" + sfilter.substring(or.length()) + ")";
            } else {
                sfilter = null;
            }
            
            String sql =
                    "SELECT DISTINCT op.hetu,\n"
                    + "    INITCAP(op.sukunimi) AS sukunimi,\n"
                    + "    NLS_INITCAP(op.etunimi) AS etunimi,\n"
                    + "    op.sahkopostiosoite AS sposti,\n"
                    + "    op.personid,\n"
                    + "    op.entinen_sukunimi,\n"
                    + "    op.osoite,\n"
                    + "    op.puhelin,\n"
                    + "    op.paa_aine,\n"
                    + "    op.aloitusvuosi,\n"
                    + "    os.laskarisuoritukset,\n"
                    + "    os.harjoitustyopisteet,\n"
                    + "    os.koepisteet,\n"
                    + "    os.arvosana,\n"
                    + "    os.ilmoittautumis_pvm,\n"
                    + "    os.ilmo_jnro,\n"
                    + "    os.ryhma_nro,\n"
                    + "    os.yhteispisteet,\n"
                    + "    os.laskarihyvitys,\n"
                    + "    os.jaassa,\n"
                    + "    os.harjoitustyohyvitys,\n"
                    + "    os.kielikoodi,\n"
                    + "    os.laajuus_op\n"
                    + "  FROM osallistuminen os, opiskelija op\n"
                    + "  WHERE os.kurssikoodi = '" + this.courseInfo.ccode + "' AND \n"
                    + "    os.lukukausi = '" + this.courseInfo.term + "' AND \n"
                    + "    os.lukuvuosi = " + this.courseInfo.year + " AND \n"
                    + "    os.tyyppi = '" + this.courseInfo.type + "' AND \n"
                    + "    os.kurssi_nro = " + this.courseInfo.cno + " AND \n"
                    + "    os.hetu = op.hetu AND \n"
                    + "    os.voimassa = 'K'"
                    + (sfilter != null ? sfilter : "") + "\n"
                    + "  ORDER BY " + ORDER_BY[this.orderBy];

            Connection con = DBConnectionManager.createConnection();            
            Statement stmt = null;
            ResultSet rs = null;
            
            try {                
                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                
                this.students = new Vector();
                
                int sid = 0;
                while (rs.next()) {
                    Student s = new Student(sid,
                            rs.getString("hetu"),
                            rs.getString("sukunimi"),
                            rs.getString("etunimi"),
                            rs.getString("sposti"),
                            rs.getInt("ilmo_jnro"));
                    
                    s.setEnrolment(rs.getTimestamp("ilmoittautumis_pvm"));
                    s.setGroupID(rs.getInt("ryhma_nro"));
                    s.setSNO(rs.getString("personid"));
                    s.setXtrTotal(rs.getInt("yhteispisteet"));
                    s.setXtrScore(Part.LASKARI, rs.getInt("laskarihyvitys"));
                    s.setXtrScore(Part.HARJOITUSTYO, rs.getInt("harjoitustyohyvitys"));
                    s.setState(rs.getString("jaassa"));
                    s.setAddress(rs.getString("osoite"));
                    s.setPrevLName(rs.getString("entinen_sukunimi"));
                    s.setPhone(rs.getString("puhelin"));
                    s.setMajor(rs.getString("paa_aine"));
                    s.setStartYear(rs.getInt("aloitusvuosi"));
                    
                    s.setCreditsNew(rs.getInt("laajuus_op"));
                    s.setLanguage(rs.getString("kielikoodi"));
                    String[] scoreFields = {"laskarisuoritukset", "harjoitustyopisteet", "koepisteet", "arvosana", "laajuus_op", "kielikoodi"};
                    
                    for (int i = 0; i < this.parts.size(); i++) {
                        Part p = (Part) this.parts.get(i);
                        int pid = p.getId();
                        
                        String scores = rs.getString(scoreFields[ pid]);
                        
                        Offering[] offerings = p.getOfferings();
                        
                        for (int j = 0; j < offerings.length; j++) {
                            Offering o = offerings[ j];
                            int oid = o.getId();
                            String score = null;
                            
                            if (scores != null && scores.length() > oid * 3) {
                                int start = oid * 3;
                                int end = (scores.length() > start + 2 ? start + 2 : scores.length());
                                score = scores.substring(start, end);
                            }
                            s.setScore(p, o, score);
                        }
                    }
                    this.students.add(sid, s);
                    sid++;
                }
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (Exception e) {
                }
                DBConnectionManager.closeConnection(con);
            }
        }
        this.lastStudentFilter = cmpFilter;
        
        return this.students;
    }
    
    public synchronized Student getStudent(int id) {
        if (this.students == null || this.students.size() <= id) {
            return null;
        } else {
            return (Student) this.students.get(id);
        }
    }    
    
    public synchronized int getUserRole() {
        return this.courseInfo.getUserRole();
    }
    
    public synchronized String getValue() {
        return this.courseInfo.getValue();
    }
    
    public synchronized boolean isFrozen() {
        return this.courseInfo.isFrozen();
    }
    
    public synchronized void load()
            throws SQLException, NullIdException, ClassNotFoundException {
        
        this.groups.clear();
        
        Connection con = DBConnectionManager.createConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // Load groups
            pstmt = con.prepareStatement(LOAD_GROUPS_SQL);
            pstmt.setString(1, this.courseInfo.ccode);
            pstmt.setString(2, this.courseInfo.term);
            pstmt.setInt(3, this.courseInfo.year);
            pstmt.setString(4, this.courseInfo.type);
            pstmt.setInt(5, this.courseInfo.cno);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int grp = rs.getInt("ilmo_jnro");
                if (!rs.wasNull()) {
                    this.groups.add(new Integer(grp));
                }
            }
            rs.close();
            pstmt.close();

            pstmt = con.prepareStatement(LOAD_COURSE_SQL);
            
            pstmt.setString(1, this.courseInfo.ccode);
            pstmt.setString(2, this.courseInfo.term);
            pstmt.setInt(3, this.courseInfo.year);
            pstmt.setString(4, this.courseInfo.type);
            pstmt.setInt(5, this.courseInfo.cno);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Part part = null;
                this.parts = new Vector();
                
                String freeze = rs.getString("tila");
                if (freeze != null && freeze.equals("J")) {
                    this.courseInfo.freeze();
                }
                
                this.credits = rs.getInt("opintoviikot");
                this.creditsMax = rs.getInt("opintoviikot_ylaraja");       //new 05/08
                this.newCreditsMax = rs.getInt("opintopisteet_ylaraja");  //new 05/08
                this.newCredits = rs.getInt("opintopisteet");          //new 05/08

                
                this.don = rs.getString("omistaja");
                
                java.sql.Date edate = rs.getDate("suoritus_pvm");
                
                this.endDate = Calendar.getInstance();
                this.endDate.setTime(rs.getDate("paattymis_pvm"));
                
                if (edate != null) {
                    this.examDate = Calendar.getInstance();
                    this.examDate.setTime(edate);
                }
                
                this.scale = rs.getString("arvostellaanko");
                if (this.scale == null || this.scale.equals("")) {
                    this.scale = "K";
                }
                
                this.convention = rs.getInt("laskentakaava");
                if (rs.wasNull()) {
                    this.convention = DEFAULT_CONVENTION;
                }

                /*
                 *  Laskarit
                 */
                int nbr = rs.getInt("laskarikerta_lkm");
                int reqo = rs.getInt("pakolliset_laskarikerta_lkm");
                int reqs = rs.getInt("pakolliset_laskaritehtava_lkm");
                int xtr = rs.getInt("max_laskaripisteet");
                int firstXtr = rs.getInt("lisapistealaraja");
                double xtrStep = rs.getDouble("lisapisteiden_askelkoko");
                String maxScores = rs.getString("laskaritehtava_lkm");
                String minScores = rs.getString("hyvaksytty_laskarilasnaolo");
                String scoreBoundaries = rs.getString("lisapisterajat");
                
                if (nbr > 0) {
                    Offering[] offerings = new Offering[nbr];
                    
                    for (int i = 0; i < nbr; i++) {
                        int start = 3 * i;
                        int end = 3 * i + 2;
                        int maxScore = 0;
                        int minScore = 0;
                        
                        if (maxScores != null && end <= maxScores.length()) {
                            try {
                                maxScore = Integer.parseInt(maxScores.substring(start, end).trim());
                            } catch (NumberFormatException nfe) {
                            }
                        }
                        offerings[ i] = new Offering(i, maxScore);
                        
                        if (minScores != null && end <= minScores.length()) {
                            try {
                                minScore = Integer.parseInt(minScores.substring(start, end).trim());
                            } catch (NumberFormatException nfe) {
                            }
                        }
                        offerings[ i].initMinScore(minScore);
                    }
                    part = new Part(Part.LASKARI, offerings, reqo, xtr);
                    part.setRequiredScore(reqs);
                    part.setFirstXtrScore(firstXtr);
                    part.setXtrStep(xtrStep);
                    part.partDefMod();
                    
                    if (scoreBoundaries != null && scoreBoundaries.length() > 1) {
                        part.setScoreBoundaries(scoreBoundaries);
                        part.scoreBoundariesMod();
                    }
                    this.parts.add(part);
                }
                if (maxScores == null) {
                    isNull[0] = true;
                }


                /*
                 *  Harjoitustyöt
                 */
                nbr = rs.getInt("harjoitustyo_lkm");
                reqo = rs.getInt("pakolliset_harjoitustyo_lkm");
                reqs = rs.getInt("min_harjoitustyopisteet_summa");
                xtr = rs.getInt("harjoitustyopisteet");
                firstXtr = rs.getInt("ht_lisapistealaraja");
                xtrStep = rs.getDouble("harjoitustoiden_askelkoko");
                maxScores = rs.getString("max_harjoitustyopisteet");
                minScores = rs.getString("min_harjoitustyopisteet");
                scoreBoundaries = rs.getString("harjoitustyon_pisterajat");
                
                if (nbr > 0) {
                    Offering[] offerings = new Offering[nbr];
                    
                    for (int i = 0; i < nbr; i++) {
                        int start = 3 * i;
                        int end = 3 * i + 2;
                        int maxScore = 0;
                        int minScore = 0;
                        
                        if (maxScores != null && end <= maxScores.length()) {
                            try {
                                maxScore = Integer.parseInt(maxScores.substring(start, end).trim());
                            } catch (NumberFormatException nfe) {
                            }
                        }
                        offerings[ i] = new Offering(i, maxScore);
                        
                        if (minScores != null && end <= minScores.length()) {
                            try {
                                minScore = Integer.parseInt(minScores.substring(start, end).trim());
                            } catch (NumberFormatException nfe) {
                            }
                        }
                        offerings[ i].initMinScore(minScore);
                    }
                    part = new Part(Part.HARJOITUSTYO, offerings, reqo, xtr);
                    part.setRequiredScore(reqs);
                    part.setFirstXtrScore(firstXtr);
                    part.setXtrStep(xtrStep);
                    part.partDefMod();
                    
                    if (scoreBoundaries != null && scoreBoundaries.length() > 1) {
                        part.setScoreBoundaries(scoreBoundaries);
                        part.scoreBoundariesMod();
                    }
                    this.parts.add(part);
                }
                if (maxScores == null) {
                    isNull[1] = true;
                }

                /*
                 *  Kokeet
                 */
                nbr = rs.getInt("valikokeet_lkm");
                reqo = rs.getInt("pakolliset_koe_lkm");
                reqs = rs.getInt("min_koepisteet_summa");
                maxScores = rs.getString("max_koepisteet");
                minScores = rs.getString("min_koepisteet");
                
                if (nbr > 0) {
                    Offering[] offerings = new Offering[nbr];
                    
                    for (int i = 0; i < nbr; i++) {
                        int start = 3 * i;
                        int end = 3 * i + 2;
                        int maxScore = 0;
                        int minScore = 0;
                        
                        if (maxScores != null && end <= maxScores.length()) {
                            try {
                                maxScore = Integer.parseInt(maxScores.substring(start, end).trim());
                            } catch (NumberFormatException nfe) {
                            }
                        }
                        offerings[ i] = new Offering(i, maxScore);
                        
                        if (minScores != null && end <= minScores.length()) {
                            try {
                                minScore = Integer.parseInt(minScores.substring(start, end).trim());
                            } catch (NumberFormatException nfe) {
                            }
                        }
                        offerings[ i].initMinScore(minScore);
                    }
                    part = new Part(Part.KOE, offerings, reqo, 0);
                    part.setFirstXtrScore(1);
                    part.setRequiredScore(reqs);
                    part.partDefMod();
                    this.parts.add(part);
                }
                if (maxScores == null) {
                    isNull[2] = true;
                }


                /*
                 *  Arvosana
                 */
                firstXtr = rs.getInt("min_yhteispisteet");
                xtrStep = rs.getDouble("arvostelun_askelkoko");
                scoreBoundaries = rs.getString("arvosanarajat");
                part = new Part(Part.ARVOSANA, 1);
                part.setXtrScore(Part.grades.length);
                part.setFirstXtrScore(firstXtr);
                part.setXtrStep(xtrStep);
                part.partDefMod();
                
                if (scoreBoundaries != null && scoreBoundaries.length() > 1) {
                    part.setScoreBoundaries(scoreBoundaries);
                    part.scoreBoundariesMod();
                }
                this.parts.add(part);

                /*
                 * Opintopisteet
                 */
                
                
                Offering[] points = new Offering[1];
                points[0] = new Offering(0, this.newCreditsMax);
                points[0].initMinScore(this.newCredits);
                part = new Part(Part.OPINTOPISTEET, points);
                part.partDefMod();
                this.parts.add(part);
                part = new Part(Part.KIELIKOODI, 1);
                part.partDefMod();
                this.parts.add(part);
                
            }
            
            if (this.selectedPart == null && this.parts.size() > 0) {
                this.selectedPart = (Part) this.parts.get(0);
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            DBConnectionManager.closeConnection(con);
        }
    }
    
    public synchronized boolean makeAssessment(String ruser)
            throws SQLException, ClassNotFoundException {
        if (ruser == null) {
            this.msg = "Arvostelijaa ei tunnistettu.";
            return false;
        }
        
        boolean rv = true;
        Connection con = DBConnectionManager.createConnection();
        CallableStatement stm = null;
        try {
            
            stm = con.prepareCall("{ ? = call arvostelu05.arvostele (?, ?, ?, ?, ?, ?) }");
            
            stm.registerOutParameter(1, java.sql.Types.INTEGER);
            
            stm.setString(2, this.courseInfo.ccode);
            stm.setInt(3, this.courseInfo.year);
            stm.setString(4, this.courseInfo.term);
            stm.setString(5, this.courseInfo.type);
            stm.setInt(6, this.courseInfo.cno);
            stm.setString(7, ruser);
            
            stm.executeUpdate();
            
            int result = stm.getInt(1);
            
            if (result == 0) {
                rv = true;
                this.msg = null;
            } else {
                rv = false;
                this.msg = "Arvostelu ei onnistunut (" + result + ").";
            }
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                }
            }
            DBConnectionManager.closeConnection(con);
        }
        return rv;
    }
      
    public void newSearch() {
        newSearch(false);
    }
    
    public synchronized void newSearch(boolean force) {
        
        if (force) {
            this.students = null;
        }
        this.orderBy = ORDER_BY_NAME;
        this.group.clear();        
        this.lname.clear();
        this.ssn.clear();
        this.lnameFrom = this.lnameTo = null;
    }

    public void orderBy(int by) {
        if (by >= 0 && by < ORDER_BY.length) {
            this.orderBy = by;
        }
    }
 
    public synchronized boolean removeStudent(Student student)
            throws SQLException, ClassNotFoundException {
        if (student == null) {
            this.msg = "Tuntematon opiskelija.";
            return false;
        }
        if (isFrozen() && student.isFrozen()) {
            this.msg = "Kertaalleen jäädytettyä opiskelijaa ei voi poistaa kurssilta.";
            return false;
        }
        
        boolean rv = true;
        Connection con = DBConnectionManager.createConnection();
        CallableStatement stm = null;
        try {
            stm = con.prepareCall("{ ? = call poistaopiskelija (?, ?, ?, ?, ?, ?, ?) }");
            stm.registerOutParameter(1, java.sql.Types.INTEGER);
            
            stm.setString(2, this.courseInfo.ccode);
            stm.setInt(3, this.courseInfo.year);
            stm.setString(4, this.courseInfo.term);
            stm.setString(5, this.courseInfo.type);
            stm.setInt(6, this.courseInfo.cno);
            stm.setInt(7, student.getGroupID());
            stm.setString(8, student.getSSNID());
            
            stm.executeUpdate();
            
            int result = stm.getInt(1);
            
            if (result == 0) {
                rv = true;
                this.msg = null;
            } else {
                rv = false;
                switch (result) {
                    case 1:
                        this.msg = "Poistaminen ei onnistunut.";
                        break;
                    case 2:
                        this.msg = "Ryhmän opiskelijalaskurin päivitys epäonnistui.";
                        break;
                    case 3:
                        this.msg = "Poistamisen vahvistaminen epäonnistui.";
                        break;
                }
            }
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                }
            }
            DBConnectionManager.closeConnection(con);
        }
        return rv;
    }    
    
    public synchronized boolean returnStudent(Student student)
            throws SQLException, ClassNotFoundException {
        if (student == null) {
            return false;
        }
        
        boolean rv = true;
        Connection con = DBConnectionManager.createConnection();
        CallableStatement stm = null;
        
        try {
            stm = con.prepareCall("{ ? = call palautaopiskelija (?, ?, ?, ?, ?, ?, ?) }");
            stm.registerOutParameter(1, java.sql.Types.INTEGER);
            
            stm.setString(2, this.courseInfo.ccode);
            stm.setInt(3, this.courseInfo.year);
            stm.setString(4, this.courseInfo.term);
            stm.setString(5, this.courseInfo.type);
            stm.setInt(6, this.courseInfo.cno);
            stm.setInt(7, student.getGroupID());
            stm.setString(8, student.getSSNID());
            
            stm.executeUpdate();
            
            int result = stm.getInt(1);
            if (result == 0) {
                rv = true;
                this.msg = null;
            } else {
                rv = false;
                switch (result) {
                    case 1:
                        this.msg = "Palautus ei onnistunut.";
                        break;
                    case 2:
                        this.msg = "Ryhmän opiskelijalaskurin päivitys epäonnistui.";
                        break;
                    case 3:
                        this.msg = "Palauttamisen vahvistaminen epäonnistui.";
                        break;
                }
            }
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                }
            }
            DBConnectionManager.closeConnection(con);
        }
        return rv;
    }
    
    public synchronized boolean selectOffering(int time) {
        return this.selectedPart.selectOffering(time);
    }
    
    public synchronized boolean selectPart(int type) {
        if (type == Part.UNDEF) {
            this.selectedPart = null;
            return true;
        }
        
        try {
            int p = parts.indexOf(new Part(type, 0));
            if (p < 0) {
                return false;
            } else {
                this.selectedPart = (Part) parts.get(p);
                return true;
            }
        } catch (NullIdException nie) {
            return false;
        }
    }
    
    public synchronized boolean setExamDate(String edate) {
        if (isFrozen()) {
            this.msg = "Kurssi on jäädytetty.";
            return false;
        }
        Calendar c = DateParser.parseDate(edate);
        if (c == null) {
            this.msg = "Suoritupäivämäärä annettu virheellisesti: '"
                    + edate + "'. (pp.kk.vvvv)";
            return false;
        } else {
            Calendar sysdate = new GregorianCalendar();
            Calendar lowLimit = sysdate;
            Calendar hiLimit = null;

            // Suorituspäivämäärä < max(sysdate, päättymis_pvm) + 2kk (Satu Eloranta)
            if (sysdate.before(this.endDate)) {
                hiLimit = (Calendar) this.endDate.clone();
            } else {
                hiLimit = (Calendar) sysdate.clone();
            }
            hiLimit.add(Calendar.MONTH, 2);

            // Suorituspäivämäärä > sysdate - 6kk (Satu Eloranta)
            lowLimit.add(Calendar.MONTH, -6);
            
            if (c.before(lowLimit) || c.after(hiLimit)) {
                this.msg = "Suorituspäivämäärän tulee olla välillä "
                        + lowLimit.get(Calendar.DAY_OF_MONTH) + "."
                        + (lowLimit.get(Calendar.MONTH) + 1) + "."
                        + lowLimit.get(Calendar.YEAR) + " - "
                        + hiLimit.get(Calendar.DAY_OF_MONTH) + "."
                        + (hiLimit.get(Calendar.MONTH) + 1) + "."
                        + hiLimit.get(Calendar.YEAR) + " ja ennen kurssin jäädyttämistä.";
                return false;
            }
            
            if (this.examDate == null
                    || !this.examDate.equals(c)) {
                this.examDateMod = true;
                this.examDate = c;
            }
            return true;
        }
    }
    
    public synchronized boolean setGradingConvention(int conv) {
        if (isFrozen()) {
            this.msg = "Kurssi on jäädytetty.";
            return false;
        }
        if (conv > 0 && conv <= this.conventions.size()) {
            if (conv != this.convention) {
                this.convention = conv;
                this.conventionMod = true;
            }
            return true;
        } else {
            return false;
        }
    }
    
    public synchronized boolean setScale(String scale) {
        if (isFrozen()) {
            this.msg = "Kurssi on jäädytetty.";
            return false;
        }
        if (scale != null
                && (scale.equals("E") || scale.equals("K"))) {
            if (!this.scale.equals(scale)) {
                this.scaleMod = true;
                this.scale = scale;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Muuntaa str:n heittomerkit (') tuplaheittomerkeiksi ('').
     */
    public static String SQLApostrophe(String str) {
        String rv = "";
        int i = 0;
        int ii = 0;
        
        i = str.indexOf("'");
        while (i > -1) {
            rv += str.substring(ii, i) + "''";
            ii = i + 1;
            i = str.indexOf("'", ii);
        }
        rv += str.substring(ii, str.length());
        
        return rv;
    }

    public String toString() {
        return this.courseInfo.toString();
    }
}
