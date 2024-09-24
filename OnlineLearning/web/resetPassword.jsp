<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Reset Password</title>
        <link rel="stylesheet" type="text/css" href="css/change_reset_password.css">
    </head>
    <body>
        <div class="body2">
            <h1>Reset Your Password</h1>
            <c:if test="${not empty message}">
                <div class="message-box-2">
                    <p>${message}</p>
                </div>
            </c:if>
            <form action="resetPassword" method="post">
                <input type="hidden" name="token" value="${sessionScope.resetToken}">
                <div class="password">
                    <span class="split">
                    <label for="newPassword">New Password</label>
                    <input type="password" id="newPassword" name="newPassword" required>
                    </span>
                    <span class="split">
                    <label for="confirmPassword">Confirm You Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required>
                    </span>
                </div>
                <button type="submit" class="submit-btn">Reset Password</button>
                 <input type="button" value="Back to Login" class="button-not-you" onclick="window.location.href = 'login.jsp'">
            </form>
        </div>
    </body>
</html>