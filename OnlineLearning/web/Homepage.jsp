<%-- 
    Document   : Homepage
    Created on : Sep 15, 2024, 9:55:36 AM
    Author     : sonna
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Online Learning Web</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-3m7HEg5vP1+mtX6mDjMl++z1/5D5PgrnFA0ZI+VfQ4I/fWcgIFnm/4UO6upAdFw7" crossorigin="anonymous">
        <link href="css/Homepage.css" rel="stylesheet">

        <style>

        </style>
    </head>
    <body>
        <%@include file="Header.jsp" %>
        <p></p>
        <div class="slider-container">
            <h2 class="slider-title">Hot</h2>
            <div class="slider">
                <!-- Radio buttons for navigation -->
                <input type="radio" name="slider" id="slide1" checked>
                <input type="radio" name="slider" id="slide2">
                <input type="radio" name="slider" id="slide3">
                <input type="radio" name="slider" id="slide4">
                <input type="radio" name="slider" id="slide5">
                <!-- Slider images -->
                <div class="slides">
                    <c:forEach var="subject" items="${topSubjects}">
                        <div class="slide">
                            <img src="image/${subject.thumbnail}" alt="${subject.title}">
                        </div>
                    </c:forEach>
                </div>
                <!-- Radio controls -->
                <div class="radio-controls">
                    <label for="slide1"></label>
                    <label for="slide2"></label>
                    <label for="slide3"></label>
                    <label for="slide4"></label>
                    <label for="slide5"></label>
                </div>
            </div>
        </div>

        <section id="latest-blogs">
            <h2 class="text-center mt-5 mb-4">Latest Blogs</h2>
            <div class="container">
                <div class="row">
                    <c:forEach var="blog" items="${latestBlogs}">
                        <div class="col-md-4 mb-4">
                            <div class="card">
                                <img class="card-img-top" src="image/default-thumbnail.jpg" alt="${blog.title}">
                                <div class="card-body">
                                    <h5 class="card-title">
                                        <a href="blogDetail.jsp?blogId=${blog.blogId}">
                                            ${blog.title}
                                        </a>
                                    </h5>
                                    <p class="card-text">
                                        ${fn:substring(blog.content, 0, 100)}...
                                    </p>
                                    <p class="text-muted">Created at: ${blog.createAt}</p>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>

        <section id="subjects">
            <h2 class="text-center mt-5 mb-4">Top Subjects</h2>
            <div class="container">
                <div class="row">
                    <c:forEach var="subject" items="${topSubjects}">
                        <div class="col-md-4 mb-4">
                            <div class="card">
                                <img class="card-img-top" src="image/${subject.thumbnail}" alt="${subject.title}">
                                <div class="card-body">
                                    <h5 class="card-title">
                                        <a href="subjectDetail.jsp?subjectId=${subject.subjectID}">
                                            ${subject.title}
                                        </a>
                                    </h5>
                                    <p class="card-text">
                                        ${fn:substring(subject.description, 0, 100)}...
                                    </p>
                                    <p class="text-muted">Updated on: ${subject.updateDate}</p>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>
        <p></p>
        <%@include file="Footer.jsp" %>
    </body>
</html>
