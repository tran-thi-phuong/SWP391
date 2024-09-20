<%-- 
    Document   : login
    Created on : Sep 14, 2024, 2:25:37 PM
    Author     : tuant
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        
    </head>
    <body>
        <%@include file="Header.jsp" %>
        <div>
            <div class="banner">
                <img src="images/banner.png" class="" />
            </div>
            <div class="form-size">
                <form method="post" action="login">
                    <h1 id="login-header">Login to your account</h1>
                    <c:if test="${requestScope.error != null && requestScope.error}">
                        <div class="alert alert-danger alert-size" role="alert">
                            Username or password is incorrect.
                        </div>
                    </c:if>
                    <div class="form-group input-box1">
                        <input type="text" class="form-control" placeholder="Username or Email" name="username" 
                               value="${param.username}" required>
                    </div>
                    <div class="form-group input-box2">
                        <input type="password" class="form-control" placeholder="Password" name="password" 
                               value="" required>
                    </div>
                    <div class="login-btn">
                        <button type="submit" class="ud-btn ud-btn-large ud-btn-brand ud-heading-md">
                            <span class="ud-btn-label">Login</span>
                        </button>
                    </div>
                </form>
                <div class="forgot-link">
                    <span>or <a class="" href=#">Forgot Password</a></span>
                </div>
            </div>
        </div>
        <%@include file="Footer.jsp" %>
        <link rel="stylesheet" href="css/login.css">
    </body>
</html>