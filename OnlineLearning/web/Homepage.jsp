<%-- 
    Document   : Homepage
    Created on : Sep 15, 2024, 9:55:36 AM
    Author     : sonna
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Online Learning Web</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="css/Homepage.css" rel="stylesheet">
    <style>
       
    </style>
</head>
<body>
    <%@include file="Header.jsp" %>
    <p></p>
    <div class="slider-container">
        <div class="slider">
            <input type="radio" name="slider" id="slide1" checked>
            <input type="radio" name="slider" id="slide2">
            <input type="radio" name="slider" id="slide3">

            <div class="slides">
                <div class="slide">
                    <img src="images/slider1.png" alt="Slide 1">
                </div>
                <div class="slide">
                    <img src="images/slider2.png" alt="Slide 2">
                </div>
                <div class="slide">
                    <img src="images/slider3.png" alt="Slide 3">
                </div>
            </div>

            

            <div class="radio-controls">
                <label for="slide1"></label>
                <label for="slide2"></label>
                <label for="slide3"></label>
            </div>
        </div>
    </div>
    <p></p>
    <div class="courses-container">
            <div class="course">
                <a href="course-details.html">
                    <img src="slider1.png" alt="Course 1">
                    <div class="course-info">
                        <h3>Course Title 1</h3>
                        <p>Course Description</p>
                    </div>
                </a>
            </div>
            <div class="course">
                <a href="course-details.html">
                    <img src="slider1.png" alt="Course 2">
                    <div class="course-info">
                        <h3>Course Title 2</h3>
                        <p>Course Description</p>
                    </div>
                </a>
            </div>
            <div class="course">
                <a href="course-details.html">
                    <img src="slider1.png" alt="Course 3">
                    <div class="course-info">
                        <h3>Course Title 3</h3>
                        <p>Course Description</p>
                    </div>
                </a>
            </div>
            <div class="course">
                <a href="course-details.html">
                    <img src="slider1.png" alt="Course 4">
                    <div class="course-info">
                        <h3>Course Title 4</h3>
                        <p>Course Description</p>
                    </div>
                </a>
            </div>
            <div class="course">
                <a href="course-details.html">
                    <img src="images/slider1.png" alt="Course 5">
                    <div class="course-info">
                        <h3>Course Title 5</h3>
                        <p>Course Description</p>
                    </div>
                </a>
            </div>
            <div class="course">
                <a href="course-details.html">
                    <img src="slider1.png" alt="Course 6">
                    <div class="course-info">
                        <h3>Course Title 6</h3>
                        <p>Course Description</p>
                    </div>
                </a>
            </div>
            <div class="course">
                <a href="course-details.html">
                    <img src="slider1.png" alt="Course 7">
                    <div class="course-info">
                        <h3>Course Title 7</h3>
                        <p>Course Description</p>
                    </div>
                </a>
            </div>
            <div class="course">
                <a href="course-details.html">
                    <img src="slider1.png" alt="Course 8">
                    <div class="course-info">
                        <h3>Course Title 8</h3>
                        <p>Course Description</p>
                    </div>
                </a>
            </div>
        </div>
    <p></p>
    <%@include file="Footer.jsp" %>
</body>
</html>
