package jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginServlet extends HttpServlet {
    Connection conn=null;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String name = request.getParameter("uname");
        String pass = request.getParameter("psw");
        String query = "select * from UserInfo where name=? and password=?";
        String dbName="";
        String dbPass="";
        try(PrintWriter out = response.getWriter()){
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/AB","mukit","mukit");
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, pass);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                dbName=rs.getString(2);
                dbPass=rs.getString(3);
            }
            HttpSession session = request.getSession();
            if(name.equals("admin") && pass.equals("admin"))
            {
                RequestDispatcher rd = request.getRequestDispatcher("./AdminPage");
                rd.forward(request, response);
            }
            else if(name.equals(dbName) && pass.equals(dbPass))
            {
                session.setAttribute("name", name);
                RequestDispatcher rd = request.getRequestDispatcher("./Home");
                rd.forward(request, response);

                
            }else{
                RequestDispatcher rd = request.getRequestDispatcher("login.html");
                rd.include(request, response);
                out.println("<html>");
                out.println("<body>");
                out.println("<h4 align=center>Incorrect Username or Password.</h4>");
                out.println("</body>");
                out.println("</html>");
            }
            
        }catch (ClassNotFoundException e) {
            PrintWriter out = response.getWriter();
            out.println("Class Not found");
            e.printStackTrace();
        }catch (SQLException e) {
            PrintWriter out = response.getWriter();
            out.println("Connection Failed");
            e.printStackTrace();
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
