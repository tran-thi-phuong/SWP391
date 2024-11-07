<%-- 
    Document   : Homepage
    Created on : Sep 15, 2024, 9:55:36 AM
    Author     : sonna
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- Using JSTL Core library -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Online Learning Web</title>
        <!-- Preconnect to fonts and include CSS for styling -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Homepage.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet"> 
    </head>
    <body>
        <%@include file="Header.jsp" %> <!-- Include the header of the page -->

        <div class="slider-container"> <!-- Slider section for displaying images -->
            <h2 class="slider-title">Hot</h2>
            <div class="slider">
                <!-- Radio buttons for navigation -->
                <c:forEach var="slider" items="${sliders}" varStatus="status">
                    <c:if test="${slider.status == 'Show'}">
                        <input type="radio" name="slider" id="slide${status.index + 1}" <c:if test="${status.index == 0}">checked</c:if>>
                    </c:if>
                </c:forEach>

                <div class="slides">
                    <c:forEach var="slider" items="${sliders}" varStatus="status">
                        <c:if test="${slider.status == 'Show'}">
                            <div class="slide">
                                <img src="${slider.image}" alt="${slider.title}"> <!-- Display slider images -->
                            </div>
                        </c:if>
                    </c:forEach>
                </div>

                <div class="radio-controls"> <!-- Navigation controls for the slider -->
                    <c:forEach var="slider" items="${sliders}" varStatus="status">
                        <c:if test="${slider.status == 'Show'}">
                            <label for="slide${status.index + 1}"></label>
                        </c:if>
                    </c:forEach>
                </div>
            </div>
        </div>

        <section id="latest-blogs"> <!-- Latest Blogs section -->
            <h2 class="text-center mt-5 mb-4">Latest Blogs</h2>
            <div class="container">
                <div class="row">
                    <c:forEach var="blog" items="${latestBlogs}">
                        <div class="col-md-4 mb-4">
                            <div class="card">
                                <img class="card-img-top" src="image/default-thumbnail.jpg" alt="${blog.title}"> <!-- Default thumbnail for blogs -->
                                <div class="card-body">
                                    <h5 class="card-title">
                                        <a href="blogDetail?blogId=${blog.blogId}"> <!-- Link to the blog detail page -->
                                            ${blog.title}
                                        </a>
                                    </h5>
                                    <p class="card-text">
                                        ${fn:substring(blog.content, 0, 100)}... <!-- Display a snippet of the blog content -->
                                    </p>
                                    <p class="text-muted">Created at: ${blog.createAt}</p> <!-- Display blog creation date -->
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>

        <section id="subjects"> <!-- Top Subjects section -->
            <h2 class="text-center mt-5 mb-4">Top Subjects</h2>
            <div class="container">
                <div class="row">
                    <c:forEach var="subject" items="${topSubjects}">
                        <div class="col-md-4 mb-4">
                            <div class="card">
                                <img class="card-img-top" src="images/${subject.thumbnail}" alt="${subject.title}"> <!-- Display subject thumbnail -->
                                <div class="card-body">
                                    <h5 class="card-title">
                                        <a href="SubjectView?subjectId=${subject.subjectID}"> <!-- Link to the subject view page -->
                                            ${subject.title}
                                        </a>
                                    </h5>
                                    <p class="card-text">
                                        ${fn:substring(subject.description, 0, 100)}... <!-- Display a snippet of the subject description -->
                                    </p>
                                    <p class="text-muted">Updated on: ${subject.updateDate}</p> <!-- Display subject update date -->
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>
        
        <%@include file="Footer.jsp" %> <!-- Include the footer of the page -->
    </body>
</html>
