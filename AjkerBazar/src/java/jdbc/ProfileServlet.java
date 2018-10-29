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

public class ProfileServlet extends HttpServlet {

    String userName;
    Connection conn;
    ResultSet rs, rs2, rs3;
    String t, b;
    int prev;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        String query = "select * from Cart where uname=?";
        String query2 = "update USERINFO set BALANCE=? where NAME=?";
        String query3 = "select BALANCE from USERINFO where NAME=?";
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
                    ps.setString(1, userName);
                    rs = ps.executeQuery();

                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Products|AjkerBazar</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<div align=center>");
                    out.println("<h1>AjkerBazar<h1>");
                    out.println("<h2></h2>");
                    out.println("<a href=\"./Profile\">" + userName + "</a>&emsp;&emsp;");
                    out.println("<a href=\"./Home\">Home</a> &emsp;&emsp;");
                    out.println("<a href=\"./Logout\">Logout</a>");

                    out.println("</div>");

                    PreparedStatement ps3 = conn.prepareStatement(query3);
                    ps3.setString(1, userName);
                    rs3 = ps3.executeQuery();
                    out.println("<div>");
                    while (rs3.next()) {
                        t = rs3.getString(1);
                    }
                    if (t == null) {
                        out.println("<h1>Current Balance:0</h1>");
                        prev = 0;
                    } else {
                        out.println("<h1>Current Balance:" + t + " </h1>");
                        prev = Integer.parseInt(t);
                    }

                    b = request.getParameter("balance");

                    
                    if (b != null) {
                        b = Integer.toString(Integer.parseInt(b) + prev);
                        PreparedStatement ps2 = conn.prepareStatement(query2);
                        ps2.setString(1, b);
                        ps2.setString(2, userName);
                        int a = ps2.executeUpdate();
                        response.sendRedirect("./Profile");
                    }

                    out.println("<form action=\"./Profile\" method=\"post\">");
                    out.println("<h2>Add BALANCE(in TAKA): <input type=\"text\" autocomplete=\"off\" name=\"balance\"></h2>");
                    out.println("<h2><input type=\"submit\" value=\"ADD\"><h2>");
                    out.println("</form>");

                    out.println("<h1> Timeline </h1>");

                    out.println("<html><body><table>"
                            + "<tr>"
                            + "<td>Product Id &emsp;&emsp;</td>"
                            + "<td>Name &emsp;&emsp;</td>"
                            + "<td>Unit Price &emsp;&emsp;</td>"
                            + "<td>Quantity(Kg/Piece)&emsp;&emsp;</td>"
                            + "<td>Cost</td>"
                            + "</tr>");

                    while (rs.next()) {
                        out.println("<tr>"
                                + "<td>" + rs.getString(1) + "&emsp;&emsp;&emsp;&emsp;</td>"
                                + "<td>" + rs.getString(3) + "&emsp;&emsp;&emsp;&emsp;</td>"
                                + "<td>" + rs.getString(4) + "&emsp;&emsp;&emsp;&emsp;</td>"
                                + "<td>" + rs.getString(5) + "&emsp;&emsp;&emsp;&emsp;</td>"
                                + "<td>" + rs.getString(6) + "&emsp;&emsp;&emsp;&emsp;</td>"
                                + "</tr>");

                    }

                    out.println("</table></body>");

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
