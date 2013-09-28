package kurki.util;

import java.io.*;
import java.util.*;

/**
 * Tämä luokka lataa tiedostosta muistiin konfiguraatiotiedoston.
 */
public class Configuration {

    protected static Properties props = new Properties();
    protected static File pfile = null;
    protected static long lmod = 0;
    private static ArrayList<String> superUsers = new ArrayList();

    private Configuration() {
    }

    public static Object getProperty(String name) {
        if (name == null) {
            return null;
        }
        loadProperties();
        return props.get(name);
    }

    public static boolean propertySet(String name) {
        if (name == null) {
            return false;
        }
        return props.containsKey(name);
    }

    public static boolean setProperty(String name, Object value) {
        if (name == null || value == null) {
            return false;
        }

        props.put(name, value);
        return true;
    }

    private static void buildSuperUsersList() {
        StringTokenizer st = new StringTokenizer(props.getProperty("superUsers"), ",");
        while (st.hasMoreTokens()) {
            String login = st.nextToken().trim();
            if (login.length() > 0) {
                superUsers.add(login);
            }
        }
    }

    public static ArrayList<String> getSuperUsers() {
        return superUsers;
    }

    /**
     * Asettaa käytettävän konfiguraatiotiedoston.
     *
     * @param file käytettävä tiedosto.
     */
    public static void setPropertiesFile(File file) {
        if (file != null && file.exists()) {
            pfile = file;
            loadProperties();
        }
    }

    private static void loadProperties() {
        if (pfile != null) {
            long lm = pfile.lastModified();

            if (lmod != lm) {
                lmod = lm;
                Properties p = new Properties(props); // vanhat defaulttina

                try {
                    FileInputStream fis = new FileInputStream(pfile);
                    p.load(fis);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                props = p;
            }
        }
        buildSuperUsersList();
    }
}
