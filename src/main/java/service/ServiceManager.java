package service;

import java.io.Serializable;
import service.exception.UndefinedRoleException;
import service.exception.ServiceAlreadyDefinedException;
import service.exception.NullIdException;
import service.exception.ServicesLockedException;
import service.exception.ServicesNotLockedException;
import java.util.*;
import java.util.Map.Entry;
import kurki.servicehandlers.AbstractVelocityServiceProvider;

public class ServiceManager implements Serializable {
    
    private static ServiceManager instance = null;
    private static boolean locked = false;
    protected static final int DEF_NO_OF_ROLES = 1;
    private static int noOfRoles = DEF_NO_OF_ROLES;
    private static Hashtable<String, ServiceAdapter> services = new Hashtable();
    protected static final Vector servicesByRoles = new Vector();
    public static void defineService( String id, int lowest_role_to_allow, String description, AbstractVelocityServiceProvider handler )
	throws NullIdException,
	ServiceAlreadyDefinedException,
	ServicesLockedException,
	UndefinedRoleException {
	if ( locked )
	    throw new ServicesLockedException();

	else if ( lowest_role_to_allow < 0 || lowest_role_to_allow >= noOfRoles ) 
	    throw new UndefinedRoleException();

	else if ( id == null )
	    throw new NullIdException();

	else if ( services.containsKey( id ) )
	    throw new ServiceAlreadyDefinedException(); 

	ServiceAdapter newService = new ServiceAdapter( id, lowest_role_to_allow, description, handler );

	services.put( id, newService );
    }
    public static ServiceManager getInstance() throws ServicesNotLockedException {
	if ( !locked ) throw new ServicesNotLockedException();
	return instance;
    }
    public static int getNoOfServices() {
	return services.size();
    }
    public Service getService( String id ) {
	
	if ( id == null ) return null;
	else return (ServiceAdapter)services.get( id );
    }
    
    public Service[] getValidServicesFor( int role ) {
	
	if ( role < 0 || role >= noOfRoles ) { return null; }
	else {
	    return (ServiceAdapter[])servicesByRoles.get( role );
	}
    }
    
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
	    for ( int i = act.getLowestRole(); i < noOfRoles; i++ ) {
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
    
    public boolean isValidServiceFor( Service act, int role ) {
	
	ServiceAdapter[] serv = (ServiceAdapter[])servicesByRoles.get( role );
	int found = Arrays.binarySearch( serv, act );
	
	return found >= 0;
    }
    
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
