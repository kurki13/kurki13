/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.util;

import java.util.ArrayList;
import java.util.List;
import model.Kurssi;

/**
 *
 * @author mkctammi
 */
public class Tuloslistat {

    public class Kohta {

        boolean ehto;
        String checkboxname;
        String bundlekey;
        String otsikkoname;
        String otsikkodefault; // tää kandeis varmaa kans vaihtaa bundleen

        private Kohta(boolean ehto, String checkboxname, String bundlekey, String otsikkoname, String otsikkodefault) {
            this.ehto = ehto;
            this.checkboxname = checkboxname;
            this.bundlekey = bundlekey;
            this.otsikkoname = otsikkoname;
            this.otsikkodefault = otsikkodefault;
            kohdat.add(this);
        }

        /**
         * @return the ehto
         */
        public boolean isEhto() {
            return ehto;
        }

        /**
         * @return the checkboxname
         */
        public String getCheckboxname() {
            return checkboxname;
        }

        /**
         * @return the bundlekey
         */
        public String getBundlekey() {
            return bundlekey;
        }

        /**
         * @return the otsikkoname
         */
        public String getOtsikkoname() {
            return otsikkoname;
        }

        /**
         * @return the otsikkodefault
         */
        public String getOtsikkodefault() {
            return otsikkodefault;
        }
    }
    List<Kohta> kohdat = new ArrayList();
    
    private Kohta opiskelijanumero;
    private Kohta harjoitustyot_eriteltyina;
    private Kohta valikokeet_eriteltyina;
    private Kohta laskarit_summa;
    private Kohta harjoitustyot_summa;
    private Kohta kokeet_summa;
    private Kohta laskarit_lisapisteet;
    private Kohta harjoitustyot_lisapisteet;
    private Kohta yhteispisteet;
    private Kohta arvosana;
    private Kohta opintopisteet;
    private Kohta kieli;
    private Kohta tilastoja;
    private Kohta arvosanajakauma;
    private Kohta palautetilaisuus;
    private Kohta allekirjoitus;
    private Kohta hyvaksytyt;
    private Kohta hylatyt;
    private Kohta jaadytyksenjalkeiset;

    public List<Kohta> getKohdat(Kurssi kurssi) {
        setKurssi(kurssi);
        return kohdat;
    }

    public void setKurssi(Kurssi kurssi) {
        opiskelijanumero = new Kohta(
                (true),
                "inc_ssn",
                "opnro",
                null,
                null);
        harjoitustyot_eriteltyina = new Kohta(
                (kurssi.getHarjoitustyo_lkm() > 1),
                "inc_htsep",
                "htpsterit",
                "inc_htsepname",
                "Työt");
        valikokeet_eriteltyina = new Kohta(
                (kurssi.getValikokeet_lkm() > 1),
                "inc_koesep",
                "koepsterit",
                "inc_koesepname",
                "Kokeet");
        laskarit_summa = new Kohta(
                (kurssi.getLaskarikerta_lkm() > 0),
                "inc_lhsum",
                "lhtpsum",
                "inc_lhsumname",
                "Laskarit");
        harjoitustyot_summa = new Kohta(
                (kurssi.getHarjoitustyo_lkm() > 0),
                "inc_htsum",
                "htpstsum",
                "inc_htsumname",
                "Ht?");
        kokeet_summa = new Kohta(
                (kurssi.getValikokeet_lkm() > 0),
                "inc_koesum",
                "koepstsum",
                "inc_koesumname",
                "Koe_Sum");
        laskarit_lisapisteet = new Kohta(
                (kurssi.getLaskarikerta_lkm() > 0),
                "inc_lhlp",
                "lhlpst",
                "inc_lhlpname",
                "Lask_lisäp");
        harjoitustyot_lisapisteet = new Kohta(
                (kurssi.getHarjoitustyo_lkm() > 0),
                "inc_htlp",
                "htlpst",
                "inc_htlpname",
                "Ht_lisäp");
        yhteispisteet = new Kohta(
                (true),
                "inc_sum",
                "yhtpst",
                "inc_sumname",
                "Yhteisp.");
        arvosana = new Kohta(
                (true),
                "inc_grade",
                "Arvosana",
                "inc_gradename",
                "Arvos");
        opintopisteet = new Kohta(
                (true),
                "inc_crunits",
                "Opintopisteet",
                "inc_cruname",
                "Opintopisteet");
        kieli = new Kohta(
                (true),
                "inc_language",
                "kieli",
                "inc_languagename",
                "kieli");
        tilastoja = new Kohta(
                (true),
                "inc_statistics",
                "tilastoja",
                null,
                null);
        arvosanajakauma = new Kohta(
                (true),
                "inc_gradeDistr",
                "arvosanajakauma",
                null,
                null);
        palautetilaisuus = new Kohta(
                (true),
                "inc_feedback",
                "palautetilaisuus",
                null,
                null);
        allekirjoitus = new Kohta(
                (true),
                "inc_signature",
                "allekirj",
                null,
                null);
        hyvaksytyt = new Kohta(
                (true),
                "inc_accepted",
                "hyvaksytyt",
                null,
                null);
        hylatyt = new Kohta(
                (true),
                "inc_failed",
                "isohylatyt",
                null,
                null);
        jaadytyksenjalkeiset = new Kohta(
                (kurssi.getTila().equals("J")),
                "inc_changes",
                "jaadytyksenJalkeiset",
                null,
                null);
    }
}
