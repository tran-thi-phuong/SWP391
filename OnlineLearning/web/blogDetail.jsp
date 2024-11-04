<%-- 
    Document   : blogDetail
    Created on : Sep 30, 2024, 9:53:15 PM
    Author     : sonna
--%>

<%-- 
    Document   : blogDetail
    Created on : Sep 30, 2024, 9:53:15 PM
    Author     : sonna
--%>

<%@ page import="model.Blog" %>
<%@ page import="model.Users" %>
<%@ page import="model.BlogCategory" %>
<%@ page import="dal.BlogDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.regex.*" %>

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
        <style>
            .sidebar {
                background-color: #f8f9fa;
                padding: 15px;
                margin-top: 15px;
            }
            .blog-detail-container {
                padding: 20px;
                background-color: #fff;
                margin-top: 15px;
            }
            .latest-new {
                padding: 15px;
                background-color: #f8f9fa;
                margin-top: 15px;
            }
        </style>
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <div class="container">
            <div class="row">
                <!-- Blog Detail (Content Center) -->
                <div class="col-md-8 blog-detail-container">
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
                        <%= formatContent(blog.getContent()) %>
                    </div>

                    <% } else { %>
                    <p>Blog post not found!</p>
                    <% } %>
                </div>

                <div class="col-md-4 latest-new">
                    <h4>Bài viết mới nhất</h4>
                    <c:forEach var="latestBlog" items="${latestBlogs}">
                        <div class="latest-blog-item mb-3">
                            <a href="blogDetail?blogId=${latestBlog.blogId}">
                                <strong>${latestBlog.title}</strong>
                            </a>
                            <p>Created at: ${latestBlog.createAt}</p>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

        <%!
     public String formatContent(String content) {
         // Replace image URLs with <img> tags
         content = content.replaceAll("(https?://[\\S]+\\.(jpg|jpeg|png|gif))", "<img src=\"$1\" alt=\"Image\" class=\"img-fluid\">");

         // Replace YouTube video URLs with <iframe> tags
         content = content.replaceAll("(https?://www.youtube.com/watch\\?v=([\\w-]+))", "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/$2\" frameborder=\"0\" allowfullscreen></iframe>");

         // Replace Vimeo video URLs with <iframe> tags
         content = content.replaceAll("(https?://vimeo.com/(\\d+))", "<iframe src=\"https://player.vimeo.com/video/$2\" width=\"560\" height=\"315\" frameborder=\"0\" allowfullscreen></iframe>");

         // Replace Dailymotion video URLs with <iframe> tags
         content = content.replaceAll("(https?://www.dailymotion.com/video/([\\w-]+))", "<iframe frameborder=\"0\" width=\"560\" height=\"315\" src=\"https://www.dailymotion.com/embed/video/$2\"></iframe>");

         // Replace local video URLs with <video> tags
         content = content.replaceAll("(https?://[\\S]+\\.(mp4|webm))", "<video width=\"560\" height=\"315\" controls><source src=\"$1\" type=\"video/$2\">Your browser does not support the video tag.</video>");

         // Preserve line breaks
         content = content.replaceAll("\n", "<br>");

         return content;
     }
        %>

        <%@include file="Footer.jsp"%>
    </body>
</html>
