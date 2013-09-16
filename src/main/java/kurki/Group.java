package kurki;

import service.exception.NullIdException;
import service.*;

import java.util.*;
import java.io.Serializable;

public class Group extends ComparableOptionAdapter 
    implements Serializable {

    protected String lname = null;

    public Group( int id, String desc ) throws NullIdException {
	super( new Integer(id), desc );
    }

    public int getId() { return ((Integer)this.id).intValue(); }
}
