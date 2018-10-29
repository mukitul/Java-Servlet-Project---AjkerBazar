
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    HttpSession sess = request.getSession(false);
    String name = (String) sess.getAttribute("name");
    if (name == null) {
        RequestDispatcher rd = request.getRequestDispatcher("./index.jsp");

    } else {
        RequestDispatcher rd = request.getRequestDispatcher("./Home");
        rd.forward(request, response);
    }

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome|AjkerBazar</title>
    </head>
    <body>

        <div>
            <h1>Welcome To AjkerBazar</h1>
        </div>

        <div align="right">
            <a href="http://localhost:9090/AjkerBazar/registration.html">Sign Up</a>
            <a>|</a>
            <a href="http://localhost:9090/AjkerBazar/login.html">Sign In</a>
        </div>
        <div align="center">
            <img src="cover.jpg" width="100%"</img>
        </div>
    </body>
</html>
