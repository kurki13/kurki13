package service;

import kurki.servicehandlers.AbstractVelocityServiceProvider;
import service.exception.NullIdException;

public class ServiceAdapter 
    extends ComparableOptionAdapter implements Service {

    protected int role = UNDEFINED_ROLE;
    AbstractVelocityServiceProvider handler;
    @Override
    public String getId() { return (String)this.id; }
    
    @Override
    public int getLowestRole() { return this.role; }
    @Override
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
    
    public ServiceAdapter( String id, int forRoleAndUp, String description, AbstractVelocityServiceProvider handler )
	throws NullIdException {
	
	super( id, description );
        this.handler = handler;
	this.role = forRoleAndUp;
    }

    @Override
    public AbstractVelocityServiceProvider getHandler() {
        return handler;
    }
}
