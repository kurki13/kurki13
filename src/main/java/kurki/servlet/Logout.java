package kurki.servlet;


import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Logout extends HttpServlet {

    public void service( HttpServletRequest req, HttpServletResponse res )
	throws ServletException, IOException {
	HttpSession s = req.getSession();
	s.invalidate();
	res.sendRedirect("../logout.vm");
    }
}