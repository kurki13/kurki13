package debug;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kurki.util.Configuration;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mkctammi
 */
public class Pipe {

    public static final int MONTHS_OPEN = 12;
    public static final int SUPER_OPEN = 48;
    protected static final String COURSE_INFOS =
            //<editor-fold defaultstate="collapsed" desc="courseInfos">

            // LUENTO- (JA LABORATORIOKURSSIT)
            "SELECT DISTINCT k.kurssikoodi, k.lukuvuosi, k.lukukausi, k.tyyppi, k.tila,\n"
            + "    k.kurssi_nro, k.nimi AS nimi, k.alkamis_pvm AS alkamis_pvm, k.paattymis_pvm,\n"
            + "    decode(k.tila, 'J', 'X', 'A') AS orderBy\n"
            + "  FROM kurssi k, opetus o, opetustehtavan_hoito oh, henkilo h\n"
            + "  WHERE k.paattymis_pvm > to_date('31.12.2001','DD.MM.YYYY')\n"
            + "    AND ADD_MONTHS( k.paattymis_pvm, " + MONTHS_OPEN + " ) >= SYSDATE\n"
            //	+"    AND k.alkamis_pvm <= SYSDATE+30\n"
            + "    AND k.tila not in ('S', 'O')\n"
            // 	+"    AND k.tila not in ('S', 'O', 'J')\n" // ei jäädytettyjä kursseja
            //	+"    AND k.tyyppi in ('K', 'S')\n" // vain luentokurssit ja seminaarit
            + "    AND k.tyyppi in ('K', 'S', 'A')\n" // luento- ja laboratoriokurssit sekä seminaarit
            + "    AND o.kurssikoodi = k.kurssikoodi AND oh.kurssikoodi = k.kurssikoodi\n"
            + "    AND o.lukukausi = k.lukukausi AND oh.lukukausi = k.lukukausi\n"
            + "    AND o.lukuvuosi = k.lukuvuosi AND oh.lukuvuosi = k.lukuvuosi\n"
            + "    AND o.tyyppi = k.tyyppi AND oh.tyyppi = k.tyyppi\n"
            + "    AND o.kurssi_nro = k.kurssi_nro AND oh.kurssi_nro = k.kurssi_nro\n"
            + "    AND oh.htunnus = h.htunnus\n"
            + "    AND h.ktunnus = ?\n"
            // KOKEET
            + "UNION\n"
            + "(SELECT DISTINCT ku.kurssikoodi, ku.lukuvuosi, ku.lukukausi, ku.tyyppi, ku.tila,\n"
            + "    ku.kurssi_nro, ku.nimi || ', koe ' || TO_CHAR(ku.paattymis_pvm, 'DD.MM.YY'),\n"
            + "    ku.alkamis_pvm, ku.paattymis_pvm, decode(ku.tila, 'J', 'X', 'A') AS orderBy\n"
            + "  FROM kurssi ku, koe ko, henkilo h\n"
            + "  WHERE ku.paattymis_pvm > to_date('31.12.2001','DD.MM.YYYY')\n"
            + "    AND ADD_MONTHS( ku.paattymis_pvm, " + MONTHS_OPEN + " ) >= SYSDATE\n"
            //	+"    AND ku.paattymis_pvm <= SYSDATE+30\n"
            + "    AND ku.tyyppi = 'L'\n" // vain kokeet
            + "    AND ku.tila not in ('S', 'O')\n"
            // 	+"    AND ku.tila not in ('S', 'O', 'J')\n" // ei jäädytettyjä kursseja
            + "    AND ko.kurssikoodi = ku.kurssikoodi\n"
            + "    AND ko.lukukausi = ku.lukukausi\n"
            + "    AND ko.lukuvuosi = ku.lukuvuosi\n"
            + "    AND ko.tyyppi = ku.tyyppi\n"
            + "    AND ko.kurssi_nro = ku.kurssi_nro\n"
            + "    AND h.htunnus = ko.htunnus\n"
            + "    AND h.ktunnus = ?)\n"
            + "ORDER BY orderBy ASC, nimi ASC, alkamis_pvm ASC";
    //</editor-fold>
    protected static final String SUPER_INFOS =
            //<editor-fold defaultstate="collapsed" desc="superInfos">

            "SELECT DISTINCT k.kurssikoodi, k.lukuvuosi, k.lukukausi, k.tyyppi, k.tila,\n"
            + "    k.kurssi_nro, k.nimi AS nimi, k.alkamis_pvm AS alkamis_pvm, k.paattymis_pvm,\n"
            + "    decode(k.tila, 'J', 'X', 'A') AS orderBy\n"
            + "  FROM kurssi k\n"
            + "  WHERE k.paattymis_pvm > to_date('31.12.2001','DD.MM.YYYY')\n"
            + "    AND ADD_MONTHS( k.paattymis_pvm, " + SUPER_OPEN + " ) >= SYSDATE\n"
            + "    AND k.tyyppi in ('K', 'S', 'A')\n" // luento- ja laboratoriokurssit sekä seminaarit
            + "    AND k.tila not in ('S', 'O')\n"
            // KOKEET
            + "UNION\n"
            + "(SELECT DISTINCT ku.kurssikoodi, ku.lukuvuosi, ku.lukukausi, ku.tyyppi, ku.tila,\n"
            + "    ku.kurssi_nro, ku.nimi || ', koe ' || TO_CHAR(ku.paattymis_pvm, ' DD.MM.YY'),\n"
            + "    ku.alkamis_pvm, ku.paattymis_pvm, decode(ku.tila, 'J', 'X', 'A') AS orderBy\n"
            + "  FROM kurssi ku\n"
            + "  WHERE ku.paattymis_pvm > to_date('31.12.2001','DD.MM.YYYY')\n"
            + "    AND ADD_MONTHS( ku.paattymis_pvm, " + SUPER_OPEN + " ) >= SYSDATE\n"
            + "    AND ku.tila not in ('S', 'O')\n"
            + "    AND ku.tyyppi = 'L')\n" // vain kokeet
            + "ORDER BY orderBy ASC, nimi ASC, alkamis_pvm ASC";
    //</editor-fold>
    
    public static String getName() {
        return "Not a pipe";
    }

    private static void config() {
        Configuration.setProperty("dbDriver", "oracle.jdbc.OracleDriver");
        Configuration.setProperty("dbServer", "jdbc:oracle:thin:@bodbacka.cs.helsinki.fi:1521:test");
        Configuration.setProperty("dbUser", "tk_testi");
        Configuration.setProperty("dbPassword", "tapaus2");
    }
    
    private static Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.OracleDriver");
        return DriverManager.getConnection("jdbc:oracle:thin:@bodbacka.cs.helsinki.fi:1521:test", "tk_testi", "tapaus2");
    }

    public String test() throws SQLException, ClassNotFoundException {
        String ret = "";
        Connection conn = makeConnection();
        PreparedStatement ps = conn.prepareStatement("Select * from kurssi");
        ResultSet rs = ps.executeQuery();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            ret += i + ". " + rs.getMetaData().getColumnName(i) + " " + rs.getMetaData().getColumnTypeName(i) + "<br>";
        }
        ret+="<br>";
        while(rs.next()) {
            ret += rs.getString(1) + "<br>";
        }
        conn.close();
        return ret;
    }
}
