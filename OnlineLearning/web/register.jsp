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
        <title>Sign up</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <link rel="stylesheet" href="css/register.css">

    </head>
    <body>
        <%@include file="Header.jsp" %>
        <div>
            <div class="banner">
                <img src="images/banner.png" class="" />
            </div>
            <div class="form-size">
                <form method="post" action="register">
                    <h1 id="login-header">Sign up and start learning</h1>
                    <c:if test="${requestScope.invalidUsername != null && requestScope.invalidUsername}">
                        <div class="alert alert-danger alert-size" role="alert">
                            Username is already existed.
                        </div>
                    </c:if>
                    <c:if test="${requestScope.passNotMatch != null && requestScope.passNotMatch}">
                        <div class="alert alert-danger alert-size" role="alert">
                            Passwords are not match. Try again
                        </div>
                    </c:if>
                    <c:if test="${requestScope.invalidEmail != null && requestScope.invalidEmail}">
                        <div class="alert alert-danger alert-size" role="alert">
                            Email is already existed.
                        </div>
                    </c:if>
                    <c:if test="${requestScope.invalidPhone != null && requestScope.invalidPhone}">
                        <div class="alert alert-danger alert-size" role="alert">
                            Phone number is already existed.
                        </div>
                    </c:if>
                    <div class="form-group input-box1">
                        <input type="text" class="form-control" placeholder="Fullname" name="name" 
                               value="${param.name}" required pattern="[a-zA-ZÀ-ỹ ]{4,30}"
                               title="Full name must be between 4 and 30 characters,
                               and not contain special characters or numbers.">
                    </div>
                    <div class="form-group input-box2">
                        <input type="text" class="form-control" placeholder="Username" name="username" 
                               value="${param.username}" required pattern="[A-Za-z0-9]{3,16}" 
                               title="Username must be between 3 and 16 characters and not contain special characters.">
                    </div>
                    <div class="form-group input-box2">
                        <input type="email" class="form-control" placeholder="Email" name="email" 
                               value="${param.email}" required>
                    </div>
                    <div class="form-group input-box2">
                        <input type="password" class="form-control" placeholder="Password" name="password" 
                               value="${param.password}" required 
                               pattern="^(?=.*[A-Z])(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,16}$" 
                               title="Password must start with an uppercase letter, be 8-16 characters long, and contain at least one special character.">
                    </div>
                    <div class="form-group input-box2">
                        <input type="password" class="form-control" placeholder="Confirm Password" name="repassword" 
                               value="${param.repassword}" required 
                               pattern="^(?=.*[A-Z])(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,16}$" 
                               title="Password must start with an uppercase letter, be 8-16 characters long, and contain at least one special character.">
                    </div>

                    <div class="form-group input-box2">
                        <input type="tel" class="form-control" pattern="[0]{1}[1-9]{1}[0-9]{8}" name="phone" 
                               placeholder="Phone number (Optional)" value="${param.phone}" 
                               title="">
                    </div>
                    <div class="form-group input-box2">
                        <input type="text" class="form-control" placeholder="Address (Optional)" name="address" 
                               value="${param.address}" >
                    </div> 
                    <div class="form-group input-box2 gender">
                        Gender:
                        <div>
                            <input type="radio" id="none" name="gender" value="none" checked>
                            <label for="none">None</label>
                        </div>
                        <div>
                            <input type="radio" id="male" name="gender" value="Male">
                            <label for="male">Male</label>
                        </div>
                        <div>
                            <input type="radio" id="female" name="gender" value="Female" >
                            <label for="female">Female</label>
                        </div>
                    </div>
                    <div class="login-btn">
                        <button type="submit" class="ud-btn ud-btn-large ud-btn-brand ud-heading-md" name="sign-up-btn">
                            <span class="ud-btn-label">Sign up</span>
                        </button>
                    </div>
                </form>
                <div class="forgot-link">
                    <span>Already have an account? <a class="" href="login.jsp">Log in</a></span>
                </div>
            </div>
        </div>
        <br>
        <%@include file="Footer.jsp" %>
    </body>
</html>
