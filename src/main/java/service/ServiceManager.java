package service;

import service.exception.UndefinedRoleException;
import service.exception.ServiceAlreadyDefinedException;
import service.exception.NullIdException;
import service.exception.ServicesLockedException;
import service.exception.ServicesNotLockedException;
import java.util.*;
import java.util.Map.Entry;

public class ServiceManager {

    /**
     **  Järjestelmän ainoa tämän luokan ilmentymä.
     */ 
    private static ServiceManager instance = null;


    /**
     **  Onko kaikki tarjottavat palvelut jo määritelty.
     */
    private static boolean locked = false;

    /**
     **  Roolien oletusarvoinen lkm (1).
     */
    protected static final int DEF_NO_OF_ROLES = 1;

    /**
     **  Tunnistettujen roolien lkm.
     */ 
    private static int noOfRoles = DEF_NO_OF_ROLES;

    /**
     ** Kaikki mahdolliset toiminnot.
     */
    private static Hashtable<String, ServiceAdapter> services = new Hashtable();

    /**
     **  Toiminnot jaettuna roolien mukaan.
     */
    protected static final Vector servicesByRoles = new Vector();


    /**
     **  Luo identifioimattoman toiminnon, jonka avulla voidaan
     **  esim. hakea tunnettu (ja identifioitu) toiminto.
     */
    public static Service createFakeService( String id )
	throws NullIdException {

	return new ServiceAdapter( id, "match" );
    }


    /**
     **  Määrittelee tunnetun toiminnon, joka on identifioitu tietylle
     **  oikeusluokalle (käyttäjäryhmälle) ja tätä ylemmille.
     * @param id
     * @param lowest_role_to_allow Pienin roolinumero joka saa käyttää tätä palvelua
     * @param description
     * @throws service.exception.NullIdException
     * @throws service.exception.ServiceAlreadyDefinedException
     * @throws service.exception.ServicesLockedException
     * @throws service.exception.UndefinedRoleException
     */
    public static void defineService( String id, int lowest_role_to_allow, String description )
	throws NullIdException,
	ServiceAlreadyDefinedException,
	ServicesLockedException,
	UndefinedRoleException {


	/*
	 *  Tarvittavat tarkistukset...
	 */
	if ( locked )
	    throw new ServicesLockedException();

	else if ( lowest_role_to_allow < 0 || lowest_role_to_allow >= noOfRoles ) 
	    throw new UndefinedRoleException();

	else if ( id == null )
	    throw new NullIdException();

	else if ( services.containsKey( id ) )
	    throw new ServiceAlreadyDefinedException(); 

	ServiceAdapter newService = new ServiceAdapter( id, lowest_role_to_allow, description );

	services.put( id, newService );
    }

    /**
     **  Ainoa ilmentymä.
     * @return 
     * @throws service.exception.ServicesNotLockedException
     */
    public static ServiceManager getInstance() throws ServicesNotLockedException {
	if ( !locked ) throw new ServicesNotLockedException();
	return instance;
    }

    /**
     **  Määriteltyjen Palveluiden lkm.
     * @return 
     */
    public static int getNoOfServices() {
	return services.size();
    }

    
    /**
     **  Toiminnon <tt>id</tt> perustiedot.
     * @param id
     * @return 
     */
    public Service getService( String id ) {
	
	if ( id == null ) return null;
	else return (ServiceAdapter)services.get( id );
    }
    

    /**
     **  Tietylle käyttäjäryhmälle tarjotut palvelut
     * @param role
     * @return 
     */
    public Service[] getValidServicesFor( int role ) {
	
	if ( role < 0 || role >= noOfRoles ) { return null; }
	else {
	    return (ServiceAdapter[])servicesByRoles.get( role );
	}
    }

    

    /**
     **  Tätä kutsutaan kun kaikki mahdolliset toiminnot on määritelty.
     **  Tämän jälkeen uusia toimintoja tai korkeinta oikeusluokkaa
     **  ei voi enää määritellä.
     */
    public static void lockServices() 
	throws ServicesLockedException {
	
	if ( locked ) throw new ServicesLockedException();
	
	locked = true;

	/*
	 *  Jaetaan toiminnot roolien mukaisesti
	 */

	// alustetaan servicesByRoles
	for ( int i = 0; i < noOfRoles; i++ ) {
	    servicesByRoles.add( i,  new Vector() );
	}

	// Lisätään rooleille näiden mukaiset oikeudet
	Enumeration acts = services.elements();
	Service act;

	while ( acts.hasMoreElements() ) {
	    act = (Service)acts.nextElement();

	    // Lisätään oikeus kaikille, joilla on yhtäkovat
	    // tai kovemmat oikeudet
	    for ( int i = act.getRole(); i < noOfRoles; i++ ) {
		((Vector)servicesByRoles.get( i )).add( act );
	    }
	}

	// muutetaan roolien mukaiset oikeudet Vectoreista taulukoiksi
	for ( int i = 0; i < noOfRoles; i++ ) {
	    Vector vec = (Vector)servicesByRoles.get(i);
	    Service[] ait = (ServiceAdapter[])vec.toArray( new ServiceAdapter[vec.size()] );
	    Arrays.sort(ait);

	    // Muutetaan Vectori taulukoksi myös servicesByRoles -
	    // tietorakenteessa.
	    servicesByRoles.set( i, ait ); 
	}

	instance = new ServiceManager();
    }

    /**
     **  Onko palvelu act luvallinen roolille role?
     * @param act
     * @param role
     * @return 
     */
    public boolean isValidServiceFor( Service act, int role ) {
	
	ServiceAdapter[] serv = (ServiceAdapter[])servicesByRoles.get( role );
	int found = Arrays.binarySearch( serv, act );
	
	return found >= 0;
    }


    /**
     * Asetetaan roolien yhteislukumäärä
     * @param nor
     * @throws ServicesLockedException 
     */
    public static void setNoOfRoles( int nor )
	throws ServicesLockedException {
	
	if ( locked ) throw new ServicesLockedException();
	
	noOfRoles = nor;
    } 

    @Override
    public String toString() {
        String ret = "There are " + services.size() + " entries in the ServiceManager: <br>";
        Iterator<Entry<String,ServiceAdapter>> eit = services.entrySet().iterator();
        int count = 0;
        while(eit.hasNext()) {
            count++;
            Entry<String,ServiceAdapter> entry = eit.next();
            ServiceAdapter service = entry.getValue();
            ret += " " + count + ". Key in Hashtable: " + entry.getKey() + 
                    " || Description: " + service.getDesc() + 
                    " || Id: " + service.getId() +
                    " || Label: " + service.getLabel() +
                    " || Value: " + service.getValue() +
                    " || Smallest Role number: " + service.role + 
                    "<br>";
        }
        return ret;
    }
    
    
}
