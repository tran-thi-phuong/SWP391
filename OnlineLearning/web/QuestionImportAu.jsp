<%-- 
    Document   : QuestionImportAu
    Created on : Oct 25, 2024, 12:18:19 PM
    Author     : Admin
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.Users" %>
<%@ page import="dal.RolePermissionDAO" %>
<%@ page import="dal.PagesDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.*" %>

<%
    Users currentUser = (Users) session.getAttribute("user");
    
    if (currentUser == null) {
        response.sendRedirect("login.jsp"); 
        return;
    }

    String userRole = currentUser.getRole();

    String pageUrl = request.getRequestURL().toString();
    
    RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
    PagesDAO pagesDAO = new PagesDAO();

    Integer pageID = pagesDAO.getPageIDFromUrl(pageUrl);

    if (pageID != null) {
        
        boolean hasPermission = rolePermissionDAO.hasPermission(userRole, pageID);

        if (!hasPermission) {
           
            response.sendRedirect("login.jsp");
            return;
        }
    } else {
       
        response.sendRedirect("error.jsp?message=Page not found");
        return;
    }

   
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Question Import</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${empty sessionScope.user}">
                <c:redirect url="login.jsp"/>
            </c:when>
            <c:when test="${sessionScope.user.role != 'Admin' && sessionScope.user.role != 'Instructor'}">
                <c:redirect url="/Homepage"/>
            </c:when>
            <c:otherwise>
        <h1>Question Import</h1>
            </c:otherwise>
        </c:choose>
    </body>
</html>
