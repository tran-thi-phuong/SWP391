<%-- 
    Document   : headerfooter
    Created on : Sep 14, 2024, 1:23:39 PM
    Author     : sonna
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
    if (session != null && session.getAttribute("user") != null) {
        Users user = (Users) session.getAttribute("user");
        String role = user.getRole().toLowerCase();
    

    switch (role) {
        case "admin":

        %><jsp:include page="#" /> <%
            break;
        case "customer":
        %><jsp:include page="Homepage.jsp" /> <%
            break; 
        default:    
        %><jsp:include page="Homepage.jsp" /> <%    
    }
}
        %>
    </body>
</html>

