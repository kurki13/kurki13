package service;

public interface Service extends ComparableOption {

    /**
     **  Rooli määrittelemätön.
     */
    static final int UNDEFINED_ROLE = Integer.MIN_VALUE;

    String getId();

    /**
     **  Pienin rooli, jolle tämä palvelu tarjotaan.
     */
    int getRole();


    /**
     **  Onko tämä palvelu sallittu käyttäjälle roolissa
     **  &lt;userInRole&gt;?
     */
    boolean isValidServiceFor( int userInRole );
}
