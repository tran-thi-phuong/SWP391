<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Recover Account</title>
    <link rel="stylesheet" type="text/css" href="css/recoverAccount.css">
</head>
<body>
    <div class="body2">
        <div> 
        <c:if test="${not empty message}">
            <h1>Please check you email!!!!</h1>
            <div class="message-box">
                <p>${message}</p>
                </div>
                <input type="button" value="Back to Login" class="button-not-you" onclick="window.location.href = 'login.jsp'">
            </div>
        </c:if>
        
        <c:if test="${not empty email}">
            <h1>Your Account</h1><br>
            <div class="account-info">
                <img src="${empty user.avatar ? 'images/default_avatar.jpg' : user.avatar}" alt="Avatar" class="avatar">

                <p>Username: ${username}</p>
                <p>Email: ${email}</p>       
            </div>
            <div class="recover-actions">
                <form action="forgotPassword" method="post">
                    <input type="hidden" name="email" value="${email}"> 
                    <button type="submit">Send Reset Link</button>
                </form>
                <input type="button" value="Not You?" class="button-not-you" onclick="window.location.href = 'findAccount.jsp'">
            </div>
        </c:if>
    </div>
</body>
</html>
