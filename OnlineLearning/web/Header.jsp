<%-- 
    Document   : Header
    Created on : Sep 14, 2024, 10:15:32 AM
    Author     : sonna
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>Online Learning Web</title>

        <!-- CSS FILES -->    
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
         
        <link href="bootstrap.min.css" rel="stylesheet">
        <link href="bootstrap-icons.css" rel="stylesheet">
        <link href="header.css" rel="stylesheet">
        
    </head>

    <body>

        <nav class="navbar navbar-expand-lg bg-white shadow-lg">
            <div class="container">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <a class="navbar-brand" href="Homepage.jsp">
                    Learning
                </a>

                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav mx-auto">
                        <li class="nav-item">
                            <a class="nav-link active" href="Homepage.jsp">Homepage</a>
                        </li>

                        <li class="nav-item">
                            <a class="nav-link" href="#">Course</a>
                        </li>

                        <li class="nav-item">
                            <a class="nav-link" href="#">News</a>
                        </li>

                        <li class="nav-item">
                            <a class="nav-link" href="#">Support</a>
                        </li>
                    </ul>
                </div>

                <div class="">
                    <button type="button" class="btn btn-danger" onclick="window.location.href='login.jsp'">Login</button>
                    <button type="button" class="btn btn-secondary" >Register</button>
                </div>
            </div>
        </nav>

 
    </body>
</html>
