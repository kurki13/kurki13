/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.util;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import model.Osallistuminen;
import static view.util.Suoritukset.isInteger;

/**
 *
 * @author tkairola
 */
public class Filter {

    private static ArrayList<Osallistuminen> filteroidytOsallistumiset = new ArrayList<Osallistuminen>();

    public static void filterointi(HttpServletRequest request, String ryhma, String filterString, List<Osallistuminen> osallistumiset) {
        filteroidytOsallistumiset.clear();
        // Jos filterissä on ylipäätään tavaraa
        if (!filterString.isEmpty()) {
            String f = filterString.replaceAll("\\s", "");
            f = f.toLowerCase();
            String[] filterit = f.split(",");
            for (Osallistuminen os : osallistumiset) {
                if (os.getVoimassa().equals("K")) {
                    filteriApuri(request, filterit, os, ryhma);
                }
            }
        } //Jos filter on tyhjä, mutta ryhmän numero on annettu
        else if (filterString.isEmpty() && isInteger(ryhma)) {
            for (Osallistuminen os : osallistumiset) {
                if (os.getVoimassa().equals("K") && os.getIlmo_jnro() == Integer.parseInt(ryhma)) {
                    filteroidytOsallistumiset.add(os);
                }
            }
        } //Jos filter ja ryhmä ovat tyhjiä, filteröidään pois vain ne,
        //joiden osallistuminen ei ole voimassa
        else {
            for (Osallistuminen os : osallistumiset) {
                if (os.getVoimassa().equals("K")) {
                    filteroidytOsallistumiset.add(os);
                }
            }
        }
    }

    /*
     * Filteröidään osallistujat ryhmän mukaan, sekä annettujen
     * filteröintiparametrien mukaan.
     */
    private static void filteriApuri(HttpServletRequest request, String[] filterit, Osallistuminen os, String ryhma) {
        System.out.println("testii2"+onkoRyhmassa(ryhma, os));
        int hetu = 0;
        int nimi = 0;
        int joukossa = 0;
        System.out.println(filterit.toString());
        for (String filter : filterit) {
            System.out.println("f"+filter);
            if (onkoRyhmassa(ryhma, os)) {
                if (isInteger(filter)) {
                    if (os.getHetu().toLowerCase().startsWith(filter)) {
                        hetu = 1;
                        System.out.println("hetu");
                    }
                } else {
                    if (filter.contains("..")) {
                        if (kuuluukoSukunimiJoukkoon(request, filter, os.getSukunimi())) {
                            joukossa = 1;
                        }
                    } else if (os.getSukunimi().toLowerCase().startsWith(filter)) {
                        System.out.println("nimi");
                        nimi = 1;
                    }
                }
            }
        }
        int osumat = hetu + nimi + joukossa;
        if (osumat == filterit.length) {
            filteroidytOsallistumiset.add(os);
        }
    }

    /*
     * Tarkistaa kuuluuko sukunimi tiettyyn joukkoon. Parametrinä tulee
     * filter-merkkijono joka on esim. muotoa "air..sark", jolloin
     * tämä metodi palauttaa truen mikäli annettu sukunimi kuuluu
     * aakkosjärjestyksessä tähän annettuun väliin.
     */
    private static boolean kuuluukoSukunimiJoukkoon(HttpServletRequest request, String filter, String sukunimi) {
        String[] rajat = filter.split("\\.\\.");
        String alku = "";
        String loppu = "";
        if (rajat.length == 2) {
            alku = rajat[0];
            loppu = rajat[1];
            if (alku.compareToIgnoreCase(sukunimi.substring(0, alku.length())) <= 0
                    && loppu.compareToIgnoreCase(sukunimi.substring(0, loppu.length())) >= 0) {
                return true;
            } else {
                return false;
            }
        } else {
            SessioApuri.annaVirhe(request.getSession(), "Virheellinen filteröintiparametri " + filter);
            return false;
        }
    }

    private static boolean onkoRyhmassa(String ryhma, Osallistuminen os) {
        //jos ryhmä on valittu
        if (isInteger(ryhma)) {
            if (os.getIlmo_jnro() == Integer.parseInt(ryhma)) {
                return true;
            } else {
                return false;
            }
            //jos haetaan kaikista ryhmistä
        } else {
            return true;
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static ArrayList<Osallistuminen> getFilteroidyt() {
        return filteroidytOsallistumiset;
    }
}
