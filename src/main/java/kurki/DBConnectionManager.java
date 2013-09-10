package kurki;

import java.io.*;
import java.util.*;
import java.sql.*;

public class DBConnectionManager 
    implements Serializable {

    public static Connection createConnection() throws SQLException, ClassNotFoundException {
	Class.forName( (String)Configuration.getProperty( "dbDriver" ) );

	Connection con = DriverManager.getConnection( (String)Configuration.getProperty( "dbServer" ),
						      (String)Configuration.getProperty( "dbUser" ),
						      (String)Configuration.getProperty( "dbPassword" ) );
	if ( con == null )
	    throw new SQLException("Tietokantayhteyden luominen ei onnistunut.");
	else
	    return con;
    }

    public static void closeConnection( Connection con ) {
	try {
	    con.close();
	} catch ( SQLException e ) {}
    }
}
