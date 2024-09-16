<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Course List</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Course List</h1>
        </header>
        
        <div class="content">
            <aside class="sidebar">
                <section class="search">
                    <h2>Search course</h2>
                    <form action="search" method="get">
                        <input type="text" name="query" placeholder="...">
                        <button type="submit">Search</button>
                    </form>
                </section>
                
                <section class="categories">
                    <h2>Course Category</h2>
                    <ul>
                        <c:forEach items="${categories}" var="category">
                            <li><a href="?category=${category.id}">${category.name}</a></li>
                        </c:forEach>
                    </ul>
                </section>
                
                <section class="featured">
                    <h2>Featured Subject</h2>
                    <ul>
                        <c:forEach items="${featuredSubjects}" var="subject">
                            <li>
                                <a href="subject?id=${subject.id}">
                                    <img src="${subject.thumbnail}" alt="${subject.title}">
                                    <h3>${subject.title}</h3>
                                    <p>${subject.tagline}</p>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </section>
            </aside>
            
            <main class="subject-list">
                <c:forEach items="${subjects}" var="subject">
                    <article class="subject-item">
                        <img src="${subject.thumbnail}" alt="${subject.title}">
                        <h2><a href="subject?id=${subject.id}">${subject.title}</a></h2>
                        <p>${subject.tagline}</p>
                        <div class="price">
                            <span class="list-price">${subject.listPrice}</span>
                            <span class="sale-price">${subject.salePrice}</span>
                        </div>
                        <a href="register?id=${subject.id}" class="btn-register">Register</a>
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