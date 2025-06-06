<%-- 
    Document   : authorInformation
    Created on : Oct 3, 2024, 8:34:55 PM
    Author     : sonna
--%>

<%@ page import="model.Users" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Author Information</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet"> 
        <style>
            footer{
                margin-top: 250px;
            }
        </style>
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <p></p>
        
        <% 
            Users author = (Users) request.getAttribute("author");
            if (author != null) {
        %>
        <div class="container">
            <h1>Author Information</h1>
            <p><strong>Fullname:</strong> <%= author.getName() %></p>
            <p><strong>Email:</strong> <%= author.getEmail() %></p>
            <p><strong>Phone:</strong> <%= author.getPhone() %></p>
        </div>
        <% } else { %>
        <p>Author is not existed!</p>
        <% } %>
        <%@include file="Footer.jsp"%>
          
    </body>
</html>

