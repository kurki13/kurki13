package kurki;

import java.io.*;
import java.util.*;

/**
 * Tämä luokka lataa tiedostosta muistiin konfiguraatiotiedoston.
 */
public class Configuration {
    protected static Properties props = new Properties();
    protected static File pfile = null;
    protected static long lmod = 0;

    private Configuration() {}

    public static Object getProperty( String name ) {
	if ( name == null ) return null;
	loadProperties();
	return props.get( name );
    }
 
    public static boolean propertySet( String name ) {
	if ( name == null ) return false;
	return props.containsKey( name );
    }

    public static boolean setProperty( String name, Object value ) {
	if ( name == null || value == null ) return false;

	props.put( name, value );
	return true;
    }

    /**
     * Asettaa käytettävän konfiguraatiotiedoston.
     * @param file käytettävä tiedosto.
     */
    public static void setPropertiesFile( File file ) {
	if ( file != null && file.exists() ) {
	    pfile = file;
	    loadProperties();
	}
    }

    private static void loadProperties() {
	if ( pfile != null ) {
	    long lm = pfile.lastModified();

	    if ( lmod != lm ) {
		lmod = lm;
		Properties p = new Properties( props ); // vanhat defaulttina

		try {
		    FileInputStream fis = new FileInputStream( pfile );
		    p.load(fis);
		} catch ( Exception e ) {
		    e.printStackTrace();
		}
		props = p;
	    }
	}
    }
}
