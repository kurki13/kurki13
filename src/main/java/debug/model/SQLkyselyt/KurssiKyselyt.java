/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import java.sql.PreparedStatement;

/**
 *
 * @author tkairola
 */
public class KurssiKyselyt {
    
        private static final int MONTHS_OPEN = 12;
    private static final int SUPER_OPEN = 48;
    private static final String COURSE_INFOS = //<editor-fold defaultstate="collapsed" desc="courseInfos">

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
    private static final String SUPER_INFOS = //<editor-fold defaultstate="collapsed" desc="superInfos">

            "SELECT DISTINCT k.kurssikoodi, k.lukuvuosi, k.lukukausi, k.tyyppi, k.tila,\n"
            + "    k.kurssi_nro, k.nimi AS nimi, k.alkamis_pvm AS alkamis_pvm, k.paattymis_pvm,\n"
            + "    decode(k.tila, 'J', 'X', 'A') AS orderBy\n"
            + "  FROM kurssi k\n"
            + "  WHERE k.paattymis_pvm > to_date('31.12.2001','DD.MM.YYYY')\n"
            + "    AND ADD_MONTHS( k.paattymis_pvm, " + SUPER_OPEN + " ) >= SYSDATE\n"
            //	+"    AND k.alkamis_pvm <= SYSDATE+30\n"
            //      +"    AND k.tyyppi in ('K', 'S')\n" // vain luentokurssit ja seminaarit
            + "    AND k.tyyppi in ('K', 'S', 'A')\n" // luento- ja laboratoriokurssit sekä seminaarit
            + "    AND k.tila not in ('S', 'O')\n"
            // 	+"    AND k.tila not in ('S', 'O', 'J')\n" // ei jäädytettyjä kursseja

            // KOKEET
            + "UNION\n"
            + "(SELECT DISTINCT ku.kurssikoodi, ku.lukuvuosi, ku.lukukausi, ku.tyyppi, ku.tila,\n"
            + "    ku.kurssi_nro, ku.nimi || ', koe ' || TO_CHAR(ku.paattymis_pvm, ' DD.MM.YY'),\n"
            + "    ku.alkamis_pvm, ku.paattymis_pvm, decode(ku.tila, 'J', 'X', 'A') AS orderBy\n"
            + "  FROM kurssi ku\n"
            + "  WHERE ku.paattymis_pvm > to_date('31.12.2001','DD.MM.YYYY')\n"
            + "    AND ADD_MONTHS( ku.paattymis_pvm, " + SUPER_OPEN + " ) >= SYSDATE\n"
            //	+"    AND ku.paattymis_pvm <= SYSDATE+30\n"
            + "    AND ku.tila not in ('S', 'O')\n"
            // 	+"    AND ku.tila not in ('S', 'O', 'J')\n" // ei jäädytettyjä kursseja
            + "    AND ku.tyyppi = 'L')\n" // vain kokeet
            + "ORDER BY orderBy ASC, nimi ASC, alkamis_pvm ASC";
    //</editor-fold>
    
       public static String super_infos_queryString() {
        return SUPER_INFOS;
    }

       //string.format
//    public static PreparedStatement course_infos(String ruser) {
//        PreparedStatement preparedStatement = databaseConnection.prepareStatement(COURSE_INFOS);
//        preparedStatement.setString(1, ruser);
//        preparedStatement.setString(2, ruser);
//        return preparedStatement;
//    }
}
