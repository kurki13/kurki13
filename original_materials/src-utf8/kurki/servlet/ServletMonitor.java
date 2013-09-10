package kurki.servlet;
import javax.servlet.*;
import javax.servlet.http.*;

public class ServletMonitor {
    private static ServletMonitor mon = new ServletMonitor();
    private static final String LOCK = "lock";

    private ServletMonitor() {}

    public static ServletMonitor getMonitor() {
	return mon;
    }

    public synchronized boolean lock( HttpSession session ) {
	try {
	    while ( session.getAttribute( LOCK ) != null ) {
		try {
		    wait();
		} catch (InterruptedException e) {}
	    }
	    session.setAttribute( LOCK, "lock" );
	    return true;
	} catch ( IllegalStateException ise ) {
	    return false;
	}
    }

    public synchronized boolean unlock( HttpSession session ) {
	try {
	    session.removeAttribute( LOCK );
	    return true;
	}catch ( IllegalStateException ise ) {
	    return false;
	} finally {
	    notifyAll();
	}
    }
}
