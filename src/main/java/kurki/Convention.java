package kurki;

import kurki.util.LocalisationBundle;
import service.*;

public class Convention extends ComparableOptionAdapter{
    protected String helpTxt = "";
    
    public Convention( int id, String desc, String helpTxt ) {
	this.id = new Integer( id );
	this.desc = desc;
	this.helpTxt = helpTxt;
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
