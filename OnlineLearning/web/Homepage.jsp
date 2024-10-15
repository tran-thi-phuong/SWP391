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
        <link href="css/Homepage.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet"> 
    </head>
    <body>
        <%@include file="Header.jsp" %>
        <p></p>
        <div class="slider-container">
            <h2 class="slider-title">Hot</h2>
            <div class="slider">
                <!-- Radio buttons for navigation -->
                <c:forEach var="slider" items="${sliders}" varStatus="status">
                    <c:if test="${slider.status == 'shown'}">
                        <input type="radio" name="slider" id="slide${status.index + 1}" <c:if test="${status.index == 0}">checked</c:if>>
                    </c:if>
                </c:forEach>

                <div class="slides">
                    <c:forEach var="slider" items="${sliders}" varStatus="status">
                        <c:if test="${slider.status == 'shown'}">
                            <div class="slide">
                                <img src="${slider.image}" alt="${slider.title}">
                            </div>
                        </c:if>
                    </c:forEach>
                </div>

                <div class="radio-controls">
                    <c:forEach var="slider" items="${sliders}" varStatus="status">
                        <c:if test="${slider.status == 'shown'}">
                            <label for="slide${status.index + 1}"></label>
                        </c:if>
                    </c:forEach>
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
                                        <a href="blogDetail?blogId=${blog.blogId}">
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
                                <img class="card-img-top" src="${subject.thumbnail}" alt="${subject.title}">
                                <div class="card-body">
                                    <h5 class="card-title">
                                        <a href="subject-detail.jsp?subjectId=${subject.subjectID}">
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