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
                            <li>
                                <a href="?category=${category.subjectCategoryId}">Category 1</a>
                            </li>
                            <c:forEach items="${categoriess}" var="category">
                                <li>
                                    <a href="?category=${category.subjectCategoryId}">${category.title}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </section>
                </aside>

                <main class="subject-list">
                    <c:forEach items="${subjects}" var="subject">
                        <article class="subject-item">
                            <div class="center-itself", style="background: url('${subject.thumbnail}'); background-size: cover; height: 100px"></div>
                            <h2>
                                <a href="subject?id=${subject.subjectId}">${subject.title}</a>
                            </h2>
                            <p>${subject.description}</p>
                            <a href="register?id=${subject.subjectId}" class="btn-register">Register</a>
                        </article>
                    </c:forEach>
                </main>


            </div>

            <footer>
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="?page=${currentPage - 1}">Previous Page</a>
                    </c:if>
                    <span>Page ${currentPage} / ${totalPages}</span>
                    <c:if test="${currentPage < totalPages}">
                        <a href="?page=${currentPage + 1}">Next page</a>
                    </c:if>
                </div>
            </footer>
        </div>
    </body>
</html>
