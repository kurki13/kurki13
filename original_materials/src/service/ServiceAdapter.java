package service;

public class ServiceAdapter 
    extends ComparableOptionAdapter implements Service {

    protected int role = UNDEFINED_ROLE;

    /**
     **  Tämän toiminnon tunniste.
     */
    public String getId() { return (String)this.id; }


    /**
     ** Pienin oikeusluokka, joka on oikeutettu suorittamaan tämän
     **  toiminnon.
     */
    public int getRole() { return this.role; }

    
    /**
     **  Saako <tt>userInRole</tt> suorittaa tämän toiminnon?
     */
    public boolean isValidServiceFor( int userInRole ) {
	if ( userInRole != UNDEFINED_ROLE ) {
	    return userInRole >= this.role;
	}
	else {
	    try {
		return ServiceManager.getInstance().isValidServiceFor( this, userInRole );
	    } catch ( Exception e ) {
		return false;
	    }
	}
    }


    /**
     ** 
     */
    public ServiceAdapter( Service another ) {
	 
	this.id = another.getComparableId();
	this.desc = another.getDesc();
	this.role = another.getRole();
    }

    /**
     ** 
     */
    public ServiceAdapter( String id, int forRoleAndUp, String description )
	throws NullIdException {
	
	super( id, description );
	this.role = forRoleAndUp;
    }


    /**
     ** 
     */
    public ServiceAdapter( String id, String description )
	throws NullIdException {
	
	super( id, description );
	this.role = UNDEFINED_ROLE;
    }
}
