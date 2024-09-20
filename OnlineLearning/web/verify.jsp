<%-- 
    Document   : verify
    Created on : Sep 19, 2024, 4:13:26 PM
    Author     : tuant
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html >
    <head>
        <meta charset="UTF-8">
         <title>Verify your account</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">    
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    </head>
    <body>
        
        <%@include file="Header.jsp" %>
        <div class="form-size">
            <form method="post" action="verify">
                <h1 id="login-header">Enter confirmation code</h1>
                <span>We sent a confirmation code to the email address that you provided: ${requestScope.email}</span>
                <c:if test="${requestScope.error != null && requestScope.error}">
                    <div class="alert alert-danger alert-size" role="alert">
                        Confirmation code is incorrect.
                    </div>
                </c:if>
                <input type="hidden" name="userId" value="${requestScope.userId}">
                <input type="hidden" name="email" value="${requestScope.email}">
                <div class="form-group input-box1">
                    <input type="text" class="form-control" pattern="[0-9]{1,6}" placeholder="6-digit code" name="code" 
                           value="${param.code}" required>
                </div>
                <div class="forgot-link">
                    <a class="" href="resendCode?userId=${requestScope.userId}&email=${requestScope.email}">Resend confirmation code</a>
                </div>
                <div class="login-btn">
                    <button type="submit" class="ud-btn ud-btn-large ud-btn-brand ud-heading-md">
                        <span class="ud-btn-label">Verify</span>
                    </button>
                </div>
            </form>

        </div>
        <%@include file="Footer.jsp" %>
        <link rel="stylesheet" href="css/verify.css"/>
    </body>
</html>


