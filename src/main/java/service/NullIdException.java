package service;

public class NullIdException extends Exception {
    public NullIdException(){ super(); }
    public NullIdException( String msg ){ super( msg ); }
}
