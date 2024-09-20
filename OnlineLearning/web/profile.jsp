<%-- 
    Document   : profile
    Created on : Sep 19, 2024, 8:03:50 PM
    Author     : tuant
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Your profile</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    </head>
    <body>
        <%@include file="Header.jsp" %>
        <div class="form-size ">
            <form method="post" action="updateProfile" enctype="multipart/form-data">
                <div class="">
                    <div class="avatar">
                        <div class="img-avt">
                            <img src="${sessionScope.user.avatar == null ? "images/banner.png" : sessionScope.user.avatar}" alt="alt"/>
                        </div>
                        <input type="file" name="avatar">
                    </div>
                    <div class="profile-info">
                        <h1 id="login-header">Your profile</h1>
                        <c:if test="${requestScope.success != null && requestScope.success}">
                            <div class="alert alert-success alert-size" role="alert">
                                Update infomation successfully.
                            </div>
                        </c:if>
                        <c:if test="${requestScope.invalidUsername != null && requestScope.invalidUsername}">
                            <div class="alert alert-danger alert-size" role="alert">
                                Username is already existed.
                            </div>
                        </c:if>
                        <c:if test="${requestScope.invalidPhone != null && requestScope.invalidPhone}">
                            <div class="alert alert-danger alert-size" role="alert">
                                Phone number is already existed.
                            </div>
                        </c:if>
                        <div class="input-box1">    
                            Email: ${sessionScope.user.email}
                        </div>

                        <div class="form-group input-box1">
                            <span class="input-label"><small>Fullname</small></span>
                            <input type="text" class="form-control" placeholder="Fullname" name="name" 
                                   value="${sessionScope.user.name}" required pattern="[a-zA-ZÀ-ỹ ]{4,30}"
                                   title="Full name must be between 4 and 30 characters,
                                   and not contain special characters or numbers.">
                        </div>
                        <div class="form-group input-box2">
                            <span class="input-label"><small>Username</small></span>
                            <input type="text" class="form-control" placeholder="Username" name="username" 
                                   value="${sessionScope.user.username}" required pattern="[A-Za-z0-9]{3,16}" 
                                   title="Username must be between 3 and 16 characters and not contain special characters.">
                        </div>

                        <div class="form-group input-box2">
                            <span class="input-label"><small>Phone number</small></span>
                            <input type="tel" class="form-control" pattern="[0]{1}[1-9]{1}[0-9]{8}" name="phone" 
                                   placeholder="Phone number" value="${sessionScope.user.phone}" 
                                   title="">
                        </div>
                        <span class="input-label"><small>Address</small></span>
                        <div class="form-group input-box2">
                            <input type="text" class="form-control" placeholder="Address" name="address" 
                                   value="${sessionScope.user.address}" >
                        </div> 
                        <div class="form-group input-box1 gender">
                            Gender:
                            <div>
                                <input type="radio" id="none" name="gender" value="none" ${sessionScope.user.gender == null ? "checked" : ""}>
                                <label for="none">None</label>
                            </div>
                            <div>
                                <input type="radio" id="male" name="gender" value="Male" ${sessionScope.user.gender == "Male" ? "checked" : ""}>
                                <label for="male">Male</label>
                            </div>
                            <div>
                                <input type="radio" id="female" name="gender" value="Female" ${sessionScope.user.gender == "Female" ? "checked" : ""}>
                                <label for="female">Female</label>
                            </div>
                        </div>
                        <div class="login-btn">
                            <div><button type="submit" class="ud-btn ud-btn-large ud-btn-brand ud-heading-md">
                                <span class="ud-btn-label">Save</span>
                            </button></div>
                            <div class="change-btn"><a href="customer.jsp" >
                                    <span class="ud-btn-label">Change Password</span>
                                </a></div class="change-btn">
                        </div>
                        <div class="change-password-btn">

                        </div>
                    </div>
                </div>
            </form>

        </div>

        <%@include file="Footer.jsp" %>
        <link rel="stylesheet" href="css/profile.css"/>
    </body>
</html>
