package service;

public abstract class ComparableOptionAdapter implements ComparableOption {
    protected Comparable id;
    protected String desc;

    protected ComparableOptionAdapter() {}

    public ComparableOptionAdapter( Comparable id, String description )
	throws NullIdException {

	if ( id == null ) { throw new NullIdException(); }

	this.id = id;
	this.desc = description;
    }

    public ComparableOptionAdapter( ComparableOption clone ) {
	this.id = clone.getComparableId();
	this.desc = clone.getDesc();
    }
 
    public String getLabel() { return this.desc; }

    public String getValue() { return ""+this.id; }

    public String getDesc() { return this.desc; }

    public Comparable getComparableId() { return this.id; }

    public boolean equals(Object another) {
	if ( another != null ) 
	    return this.compareTo( another ) == 0;
	else
	    return false;
    }

    public int compareTo( Object another ) {
	return this.getComparableId().compareTo( ((ComparableOption)another).getComparableId() );
     }

    public String toString() {
	return this.desc + " ("+this.id+")";
    }
}
