package service;

public interface ComparableOption extends Comparable, Option {

    public int compareTo( Object another );

    public boolean equals(Object another);

    public Comparable getComparableId();

    public String getDesc();

    public String getLabel();

    public String getValue();
}
