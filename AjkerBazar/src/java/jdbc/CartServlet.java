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

public class CartServlet extends HttpServlet {

    String userName;
    Connection conn;
    ResultSet rs, rs2, rs4, rs5;
    String id, name, unitPrice, quantity, newbal;
    int cost, a = 0;
    int sum = 0;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        String query = "select * from Product where id=?";
        String productQuery = "select distinct category from Product";
        String insertQuery = "insert into Cart(pid,uname,pname,price,quantity,cost) values(?,?,?,?,?,?)";
        String query3 = "select BALANCE from USERINFO where NAME=?";
        //String query4 = "update USERINFO set BALANCE=? where NAME=?";
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/AB", "mukit", "mukit");
            String queryPart = request.getQueryString();
            //out.println("<h4>" + queryPart + "</h4>");
            String part1 = queryPart.replace("&", "");
            String part2 = part1.replace("quantity=0", "");
            String part3 = part2.replace("productId=", " ");
            String tmp_part3 = part3.replace("quantity=", " ");
            String part4 = tmp_part3.replace(" ", ":");
            out.println("<h4>" + part4 + "</h4>");

            String[] part5 = part4.split(":");
            /*for (int i = 1; i < part5.length; i++) {
                out.println("<h4>" + part5[1] + "</h4>");
            }
            out.println("<h4>" + part5.length + "</h4>");*/
            //part5 - even index contain prouduct id, odd index contain product quantity
            PreparedStatement ps = conn.prepareStatement(query);
            PreparedStatement ps2 = conn.prepareStatement(productQuery);
            rs2 = ps2.executeQuery();
            PreparedStatement ps3 = conn.prepareStatement(insertQuery);

            if (session != null) {
                userName = (String) session.getAttribute("name");
                if (userName == null) {
                    RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                    rd.include(request, response);
                } else {

                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Your Cart|AjkerBazar</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<div align=center>");
                    out.println("<h1>AjkerBazar<h1>");
                    out.println("<h2></h2>");
                    out.println("<a href=\"./Profile\">" + userName + "</a>&emsp;&emsp;");
                    out.println("<a href=\"./Home\">Home</a> &emsp;&emsp;");
                    out.println("<a href=\"./Logout\">Logout</a>");
                    out.println("<h1>Our Product Categories</h1>");
                    out.println("<fieldset>");
                    out.println("<h1>");
                    while (rs2.next()) {
                        out.println("<a href=\"./Product?" + rs2.getString(1) + "\">" + rs2.getString(1) + "</a>&emsp;");
                    }
                    out.println("</fieldset>");
                    out.println("</h1>");
                    out.println("</div>");
                    out.println("<div>");
                    out.println("<form action=\"./Payment\" method=\"get\">");
                    out.println("<h1>Your Cart</h1>");
                    out.println("<h2></h2>");
                    out.println("<html><body><table>"
                            + "<tr>"
                            + "<td>Product Id &emsp;</td>"
                            + "<td>Product Name &emsp;</td>"
                            + "<td>Unit Price &emsp;</td>"
                            + "<td>Quantity &emsp;</td>"
                            + "<td>Cost</td>"
                            + "</tr>");
                    for (int i = 1; i < part5.length; i = i + 2) {
                        ps.setString(1, (part5[i]));
                        rs = ps.executeQuery();

                        while (rs.next()) {
                            id = rs.getString(1);
                            name = rs.getString(2);
                            unitPrice = rs.getString(3);
                            quantity = part5[i + 1].replace(" ", "");
                            cost = Integer.parseInt(unitPrice) * Integer.parseInt(quantity);
                            sum = sum + cost;
                            out.println("<html><body><table>"
                                    + "<tr>"
                                    + "<td>" + id + "&emsp;&emsp;&emsp;&emsp;</td>"
                                    + "<td>" + name + "&emsp;&emsp;&emsp;&emsp;</td>"
                                    + "<td>" + unitPrice + "&emsp;&emsp;&emsp;&emsp;</td>"
                                    + "<td>" + quantity + "&emsp;&emsp;&emsp;&emsp;</td>"
                                    + "<td>" + cost + "</td>"
                                    + "</tr>");
                        }
                    }
                    out.println("</table></body>");
                    PreparedStatement ps4 = conn.prepareStatement(query3);
                    ps4.setString(1, userName);
                    rs4 = ps4.executeQuery();

                    out.println("<h1>Your Bill: " + sum + " Taka</h1>");
                    String t = "";
                    while (rs4.next()) {
                        t = rs4.getString(1);

                    }
                    if (t != null) {
                        out.println("<h1>Your Account Balance: " + t + " Taka</h1>");
                        if (Integer.parseInt(t) > sum) {
                            newbal = Integer.toString(Integer.parseInt(t) - sum);
                            out.println("Pay using AjkerBazar Pay:<input type= \"submit\"  value=\"Pay Here\" name=\"" + newbal + "\">");
                            for (int i = 1; i < part5.length; i = i + 2) {
                                ps.setString(1, (part5[i]));
                                rs = ps.executeQuery();

                                while (rs.next()) {
                                    id = rs.getString(1);
                                    name = rs.getString(2);
                                    unitPrice = rs.getString(3);
                                    quantity = part5[i + 1].replace(" ", "");
                                    cost = Integer.parseInt(unitPrice) * Integer.parseInt(quantity);

                                    ps3.setString(1, id);
                                    ps3.setString(2, userName);
                                    ps3.setString(3, name);
                                    ps3.setString(4, unitPrice);
                                    ps3.setString(5, quantity);
                                    ps3.setString(6, Integer.toString(cost));
                                    a = ps3.executeUpdate();
                                }
                            }
                        } else {
                            out.println("<h1>Pay using AjkerBazar Pay:</h1>You have not sufficient balance to buy these products.");
                            out.println("Recharge Your Account Balance <a href=\"./Profile\">HERE</a>&emsp;&emsp;");
                        }
                    } else {
                        out.println("<h1>Your Account Balance: 0 Taka</h1>");
                        out.println("Pay using AjkerBazar Pay:You have not sufficient balance to buy these products.");
                        out.println("Recharge Your Account Balance <a href=\"./Profile\">HERE</a>&emsp;&emsp;");
                    }
                    /*
                    for (int i = 1; i < part5.length; i = i + 2) {
                        ps.setString(1, (part5[i]));
                        rs = ps.executeQuery();

                        while (rs.next()) {
                            id = rs.getString(1);
                            name = rs.getString(2);
                            unitPrice = rs.getString(3);
                            quantity = part5[i + 1].replace(" ", "");
                            cost = Integer.parseInt(unitPrice) * Integer.parseInt(quantity);
                            sum = sum + cost;
                            out.println("<html><body><table>"
                                    + "<tr>"
                                    + "<td>" + id + "&emsp;&emsp;&emsp;&emsp;</td>"
                                    + "<td>" + name + "&emsp;&emsp;&emsp;&emsp;</td>"
                                    + "<td>" + unitPrice + "&emsp;&emsp;&emsp;&emsp;</td>"
                                    + "<td>" + quantity + "&emsp;&emsp;&emsp;&emsp;</td>"
                                    + "<td>" + cost + "</td>"
                                    + "</tr>");

                            ps3.setString(1, id);
                            ps3.setString(2, userName);
                            ps3.setString(3, name);
                            ps3.setString(4, unitPrice);
                            ps3.setString(5, quantity);
                            ps3.setString(6, Integer.toString(cost));
                            a = ps3.executeUpdate();
                        }
                    }
                     */

                    sum = 0;

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
