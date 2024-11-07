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
        <<p></p>
        <div class="slider-container"> <!-- Slider section for displaying images -->
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
                                    <c:if test="${not empty slider.backlink}">
                                        <!-- If backlink is not empty, wrap the image with the <a> tag -->
                                        <a href="${slider.backlink}" target="_blank">
                                            <img src="${slider.image}" alt="${slider.title}"> <!-- Display slider images -->
                                        </a>
                                    </c:if>
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

        <section id="content-section" class="container mt-5">
            <div class="row">
                <!-- Top Subjects Section: Left side (3/4 width) -->
                <div class="col-md-9">
                    <h2 class="text-center mb-4">Top Subjects</h2>
                    <div class="row p-3"> <!-- Thêm border cho toàn bộ khu vực chứa Subjects -->
                        <c:forEach var="subject" items="${topSubjects}">
                            <div class="col-md-4 mb-4">
                                <div class="card">
                                    <img class="card-img-top" src="${subject.thumbnail}" alt="${subject.title}"> <!-- Display subject thumbnail -->
                                    <div class="card-body">
                                        <h5 class="card-title">
                                            <a href="SubjectView?subjectId=${subject.subjectID}">
                                                ${subject.title}
                                            </a>
                                        </h5>
                                        <p class="card-text">
                                            ${subject.ownerName}
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <!-- Latest Blogs Section: Right side (1/4 width) -->
                <div class="col-md-3">
                    <h2 class="text-center mb-4">Latest Blogs</h2>
                    <div class="container border p-3"> <!-- Thêm border cho toàn bộ khu vực chứa Blogs -->
                        <ul class="list-unstyled">
                            <c:forEach var="blog" items="${latestBlogs}">
                                <li class="mb-3">
                                    <a href="blogDetail?blogId=${blog.blogId}" class="ms-2" style="color: orange;">
                                        ${blog.title}
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
        </section>

        <%@include file="Footer.jsp" %> <!-- Include the footer of the page -->
    </body>
</html>
