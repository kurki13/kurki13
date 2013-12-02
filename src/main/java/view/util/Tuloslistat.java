/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.util;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import model.Kurssi;

/**
 *
 * @author mkctammi
 */
public class Tuloslistat {

    public Kohta getOpiskelijanumero() {
        return opiskelijanumero;
    }

    public Kohta getHarjoitustyot_eriteltyina() {
        return harjoitustyot_eriteltyina;
    }

    public Kohta getValikokeet_eriteltyina() {
        return valikokeet_eriteltyina;
    }

    public Kohta getLaskarit_summa() {
        return laskarit_summa;
    }

    public Kohta getHarjoitustyot_summa() {
        return harjoitustyot_summa;
    }

    public Kohta getKokeet_summa() {
        return kokeet_summa;
    }

    public Kohta getLaskarit_lisapisteet() {
        return laskarit_lisapisteet;
    }

    public Kohta getHarjoitustyot_lisapisteet() {
        return harjoitustyot_lisapisteet;
    }

    public Kohta getYhteispisteet() {
        return yhteispisteet;
    }

    public Kohta getArvosana() {
        return arvosana;
    }

    public Kohta getOpintopisteet() {
        return opintopisteet;
    }

    public Kohta getKieli() {
        return kieli;
    }

    public Kohta getTilastoja() {
        return tilastoja;
    }

    public Kohta getArvosanajakauma() {
        return arvosanajakauma;
    }

    public Kohta getPalautetilaisuus() {
        return palautetilaisuus;
    }

    public Kohta getAllekirjoitus() {
        return allekirjoitus;
    }

    public Kohta getHyvaksytyt() {
        return hyvaksytyt;
    }

    public Kohta getHylatyt() {
        return hylatyt;
    }

    public Kohta getJaadytyksenjalkeiset() {
        return jaadytyksenjalkeiset;
    }
    
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
            String r = Lokalisaatio.bundle(request).getString(otsikkodefault);
            if (r == null) {
                return "";
            }
            return r;
        }
        
        public boolean isActive() {
            String r = request.getParameter(checkboxname);
            if (r == null) {
                return false;
            }
            return r.equals("on");
        }
        
        public String getOtsikko() {
            String r = request.getParameter(otsikkoname);
            if (r == null) {
                return "";
            }
            return r;           
        }
    }
    
    List<Kohta> kohdat = new ArrayList();
    Kohta opiskelijanumero;
    Kohta harjoitustyot_eriteltyina;
    Kohta valikokeet_eriteltyina;
    Kohta laskarit_summa;
    Kohta harjoitustyot_summa;
    Kohta kokeet_summa;
    Kohta laskarit_lisapisteet;
    Kohta harjoitustyot_lisapisteet;
    Kohta yhteispisteet;
    Kohta arvosana;
    Kohta opintopisteet;
    Kohta kieli;
    Kohta tilastoja;
    Kohta arvosanajakauma;
    Kohta palautetilaisuus;
    Kohta allekirjoitus;
    Kohta hyvaksytyt;
    Kohta hylatyt;
    Kohta jaadytyksenjalkeiset;
    
    HttpServletRequest request;
    
    public List<Kohta> getKohdat() {
        return kohdat;
    }

    public void setRequest(HttpServletRequest request) {
        System.out.println("Setrequest called");
        this.request = request;
        Kurssi kurssi = SessioApuri.valittuKurssi(request);
        if (kurssi == null) {
            SessioApuri.annaVirhe(request.getSession(), "Kurssia ei valittu!");
            return;
        }
        setKurssi(kurssi);
    }
    
    private void setKurssi(Kurssi kurssi) {
        opiskelijanumero = new Kohta(
                (true),
                "inc_ssn",
                "opnro",
                "inc_opnroheader",
                "tl.opnro");
        harjoitustyot_eriteltyina = new Kohta(
                (kurssi.getHarjoitustyo_lkm() > 1),
                "inc_htsep",
                "htpsterit",
                "inc_htsepname",
                "tl.tyot");
        valikokeet_eriteltyina = new Kohta(
                (kurssi.getValikokeet_lkm() > 1),
                "inc_koesep",
                "koepsterit",
                "inc_koesepname",
                "tl.kokeet");
        laskarit_summa = new Kohta(
                (kurssi.getLaskarikerta_lkm() > 0),
                "inc_lhsum",
                "lhtpsum",
                "inc_lhsumname",
                "tl.lhsum");
        harjoitustyot_summa = new Kohta(
                (kurssi.getHarjoitustyo_lkm() > 0),
                "inc_htsum",
                "htpstsum",
                "inc_htsumname",
                "tl.htsum");
        kokeet_summa = new Kohta(
                (kurssi.getValikokeet_lkm() > 0),
                "inc_koesum",
                "koepstsum",
                "inc_koesumname",
                "tl.koesum");
        laskarit_lisapisteet = new Kohta(
                (kurssi.getLaskarikerta_lkm() > 0),
                "inc_lhlp",
                "lhlpst",
                "inc_lhlpname",
                "tl.lhlp");
        harjoitustyot_lisapisteet = new Kohta(
                (kurssi.getHarjoitustyo_lkm() > 0),
                "inc_htlp",
                "htlpst",
                "inc_htlpname",
                "tl.htlp");
        yhteispisteet = new Kohta(
                (true),
                "inc_sum",
                "yhtpst",
                "inc_sumname",
                "tl.yht");
        arvosana = new Kohta(
                (true),
                "inc_grade",
                "Arvosana",
                "inc_gradename",
                "tl.arvos");
        opintopisteet = new Kohta(
                (true),
                "inc_crunits",
                "Opintopisteet",
                "inc_cruname",
                "tl.op");
        kieli = new Kohta(
                (true),
                "inc_language",
                "kieli",
                "inc_languagename",
                "tl.kieli");
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
