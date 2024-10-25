<%-- 
    Document   : UserListAu
    Created on : Oct 25, 2024, 12:19:40 PM
    Author     : Admin
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User List</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${empty sessionScope.user}">
                <c:redirect url="login.jsp"/>
            </c:when>
            <c:when test="${sessionScope.user.role != 'Admin'}">
                <c:redirect url="/Homepage"/>
            </c:when>
            <c:otherwise>
        <h1>User List</h1>
            </c:otherwise>
        </c:choose>
    </body>
</html>
