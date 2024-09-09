<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #e9ecef; /* Lighter background color */
        }
        .container {
            background: #ffffff; /* White background for the form */
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 350px;
            border: 1px solid #ddd; /* Border for added contrast */
        }
        .container h2 {
            margin: 0 0 20px;
            color: #333; /* Darker text color */
        }
        .container label {
            display: block;
            margin-bottom: 5px;
            color: #555; /* Slightly darker label color for better readability */
        }
        .container input[type="text"], .container input[type="password"] {
            width: 100%;
            padding: 12px;
            margin-bottom: 20px;
            border: 1px solid #ced4da; /* Lighter border color for text boxes */
            border-radius: 4px;
            background-color: #f8f9fa; /* Slightly off-white background for text boxes */
            box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1); /* Inner shadow for depth */
        }
        .container input[type="submit"] {
            background-color: #007bff; /* Primary button color */
            color: #ffffff; /* Button text color */
            border: none;
            padding: 12px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
        }
        .container input[type="submit"]:hover {
            background-color: #0056b3; /* Darker button color on hover */
        }
        .container a {
            display: block;
            text-align: center;
            margin-top: 15px;
            color: #007bff; /* Link color */
            text-decoration: none;
            font-size: 14px;
        }
        .container a:hover {
            text-decoration: underline;
        }
        .error {
            color: red;
            margin-bottom: 15px;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Login</h2>
        <c:if test="${not empty sessionScope.notification}">
            <div class="error">${sessionScope.notification}</div> 
        <%
            session.removeAttribute("notification");
        %>
    </c:if>
        <form action="loginController" method="post">
            <label for="email">Email:</label>
            <input type="text" id="email" name="email" required>

            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>

            <input type="submit" value="Login">

            <a href="register.jsp">Register New Account</a>
            <a href="resetPassword.jsp">Forgot Password?</a>
        </form>
    </div>
</body>
</html>
