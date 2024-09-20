<%-- 
    Document   : BlogList
    Created on : Sep 18, 2024, 10:18:37 AM
    Author     : sonna
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Blog List</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="BlogList.css" rel="stylesheet">
        <style>

        </style>
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <p></p>
        <div class="container">
            
            <h1 class="mb-4">Danh sách Blog</h1>

            <div class="row">
                <div class="col-md-3 sidebar">
                    <!-- Thanh tìm kiếm -->
                    <div class="search-bar">
                        <h4>Tìm kiếm</h4>
                        <form action="BlogList" method="get">
                            <input type="text" name="search" class="form-control" placeholder="Nhập từ khóa tìm kiếm">
                            <button type="submit" class="btn btn-primary mt-2">Tìm kiếm</button>
                        </form>
                    </div>

                    <!-- Các danh mục -->
                    <div class="categories">
                        <h4>Danh mục</h4>
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
                    <!-- Danh sách blog -->
                    <c:forEach var="blog" items="${blogs}">
                        <div class="blog-post">
                            <!-- Hiển thị tiêu đề, có link dẫn đến trang chi tiết -->
                            <h2><a href="PostDetailServlet?blogId=${blog.blogId}">${blog.title}</a></h2>

                            <!-- Hiển thị thông tin người đăng và ngày đăng -->
                            <div class="blog-info">
                                Đăng bởi ${blog.userId.name} vào ngày ${blog.createAt}
                            </div>
                        </div>
                    </c:forEach>

                    <!-- Phân trang -->
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
                                    <a class="page-link" href="blogs?page=${i}">${i}</a>
                                </li>
                            </c:forEach>
                            <c:if test="${currentPage < totalPages}">
                                <li class="page-item">
                                    <a class="page-link" href="blogs?page=${currentPage + 1}" aria-label="Next">
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
