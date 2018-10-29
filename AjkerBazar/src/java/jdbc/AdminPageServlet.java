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

public class AdminPageServlet extends HttpServlet {

    Connection conn;
    ResultSet rs, rs2;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String query1 = "select * from Product";
        String query2 = "select distinct category from Product";
        String query3 = "update Product set NAME=?,PRICE=?,Q=?, CATEGORY=? where ID=?";
        String query4 = "delete from Product where ID=?";
        String query5 = "insert into Product(NAME,PRICE,Q,CATEGORY) values(?,?,?,?)";
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/AB", "mukit", "mukit");
            String action = request.getQueryString();
            String pid = request.getParameter("pid");
            String pname = request.getParameter("pname");
            String uprice = request.getParameter("uprice");
            String q = "1";
            String cat = request.getParameter("cat");
            int a;
            PreparedStatement ps1 = conn.prepareStatement(query1);
            rs = ps1.executeQuery();
            PreparedStatement ps2 = conn.prepareStatement(query2);
            rs2 = ps2.executeQuery();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Admin|AjkerBazar</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div align=center>");
            out.println("<h1>AjkerBazar|ADMIN PANEL<h1>");
            out.println("<h1>Categories: </h1>");
            out.println("<fieldset>");
            out.println("<h1>");
            while (rs2.next()) {
                out.println(rs2.getString(1) + "&emsp;");
            }

            out.println("</fieldset>");
            out.println("</h1>");
            out.println("</div>");
            out.println("<div>");

            out.println("<h1>All Products</h1>");
            out.println("<h2>");
            out.println("<html><body><table>"
                    + "<tr>"
                    + "<td>Product Id &emsp;</td>"
                    + "<td>Product Name &emsp;</td>"
                    + "<td>Unit Price &emsp;</td>"
                    + "<td>Category &emsp;</td>"
                    + "</tr>");

            while (rs.next()) {
                out.println("<tr>"
                        + "<td>" + rs.getString(1) + "</td>"
                        + "<td>" + rs.getString(2) + "</td>"
                        + "<td>" + rs.getString(3) + "</td>"
                        + "<td>" + rs.getString(5) + "</td></tr>");

            }

            out.println("</table></body>");
            out.println("<a href=\"http://localhost:9090/AjkerBazar/adminpage.html\"><button type=\"button\">EDIT</button></a>");
            if (action.equals("update")) {
                PreparedStatement ps3 = conn.prepareStatement(query3);
                ps3.setString(1, pname);
                ps3.setString(2, uprice);
                ps3.setString(3, q);
                ps3.setString(4, cat);
                ps3.setString(5, pid);
                a = ps3.executeUpdate();
                response.sendRedirect("./AdminPage");
                
            } else if (action.equals("delete")) {
                PreparedStatement ps3 = conn.prepareStatement(query4);
                ps3.setString(1, pid);

                a = ps3.executeUpdate();
                response.sendRedirect("./AdminPage");
                
            }
            if (action.equals("add")) {
                PreparedStatement ps3 = conn.prepareStatement(query5);
                ps3.setString(1, pname);
                ps3.setString(2, uprice);
                ps3.setString(3, q);
                ps3.setString(4, cat);
                
                a = ps3.executeUpdate();
                response.sendRedirect("./AdminPage");
                
            }

            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

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
