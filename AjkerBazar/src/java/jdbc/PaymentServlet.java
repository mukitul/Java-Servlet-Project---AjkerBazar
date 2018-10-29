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

public class PaymentServlet extends HttpServlet {

    String userName;
    Connection conn;
    ResultSet rs, rs2;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        String newbal = request.getQueryString().replace("=Pay+Here","");
        String query = "update USERINFO set BALANCE=? where NAME=?";
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/AB", "mukit", "mukit");
            

            if (session != null) {
                userName = (String) session.getAttribute("name");
                if (userName == null) {
                    RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                    rd.include(request, response);
                } else {
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setString(1, newbal);
                    ps.setString(2, userName);
                    int a = ps.executeUpdate();
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Payment|AjkerBazar</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<div align=center>");
                    out.println("<h1>AjkerBazar<h1>");
                    out.println("<h2></h2>");
                    out.println("<a href=\"./Profile\">" + userName + "</a>&emsp;&emsp;");
                    out.println("<a href=\"./Home\">Home</a> &emsp;&emsp;");
                    out.println("<a href=\"./Logout\">Logout</a>");

                    out.println("</div>");
                    out.println("<div>");
                    out.println("<form action=\"./Payment\" method=\"get\">");
                    out.println("<h1> Payment Succesful!! </h1>");
                    out.println("<h2></h2>");
                    
                    out.println("</form>");
                    out.println("</div>");
                    out.println("</body>");
                    out.println("</html>");
                }

            } else {
                RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                rd.include(request, response);
            }

        } catch (ClassNotFoundException e) {
            out.println("Class Not found");
            e.printStackTrace();
        } catch (SQLException e) {
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
