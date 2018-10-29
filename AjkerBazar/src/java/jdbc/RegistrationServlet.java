
package jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistrationServlet extends HttpServlet {

    Connection conn = null;
    int a = 0, c = 0;
    ResultSet rs;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String name = request.getParameter("uname");
        String pass = request.getParameter("psw");
        
        String query = "insert into UserInfo(name,password) values(?,?)";
        
        PrintWriter out = response.getWriter();

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/AB", "mukit", "mukit");

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, pass);
            a = ps.executeUpdate();

        } catch (ClassNotFoundException e) {
            //PrintWriter out = response.getWriter();
            out.println("Class Not found");
            e.printStackTrace();
        } catch (SQLException e) {
            out.println("<html>");
            out.println("<body>");
            out.println("<div align=center>");
            out.println("Username: "+name+" is already exits.");
            out.println("<br>");
            out.println("<h4>Try to register with a new username.</h4><br>");
            out.println("<a href=\"http://localhost:9090/AjkerBazar/registration.html\">Sign Up</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            e.printStackTrace();
        }

        if (a > 0) {
            //PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<body>");
            out.println("<div align=center>");
            out.println("Hello " + name + ", You are now a registered member of AjkerBazar.");
            out.println("<br>");
            out.println("<h4>Now Sign in to your account using your username and password</h4><br>");
            out.println("<a href=\"http://localhost:9090/AjkerBazar/login.html\">Sign In</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
