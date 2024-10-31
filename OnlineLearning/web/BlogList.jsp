<%-- 
    Document   : BlogList
    Created on : Sep 18, 2024, 10:18:37 AM
    Author     : sonna
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Users" %>
<%@ page import="dal.RolePermissionDAO" %>
<%@ page import="dal.PagesDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.*" %>

<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Blog List</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <link href="BlogList.css" rel="stylesheet">
        <style>

        </style>
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <p></p>
        <div class="container">
            
            <h1 class="mb-4">Blogs</h1>

            <div class="row">
                <div class="col-md-3 sidebar">
                    <!-- Thanh tìm kiếm -->
                    <div class="search-bar">
                        <h4>Search </h4>
                        <form action="blogList" method="get">
                            <input type="text" name="search" class="form-control" placeholder="Enter word to search">
                            <button type="submit" class="btn btn-primary mt-2">Save</button>
                        </form>
                    </div>

                    <div class="categories">
                        <h4>Category</h4>
                        <ul class="list-group">
                            <c:forEach var="category" items="${categories}">
                                <li class="list-group-item">
                                    <a href="BlogList?categoryId=${category.blogCategoryId}">${category.title}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>

                <div class="col-md-9">
                    <c:forEach var="blog" items="${blogs}">
                        <div class="blog-post">
                            <h2><a href="blogDetail?blogId=${blog.blogId}">${blog.title}</a></h2>

                            <div class="blog-info">
                                Post by ${blog.userId.name} at ${blog.createAt}
                            </div>
                        </div>
                    </c:forEach>

                    <nav aria-label="Page navigation">
                        <ul class="pagination">
                            <c:if test="${currentPage > 1}">
                                <li class="page-item">
                                    <a class="page-link" href="blogs?page=${currentPage - 1}" aria-label="Previous">
                                        <span aria-hidden="true">&laquo;</span>
                                    </a>
                                </li>
                            </c:if>
                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <li class="page-item ${i == currentPage ? 'active' : ''}">
                                    <a class="page-link" href="blogList?page=${i}">${i}</a>
                                </li>
                            </c:forEach>
                            <c:if test="${currentPage < totalPages}">
                                <li class="page-item">
                                    <a class="page-link" href="blogList?page=${currentPage + 1}" aria-label="Next">
                                        <span aria-hidden="true">&raquo;</span>
                                    </a>
                                </li>
                            </c:if>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
        <br>
        <%@include file="Footer.jsp" %>
    </body>
</html>
