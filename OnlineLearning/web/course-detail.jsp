
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Subject" %>
<%@ page import="model.SubjectCategory" %>
<%@ page import="model.User" %>
<%@ page import="model.PackagePrice" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Course Details</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet"> 
        <link rel="stylesheet" href="css/course-style.css">
    </head>

    <body>
        <%@include file="Header.jsp" %>
        <c:set var="categories" value="${requestScope.categories}" />
        <c:set var="currentSub" value="${requestScope.currentSub}" />
        <c:set var="lowestOption" value="${requestScope.lowestOption}" />
        <c:set var="currentCat" value="${requestScope.currentCat}" />
        <c:set var="currentPackage" value="${requestScope.currentPackage}" />
        <div class="container">
            <aside class="sidebar">
                    <section class="search">
                        <h2>Search course</h2>
                        <form action="CourseList" method="get">  
                            <input type="text" name="query" placeholder="Search course...">
                            <button type="submit">Search</button>
                        </form>
                    </section>

                    <section class="categories">
                        <h2>Course Category</h2>
                        <ul>
                            <li>
                                <a href="CourseList?category=${category.subjectCategoryId}">All</a>
                            </li>
                            <c:forEach items="${categories}" var="category">
                                <li>
                                    <a href="CourseList?category=${category.subjectCategoryId}">${category.title}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </section>
                </aside>
            <!-- Left Content -->
            <div class="left-content">
                <div class="title">
                    <div class="title-info">Course Detail</div>
                </div>
                <div class="center-itself", style="background: url('${currentSub.thumbnail}'); background-size: cover; height: 100px"></div>
                <div class="course-details">
                    <a href="#">
                        <h1>${currentSub.title}</h1>
                    </a>
                    <div class="categories">
                        <p><span class="info">Categories:</span></p>
                        <a href="CourseList?category=${currentSub.subjectCategoryId}" class="tag">${currentCat.title}</a>
                    </div>
                    <p><span class="info">Description:</span></p>
                    <p> ${currentSub.description}
                    </p>
                    <span class="info">Publish date: </span>
                    <span>${currentSub.updateDate}</span>
                    <p class="price">
                        <span class="info">Price:</span>
                        ${lowestOption}
                    </p>
                    <div class="register">
                        <a href="#" class="register-btn" class="title-info" id="open-popup-btn">Register Now</a>
                    </div>
                </div>
            </div>

            <!-- Right Content -->
            <div class="right-content">
                <div class="title" style="background-color: #1e77bc">
                    <div class="title-info">More Courses</div>
                </div>
                <p class="page-same">
                    <c:set var="subjectPriceMap" value="${requestScope.subjectPriceMap}" />
                <ul>
                    <c:if test="${not empty subjectPriceMap}">
                        <c:forEach var="entry" items="${subjectPriceMap}">
                            <li>
                                <div class="img-same">
                                    <a href="registerCourse?id=${entry.key.subjectId}">
                                        <div style="background: url('${entry.key.thumbnail}'); background-size: cover; height: 100px; width: 75px;"></div>
                                    </a>
                                </div>
                                <div class="des-same">
                                    <a href="registerCourse?id=${entry.key.subjectId}">
                                        <b>${entry.key.title}</b>
                                    </a>
                                </div>
                                <b>
                                    <p class="price">$${entry.value}</p>
                                </b>
                            </li>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty subjectPriceMap}">
                        <li>No subjects available.</li>
                        </c:if>
                </ul>
                </p>
            </div>

        </div>

        <div id="register-popup" class="popup-container">
            <div class="popup-content">
                
                <span class="close-btn">&times;</span>
                <h2>Register for Subject Access</h2>
                <form id="registration-form" action="payment" method="post">
                <!-- Pricing Packages -->
                <section id="pricing-packages">
                    <h3>Choose Your Package</h3>
                    <c:forEach items="${currentPackage}" var="pkg">
                        <div class="package-option">
                            <label>
                                <input type="radio" name="package" value="${pkg.packageId}" required>
                                ${pkg.name} - ${pkg.durationTime} day -
                                <c:choose>
                                    <c:when test="${pkg.salePrice < pkg.price}">
                                        <span class="original-price">${pkg.price}$</span>
                                        <span class="price">${pkg.salePrice}$</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="price">${pkg.price}$</span>
                                    </c:otherwise>
                                </c:choose>
                            </label>
                        </div>
                    </c:forEach>
                </section>

                <!-- User Information -->
                <section id="user-info">
                    <h3>Enter Your Information</h3>
                    
                        <c:choose>
                            <c:when test="${sessionScope.user != null}">
                                <!-- User is logged in, make form fields read-only -->
                                <div class="form-field">
                                    <label for="full-name">Full Name:</label>
                                    <input type="text" id="full-name" name="full-name" value="${sessionScope.user.name}" readonly>
                                </div>
                                <div class="form-field">
                                    <label for="email">Email:</label>
                                    <input type="email" id="email" name="email" value="${sessionScope.user.email}" readonly>
                                </div>
                                <div class="form-field">
                                    <label for="mobile">Mobile:</label>
                                    <input type="number" id="mobile" name="mobile" value="${sessionScope.user.phone}" readonly>
                                </div>
                                <div class="form-field">
                                    <label>Gender:</label>
                                    <label>
                                        <input type="radio" name="gender" value="male" ${sessionScope.user.gender == 'male' ? 'checked' : ''} disabled>
                                        Male
                                    </label>
                                    <label>
                                        <input type="radio" name="gender" value="female" ${sessionScope.user.gender == 'female' ? 'checked' : ''} disabled>
                                        Female
                                    </label>
                                    <label>
                                        <input type="radio" name="gender" value="other" ${sessionScope.user.gender == 'other' ? 'checked' : ''} disabled>
                                        Other
                                    </label>
                                </div>
                                <button type="button" disabled>Register</button>
                            </c:when>
                            <c:otherwise>
                                <!-- User is not logged in, form is editable -->
                                <div class="form-field">
                                    <label for="full-name">Full Name:</label>
                                    <input type="text" id="full-name" name="full-name" required>
                                </div>
                                <div class="form-field">
                                    <label for="email">Email:</label>
                                    <input type="email" id="email" name="email" required>
                                </div>
                                <div class="form-field">
                                    <label for="mobile">Mobile:</label>
                                    <input type="number" id="mobile" name="mobile" required>
                                </div>
                                <div class="form-field">
                                    <label>Gender:</label>
                                    <label>
                                        <input type="radio" name="gender" value="male" required>
                                        Male
                                    </label>
                                    <label>
                                        <input type="radio" name="gender" value="female" required>
                                        Female
                                    </label>
                                    <label>
                                        <input type="radio" name="gender" value="other" required>
                                        Other
                                    </label>
                                </div>
                                <button type="submit">Register</button>
                            </c:otherwise>
                        </c:choose>
                    </form>
                </section>

                <!-- Additional Information -->
                <section id="additional-info">
                    <a href="#">Terms and Conditions</a>
                </section>
            </div>
        </div>

    </body>
    <%@include file="Footer.jsp" %>
    <script src="js/pop-up-script.js"></script>

</html>
