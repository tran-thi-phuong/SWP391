<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Course List</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet"> 
        <link href="css/style.css" rel="stylesheet" >
    </head>
    <body>
        <%@include file="Header.jsp" %>

        <!-- Main container for all content -->
        <div class="container">
            <div class="content">
                <!-- Left sidebar containing search and categories -->
                <aside class="sidebar">
                    <!-- Search form -->
                    <section class="search">
                        <h2>Search Course</h2>
                        <form action="CourseList" method="get">
                            <input type="text" name="query" value="${query}" placeholder="Search course...">
                            <c:if test="${not empty categoryId}">
                                <input type="hidden" name="category" value="${categoryId}">
                            </c:if>
                            <button type="submit">Search</button>
                        </form>
                    </section>

                    <!-- Categories section -->
                    <section class="categories">
                        <h2>Course Category</h2>
                        <ul>
                            <li>
                                <a href="CourseList">All</a>
                            </li>
                            <c:forEach items="${categories}" var="category">
                                <li>
                                    <a href="CourseList?category=${category.subjectCategoryId}&query=${query}"
                                       class="${category.subjectCategoryId eq categoryId ? 'active' : ''}">${category.title}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </section>
                </aside>

                <!-- Main content area displaying subject list -->
                <main class="subject-list">
                    <%-- Check if subjects exist --%>
                    <c:choose>
                        <c:when test="${not empty subjects}">
                            <c:forEach items="${subjects}" var="subject">
                                <article class="subject-item">
                                    <div class="center-itself" style="background: url('${subject.thumbnail}'); background-size: cover; height: 220px"></div>
                                    <h2>
                                        <a href="registerCourse?id=${subject.subjectID}" style="text-decoration: none; color: black">${subject.title}</a>
                                    </h2>
                                    <p>${subject.description}</p>
                                   <h6 style="color: #FF8C00;">${userCounts[subject.subjectID]} student are studying this course.</h6>
                                   
                                    <a href="registerCourse?id=${subject.subjectID}" class="btn-register">Register Now</a>
                                </article>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <p>No course found.</p>
                        </c:otherwise>
                    </c:choose>
                </main>
            </div>

            <!-- Footer containing pagination -->
            <footer>
                <!-- Pagination section -->
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="?page=${currentPage - 1}&query=${query}&category=${categoryId}" class="arrow">&laquo;</a>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <c:choose>
                            <c:when test="${currentPage eq i}">
                                <span class="active">${i}</span>
                            </c:when>
                            <c:otherwise>
                                <a href="?page=${i}&query=${query}&category=${categoryId}">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="?page=${currentPage + 1}&query=${query}&category=${categoryId}" class="arrow">&raquo;</a>
                    </c:if>
                </div>
            </footer>
        </div>
    </body>
    <%@include file="Footer.jsp" %>
</html>
