/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.util;

import model.Kurssi;
import model.util.SQLoader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author mkctammi
 */
public class Lisapisterajat {

    public static void kasitteleLomake(Kurssi kurssi, HttpServletRequest request) {
        String[] ltrajats = request.getParameterValues("ltrajat");
        String[] htrajats = request.getParameterValues("htrajat");
        if (ltrajats == null) {
            ltrajats = new String[]{};
        }
        if (htrajats == null) {
            htrajats = new String[]{};
        }
        int[] ltrajat;
        int[] htrajat;

        try {
            ltrajat = stringArrayIntArrayksi(ltrajats);
            htrajat = stringArrayIntArrayksi(htrajats);
        } catch (NumberFormatException ne) {
            SessioApuri.annaVirhe(request.getSession(), Lokalisaatio.bundle(request).getString("lisap.sallitutArvotOvat") + "0-999");
            return;
        }

        String lisapisterajat = kolmenRyhmatStringiksi(ltrajat);
        String harjoitustyon_pisterajat = kolmenRyhmatStringiksi(htrajat);

        kurssi.setValue(Kurssi.lisapisterajat, lisapisterajat);
        kurssi.setValue(Kurssi.harjoitustyon_pisterajat, harjoitustyon_pisterajat);

        try {
            SQLoader.tallennaKantaan(kurssi);
        } catch (SQLException se) {
            SessioApuri.annaVirhe(request.getSession(), Lokalisaatio.bundle(request).getString("lisap.tietojenTallentamisessaVirhe") + se.getMessage());
        }
        SessioApuri.annaViesti(request.getSession(), Lokalisaatio.bundle(request).getString("lisap.muutoksetTallennettu"));
    }

    /**
     * Tää tekee lomakkeen parametrina saadusta stringarraysta int arrayn, ja
     * heittää numberformatexceptionin jos joku arvo ei sisälly väliin 0-999.
     *
     * @param stringArray
     * @return
     */
    private static int[] stringArrayIntArrayksi(String[] stringArray) {
        int[] intArray = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            intArray[i] = Integer.parseInt(stringArray[i]);
            if (intArray[i] < 0 || intArray[i] > 999) {
                throw new NumberFormatException();
            }
        }
        return intArray;
    }

    public static void laskeRajatUudestaan(Kurssi kurssi, HttpServletRequest request) {
        kurssi.setValue(Kurssi.lisapisterajat, "");
        kurssi.setValue(Kurssi.harjoitustyon_pisterajat, "");
        
        kurssi.setValue(Kurssi.lisapisterajat, kolmenRyhmatStringiksi(laskariRajat(kurssi)));
        kurssi.setValue(Kurssi.harjoitustyon_pisterajat, kolmenRyhmatStringiksi(htRajat(kurssi)));
        try {
            SQLoader.tallennaKantaan(kurssi);
        } catch (SQLException ex) {
            SessioApuri.annaVirhe(request.getSession(), Lokalisaatio.bundle(request).getString("lisap.tietojenTallentamisessaVirhe") + ex.getMessage());
            return;
        }
        SessioApuri.annaViesti(request.getSession(), Lokalisaatio.bundle(request).getString("lisap.uudelleenlaskettu"));
    }

    public static List<Integer> laskariRajat(Kurssi kurssi) {
        int monta = kurssi.getValue(Kurssi.max_laskaripisteet);
        float kartuntavali = kurssi.getValue(Kurssi.lisapisteiden_askelkoko);
        int ekanraja = kurssi.getValue(Kurssi.lisapistealaraja);
        List<Integer> asetetut_pisteet = jasenteleKolmenRyhmat(kurssi.getValue(Kurssi.lisapisterajat));

        return lisapisterajatKaavanMukaan(monta, kartuntavali, ekanraja, asetetut_pisteet);
    }

    public static List<Integer> htRajat(Kurssi kurssi) {
        int monta = kurssi.getValue(Kurssi.harjoitustyopisteet);
        float kartuntavali = kurssi.getValue(Kurssi.harjoitustoiden_askelkoko);
        int ekanraja = kurssi.getValue(Kurssi.ht_lisapistealaraja);
        List<Integer> asetetut_pisteet = jasenteleKolmenRyhmat(kurssi.getValue(Kurssi.harjoitustyon_pisterajat));

        return lisapisterajatKaavanMukaan(monta, kartuntavali, ekanraja, asetetut_pisteet);
    }

    private static List<Integer> lisapisterajatKaavanMukaan(int monta, float kartuntavali, int ekanraja, List<Integer> asetetut_pisteet) {
        List<Integer> ret = new ArrayList();
        for (int i = 0; i < monta; i++) {
            if (i < asetetut_pisteet.size()) {
                ret.add(asetetut_pisteet.get(i));
            } else {
                ret.add((int) (ekanraja + i * kartuntavali));
            }
        }
        return ret;
    }

    private static ArrayList<Integer> jasenteleKolmenRyhmat(String p) {
        if (p == null) {
            p = "";
        }
        ArrayList<Integer> ret = new ArrayList();
        for (int i = 0; i + 3 <= p.length(); i += 4) {
            try {
                ret.add(Integer.parseInt(p.substring(i, i + 3).trim()));
            } catch (NumberFormatException ne) {
                ret.add(0);
            }
        }
        return ret;
    }

    private static String kolmenRyhmatStringiksi(List<Integer> l) {
        int[] ints = new int[l.size()];
        int i = 0;
        for (Integer integer : l) {
            ints[i] = integer;
            i++;
        }
        return kolmenRyhmatStringiksi(ints);
    }

    private static String kolmenRyhmatStringiksi(int[] l) {
        StringBuilder ret = new StringBuilder();
        for (Integer i : l) {
            if (i > 999) {
                i = 999;
            }
            if (i < 0) {
                i = 0;
            }
            String a = i.toString();
            for (int j = a.length(); j < 3; j++) {
                ret.append(" ");
            }
            ret.append(a).append(",");
        }
        if (ret.length() > 0) {
            ret.deleteCharAt(ret.length() - 1);
        }
        return ret.toString();
    }

    public static void main(String[] args) {
        String t = "  5,105,205,305,405,505,605,705,805,905,100,110,120,130,140,150,160,170,180,190,200,210,220,230,240,250,260,270,280,290,300,310,320,330,340,350,360,370,380,390,400,410,420,430,440,450,460,470,480,490,500,510,520,530,540";
        ArrayList<Integer> k = jasenteleKolmenRyhmat(t);
        System.out.println(Arrays.toString(k.toArray()));
        System.out.println(kolmenRyhmatStringiksi(k).equals(t));
        System.out.println(kolmenRyhmatStringiksi(new int[]{-5050, 99999, 564065, 1}));
    }
}
