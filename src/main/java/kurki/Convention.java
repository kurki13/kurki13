package kurki;

import kurki.util.LocalisationBundle;
import service.*;

public class Convention extends ComparableOptionAdapter{
    protected String helpTxt = "";
    
    public Convention( int id ) {
	this.id = new Integer( id );
	this.desc = "Not in use";
	this.helpTxt = "Not in use";
    }

    @Override
    public String getLabel() {
        return LocalisationBundle.getString("conventionLabel" + id);
    }
    
    
    
    public int getId() {
	return ((Integer)this.id).intValue();
    }

    public String getHelp() { return LocalisationBundle.getString("conventionHelp" + id); }
}
