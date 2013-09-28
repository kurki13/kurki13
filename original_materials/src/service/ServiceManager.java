package service;

import java.util.*;

public class ServiceManager {

    /**
     **  J‰rjestelm‰n ainoa t‰m‰n luokan ilmentym‰.
     */ 
    private static ServiceManager instance = null;


    /**
     **  Onko kaikki tarjottavat palvelut jo m‰‰ritelty.
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
    private static Hashtable services = new Hashtable();

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
     **  M‰‰rittelee tunnetun toiminnon, joka on identifioitu tietylle
     **  oikeusluokalle (k‰ytt‰j‰ryhm‰lle) ja t‰t‰ ylemmille.
     */
    public static void defineService( String id, int forRoleAndUp, String description )
	throws NullIdException,
	ServiceAlreadyDefinedException,
	ServicesLockedException,
	UndefinedRoleException {


	/*
	 *  Tarvittavat tarkistukset...
	 */
	if ( locked )
	    throw new ServicesLockedException();

	else if ( forRoleAndUp < 0 || forRoleAndUp >= noOfRoles ) 
	    throw new UndefinedRoleException();

	else if ( id == null )
	    throw new NullIdException();

	else if ( services.containsKey( id ) )
	    throw new ServiceAlreadyDefinedException(); 

	ServiceAdapter newService = new ServiceAdapter( id, forRoleAndUp, description );

	services.put( id, newService );
    }

    /**
     **  Ainoa ilmentym‰.
     */
    public static ServiceManager getInstance() throws ServicesNotLockedException {
	if ( !locked ) throw new ServicesNotLockedException();
	
	return instance;
    }

    /**
     **  M‰‰riteltyjen Palveluiden lkm.
     */
    public static int getNoOfServices() {
	return services.size();
    }

    
    /**
     **  Toiminnon <tt>id</tt> perustiedot.
     */
    public Service getService( String id ) {
	
	if ( id == null ) return null;
	else return (ServiceAdapter)services.get( id );
    }
    

    /**
     **  Tietyn k‰ytt‰j‰ryhm‰n p‰ivitysoikeudet.
     */
    public Service[] getValidServicesFor( int role ) {
	
	if ( role < 0 || role >= noOfRoles ) { return null; }
	else {
	    return (ServiceAdapter[])servicesByRoles.get( role );
	}
    }

    

    /**
     **  T‰t‰ kutsutaan kun kaikki mahdolliset toiminnot on m‰‰ritelty.
     **  T‰m‰n j‰lkeen uusia toimintoja tai korkeinta oikeusluokkaa
     **  ei voi en‰‰ m‰‰ritell‰.
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

	// Lis‰t‰‰n rooleille n‰iden mukaiset oikeudet
	Enumeration acts = services.elements();
	Service act;

	while ( acts.hasMoreElements() ) {
	    act = (Service)acts.nextElement();

	    // Lis‰t‰‰n oikeus kaikille, joilla on yht‰kovat
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

	    // Muutetaan Vectori taulukoksi myˆs servicesByRoles -
	    // tietorakenteessa.
	    servicesByRoles.set( i, ait ); 
	}

	instance = new ServiceManager();
    }

    /**
     **  Onko palvelu &lt;act&gt; luvallinen roolille &lt;role&gt;?
     */
    public boolean isValidServiceFor( Service act, int role ) {
	
	ServiceAdapter[] services = (ServiceAdapter[])servicesByRoles.get( role );
	int found = Arrays.binarySearch( services, act );
	
	return found >= 0;
    }


    public static void setNoOfRoles( int nor )
	throws ServicesLockedException {
	
	if ( locked ) throw new ServicesLockedException();
	
	noOfRoles = nor;
    } 
}
