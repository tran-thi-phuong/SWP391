<%-- 
    Document   : blogDetail
    Created on : Sep 30, 2024, 9:53:15 PM
    Author     : sonna
--%>

<%@ page import="model.Blog" %>
<%@ page import="model.Users" %>
<%@ page import="model.BlogCategory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Blog Details</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet"> 
    </head>
    <body>
        <%@include file="Header.jsp"%>
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
            <div class="blog-detail-container">
                <%
                    Blog blog = (Blog) request.getAttribute("blog");
                    if (blog != null) {
                        Users author = blog.getUserId();
                        BlogCategory category = blog.getBlogCategoryId();
                %>

                <div class="blog-detail-header">
                    <h1><%= blog.getTitle() %></h1>
                    <p><strong>Category:</strong> <%= category.getTitle() %></p>
                    <p><strong>Author:</strong> <a href="authorInformation?username=<%= author.getUsername() %>"><%= author.getName() %></a></p>
                    <p><strong>Created At:</strong> <%= blog.getCreateAt() %></p>
                </div>

                <div class="blog-content">
                    <p><%= blog.getContent() %></p>
                </div>

                <% } else { %>
                <p>Blog post not found!</p>
                <% } %>

            </div>
            <div class="latest-new">
                <div >
                    <c:forEach var="blog" items="${latestBlogs}">
                        <a href="blogDetail?blogId=${blog.blogId}">
                            ${blog.title}
                        </a>
                        <p>Created at: ${blog.createAt}</p>
                    </c:forEach>
                </div>
            </div>

            <%@include file="Footer.jsp"%>
    </body>
</html>

