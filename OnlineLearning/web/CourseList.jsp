<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Course List</title>
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>

        <div class="container">
            <header>
                <h1>Subject List </h1>
            </header>

            <div class="content">
                <aside class="sidebar">
                    <section class="search">
                        <h2>Search Subject</h2>
                        <form action="CourseList" method="get">  
                            <input type="text" name="query" placeholder="Search subject...">
                            <button type="submit">Search</button>
                        </form>
                    </section>

                    <section class="categories">
                        <h2>Subject Category</h2>
                        <ul>
                            <li>
                                <a href="?category=${category.subjectCategoryId}">All</a>
                            </li>
                            <c:forEach items="${categories}" var="category">
                                <li>
                                    <a href="?category=${category.subjectCategoryId}">${category.title}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </section>
                </aside>

                <main class="subject-list">
                    <c:choose>
                        <c:when test="${not empty subjects}">
                            <c:forEach items="${subjects}" var="subject">
                                <article class="subject-item">
                                    <div class="center-itself" style="background: url('${subject.thumbnail}'); background-size: cover; height: 100px"></div>
                                    <h2>
                                        <a href="register?id=${subject.subjectId}" style="text-decoration: none; color: black">${subject.title}</a>
                                    </h2>
                                    <p>${subject.description}</p>
                                    <a href="register?id=${subject.subjectId}" class="btn-register">Register</a>
                                </article>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <p>No subjects found.</p>
                        </c:otherwise>
                    </c:choose>
                </main>


            </div>

            <footer>
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="?page=${currentPage - 1}" class="arrow">&laquo;</a>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <c:choose>
                            <c:when test="${currentPage eq i}">
                                <span class="active">${i}</span>
                            </c:when>
                            <c:otherwise>
                                <a href="?page=${i}">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="?page=${currentPage + 1}" class="arrow">&raquo;</a>
                    </c:if>
                </div>
            </footer>
        </div>
    </body>
</html>
