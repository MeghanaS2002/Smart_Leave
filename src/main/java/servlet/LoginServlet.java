package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.DBConnection;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
    	response.setContentType("text/html");

        PrintWriter out = response.getWriter();
    	
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            response.sendRedirect("login.jsp?error=1");
            return;
        }

        try {
        	Connection con = DBConnection.getConnection();

        	PreparedStatement ps =
        	        con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");

        	ps.setString(1, username);
        	ps.setString(2, password);

        	ResultSet rs = ps.executeQuery();

        	if (rs.next()) {
        	    String role = rs.getString("role");

        	    HttpSession session = request.getSession();
        	    session.setAttribute("username", username);
        	    session.setAttribute("role", role);
        	    session.setMaxInactiveInterval(30 * 60);

        	    if ("faculty".equalsIgnoreCase(role)) {
        	        response.sendRedirect("faculty_dashboard.jsp");
        	    } else if ("student".equalsIgnoreCase(role)) {
        	        response.sendRedirect("student_dashboard.jsp");
        	    } else {
        	        response.sendRedirect("admin_dashboard.jsp");
        	    }
        	} else {
        	    out.println("<html>");
        	    out.println("<body>");
        	    out.println("<script>");
        	    out.println("alert('Wrong Username or Password');");
        	    out.println("window.location.href='login.jsp';");
        	    out.println("</script>");
        	    out.println("</body>");
        	    out.println("</html>");
        	}

            rs.close();
        	ps.close();
        	con.close();
        	
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }
}
