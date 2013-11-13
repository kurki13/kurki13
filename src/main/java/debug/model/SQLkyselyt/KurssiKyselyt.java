/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.SQLkyselyt;

import debug.SessioApuri;
import debug.dbconnection.DatabaseConnection;
import debug.model.Kurssi;
import debug.model.util.Filter;
import debug.model.util.SQLoader;
import debug.util.LocalisationBundle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mkctammi
 */
public class KurssiKyselyt {

	private static final int MONTHS_OPEN = 12;
	private static final int SUPER_OPEN = 48;
	private static final String asetaSuorituspvm = //<editor-fold defaultstate="collapsed" desc="asetaSuorituspvm">
		"UPDATE kurssi SET suoritus_pvm = ?\n"
		+ " WHERE kurssikoodi = ?\n"
		+ " AND lukukausi = ?\n"
		+ " AND lukuvuosi = ?\n"
		+ " AND tyyppi = ?\n"
		+ " AND kurssi_nro = ?\n";
	//</editor-fold>
	private static final String COURSE_INFOS = //<editor-fold defaultstate="collapsed" desc="courseInfos">

		// LUENTO- (JA LABORATORIOKURSSIT)
		"SELECT DISTINCT "
		+ "     k.*, \n"
		+ "     k.nimi AS nimiOrder, \n"
		+ "     k.alkamis_pvm AS alkamis_pvmOrder,\n"
		+ "     k.tila AS orderBy\n"
		+ "  FROM kurssi k, opetus o, opetustehtavan_hoito oh, henkilo h\n"
		+ "  WHERE k.paattymis_pvm > cast('2001-12-31' as date)\n"
		+ "    AND k.tila not in ('S', 'O')\n"
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
		+ "(SELECT DISTINCT "
		+ "     ku.*, \n"
		+ "     ku.nimi AS nimiOrder, \n"
		+ "     ku.alkamis_pvm as alkamis_pvmOrder, \n"
		+ "     ku.tila AS orderBy\n"
		+ "  FROM kurssi ku, koe ko, henkilo h\n"
		+ "  WHERE ku.paattymis_pvm > cast('2001-12-31' as date)\n"
		+ "    AND ku.tyyppi = 'L'\n" // vain kokeet
		+ "    AND ku.tila not in ('S', 'O')\n"
		+ "    AND ko.kurssikoodi = ku.kurssikoodi\n"
		+ "    AND ko.lukukausi = ku.lukukausi\n"
		+ "    AND ko.lukuvuosi = ku.lukuvuosi\n"
		+ "    AND ko.tyyppi = ku.tyyppi\n"
		+ "    AND ko.kurssi_nro = ku.kurssi_nro\n"
		+ "    AND h.htunnus = ko.htunnus\n"
		+ "    AND h.ktunnus = ?)\n"
		+ "ORDER BY orderBy ASC, nimiOrder ASC, alkamis_pvmOrder ASC";
	//</editor-fold>
	private static final String SUPER_INFOS =
		//<editor-fold defaultstate="collapsed" desc="superInfos">
		"SELECT DISTINCT "
		+ "     k.*, "
		+ "     k.nimi AS nimiOrder, "
		+ "     k.alkamis_pvm AS alkamis_pvmOrder,"
		+ "     k.tila AS orderBy"
		+ "  FROM kurssi k"
		+ "  WHERE k.tila not in ('S', 'O')"
		// KOKEET
		+ "UNION\n"
		+ "(SELECT DISTINCT "
		+ "     ku.*, ku.nimi AS nimiOrder, "
		+ "     ku.alkamis_pvm AS alkamis_pvmOrder,"
		+ "     ku.tila AS orderBy"
		+ "  FROM kurssi ku"
		+ "  WHERE ku.tila not in ('S', 'O')"
		+ "     AND ku.tyyppi = 'L')" // vain kokeet
		+ "ORDER BY orderBy ASC, nimiOrder ASC, alkamis_pvmOrder ASC";
	//</editor-fold>

	public static List<Kurssi> kurssitYllapitajalle() throws SQLException {
		Connection databaseConnection = DatabaseConnection.makeConnection();
		PreparedStatement preparedStatement = databaseConnection.prepareStatement(SUPER_INFOS);
		List<Kurssi> kurssit = SQLoader.loadTablesFromPreparedStatement(new Kurssi(), preparedStatement, databaseConnection);
		return filtteroiVanhatKurssitPois(kurssit, SUPER_OPEN);
	}

	/**
	 * palauttaa uuden listan josta on filtteröity annetusta kurssilistasta
	 * pois kaikki kk -vanhemmat kurssit pois
	 *
	 * @param kurssit
	 * @param kuukautta
	 * @return
	 */
	protected static List<Kurssi> filtteroiVanhatKurssitPois(List<Kurssi> kurssit, int kk) {
		List<Kurssi> kurssit_return = new ArrayList();
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		for (Kurssi kurssi : kurssit) {
			try {
				String date = kurssi.getPaattymis_pvm().toString();
				java.util.Date ppvm = df.parse(date);

				Calendar c = Calendar.getInstance();
				c.add(Calendar.MONTH, -kk);
				java.util.Date superOpenKuukauttaSitten = c.getTime();
				if (ppvm.after(superOpenKuukauttaSitten)) {
					kurssit_return.add(kurssi);
				}
			} catch (NullPointerException ne) {
				//Jos ei asetettu kannassa päättymispäivämäärää
				kurssit_return.add(kurssi);
			} catch (ParseException ex) {
				//Jos kannassa vääränlainen päättymispvm (?ei mahdollista kai)
			}
		}
		return kurssit_return;
	}

	public static List<Kurssi> kurssitKayttajalle(String ruser) throws SQLException {
		Connection databaseConnection = DatabaseConnection.makeConnection();
		PreparedStatement preparedStatement = databaseConnection.prepareStatement(COURSE_INFOS);
		preparedStatement.setString(1, ruser);
		preparedStatement.setString(2, ruser);
		List<Kurssi> kurssit = SQLoader.loadTablesFromPreparedStatement(new Kurssi(), preparedStatement, databaseConnection);
		return filtteroiVanhatKurssitPois(kurssit, MONTHS_OPEN);
	}

	public static Kurssi kurssiIDlla(String s) throws SQLException {
		try {
			String[] sd = s.split("\\.");
			return kurssiIDlla(sd[0], sd[1], sd[2], sd[3], sd[4]);
		} catch (NullPointerException ne) {
			return null;
		} catch (ArrayIndexOutOfBoundsException ae) {
			return null;
		}
	}

	public static Kurssi kurssiIDlla(String kKoodi, String lKausi,
		String lVuosi, String tyyppi, String kNro) throws SQLException {

		List<Filter> f = new ArrayList();
		if (!kKoodi.equals("")) {
			f.add(new Filter(Kurssi.kurssikoodi, kKoodi));
		}
		if (!lKausi.equals("")) {
			f.add(new Filter(Kurssi.lukukausi, lKausi));
		}
		if (!lVuosi.equals("")) {
			try {
				f.add(new Filter(Kurssi.lukuvuosi, Integer.parseInt(lVuosi)));
			} catch (NumberFormatException e) {
			}
		}
		if (!tyyppi.equals("")) {
			f.add(new Filter(Kurssi.tyyppi, tyyppi));
		}
		if (!kNro.equals("")) {
			try {
				f.add(new Filter(Kurssi.kurssi_nro, Integer.parseInt(kNro)));
			} catch (NumberFormatException e) {
			}
		}
		List<Kurssi> kurssit = SQLoader.loadTable(new Kurssi(), f);
		if (kurssit.size() > 0) {
			return kurssit.get(0);
		} else {
			return null;
		}
	}

        /**
         * Metodi asettaa kurssille suorituspäivämäärän.
         * 
         * @param kurssi Kurssi, jolle suorituspäivämäärä asetetaan
         * @param suorituspvm Asetettava suorituspäivämäärä
         * @param request 
         */
	public static void asetaSuorituspvm(Kurssi kurssi, String suorituspvm, HttpServletRequest request) {
            HttpSession istunto = request.getSession();
            LocalisationBundle bundle = SessioApuri.bundle(request);
            try {
                Connection tietokantayhteys = DatabaseConnection.makeConnection();
		PreparedStatement valmisteltuLause = tietokantayhteys.prepareStatement(asetaSuorituspvm);
		valmisteltuLause.setDate(1, java.sql.Date.valueOf(suorituspvm));
		valmisteltuLause.setString(2, kurssi.getKurssikoodi());
		valmisteltuLause.setString(3, kurssi.getLukukausi());
		valmisteltuLause.setInt(4, kurssi.getLukuvuosi());
		valmisteltuLause.setString(5, kurssi.getTyyppi());
		valmisteltuLause.setInt(6, kurssi.getKurssi_nro());
		valmisteltuLause.executeUpdate();
		valmisteltuLause.close();
		tietokantayhteys.close();
            } catch (SQLException poikkeus) {
                String virhe = bundle.getString("tkvirhe") + ": " + poikkeus.getLocalizedMessage();
                SessioApuri.annaVirhe(istunto, virhe);
            }
		
	}

	public static void tallennaKantaan(Kurssi kurssi) throws SQLException {
		kurssi.update();
		SQLoader.tallennaKantaan(kurssi);
	}
}
