<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Find Account</title>
        <link rel="stylesheet" type="text/css" href="css/findAccount.css">
    </head>
    <body style="display: flex">
        <div class="container">
            <c:if test="${not empty errorMessage}">
                <div class="error-message-1">
                    <p>${errorMessage}</p>
                </div>
            </c:if>
            <form class="center-content" action="findAccount" method="post">
                <label for="email" class="title"><p>Find your account</p></label><br>
                <label for="email">Please enter your registered email:</label>
                <div class="center-self">
                    <input type="email" id="email" name="email" required>
                </div>
                <div class="button-forgot">
                    <button type="button" class="cancel-button" onclick="window.location.href = 'login.jsp'">Cancel</button>
                    <button type="submit">Search Your Email</button>
                </div>
            </form>
        </div>
    </body>
</html>
