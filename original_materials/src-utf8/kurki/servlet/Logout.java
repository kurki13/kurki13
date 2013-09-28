package kurki.servlet;

import kurki.*;
import service.*;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.URLEncoder;

public class Logout extends HttpServlet {

    public void service( HttpServletRequest req, HttpServletResponse res )
	throws ServletException, IOException {
	HttpSession s = req.getSession();
	s.invalidate();
	res.sendRedirect("../logout.html");
    }
}
