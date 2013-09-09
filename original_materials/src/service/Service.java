package service;

public interface Service extends ComparableOption {

    /**
     **  Rooli m��rittelem�t�n.
     */
    static final int UNDEFINED_ROLE = Integer.MIN_VALUE;

    String getId();

    /**
     **  Pienin rooli, jolle t�m� palvelu tarjotaan.
     */
    int getRole();


    /**
     **  Onko t�m� palvelu sallittu k�ytt�j�lle roolissa
     **  &lt;userInRole&gt;?
     */
    boolean isValidServiceFor( int userInRole );
}
