package service;

public interface ComparableOption extends Comparable {

    @Override
    public int compareTo( Object another );

    @Override
    public boolean equals(Object another);

    public Comparable getComparableId();

    public String getDesc();

    public String getLabel();

    public String getValue();
}
