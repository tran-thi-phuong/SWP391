<%-- 
    Document   : PostDetail
    Created on : Oct 27, 2024, 10:19:40 PM
    Author     : sonna
--%>

<%@ page import="model.Blog" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.regex.*" %>

<%!
    public String formatContent(String content) {
        // Thay thế URL hình ảnh thành thẻ <img>
        content = content.replaceAll("(https?://[\\S]+\\.(jpg|jpeg|png|gif))", "<img src=\"$1\" alt=\"Image\" class=\"img-fluid\">");

        // Thay thế URL video YouTube thành thẻ <iframe>
        content = content.replaceAll("(https?://www.youtube.com/watch\\?v=([\\w-]+))", "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/$2\" frameborder=\"0\" allowfullscreen></iframe>");

        // Thay thế URL video Vimeo thành thẻ <iframe>
        content = content.replaceAll("(https?://vimeo.com/(\\d+))", "<iframe src=\"https://player.vimeo.com/video/$2\" width=\"560\" height=\"315\" frameborder=\"0\" allowfullscreen></iframe>");

        // Thay thế URL video Dailymotion thành thẻ <iframe>
        content = content.replaceAll("(https?://www.dailymotion.com/video/([\\w-]+))", "<iframe frameborder=\"0\" width=\"560\" height=\"315\" src=\"https://www.dailymotion.com/embed/video/$2\"></iframe>");

        // Thay thế URL video tự đăng tải (.mp4 hoặc .webm) thành thẻ <video>
        content = content.replaceAll("(https?://[\\S]+\\.(mp4|webm))", "<video width=\"560\" height=\"315\" controls><source src=\"$1\" type=\"video/$2\">Your browser does not support the video tag.</video>");

        return content;
    }
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết Bài viết</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <link href="css/Header_Footer.css" rel="stylesheet">

        <style>
            body {
                font-family: 'Montserrat', sans-serif;
                background-color: #f8f9fa;
            }
            h1 {
                text-align: center;
                margin-top: 20px;
                color: #343a40;
            }
            table {
                margin: 20px auto;
                width: 80%;
                background-color: #ffffff;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }
            th, td {
                text-align: left;
                padding: 15px;
            }
            th {
                background-color: #007bff;
                color: #ffffff;
            }
            td {
                background-color: #ffffff;
            }
            .no-articles {
                text-align: center;
                margin-top: 20px;
                font-size: 18px;
                color: #6c757d;
            }
        </style>
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <div style=" margin-left: 10px; margin-bottom: 20px; margin-top: 5px;">
            <a href="AddBlog" class="btn btn-primary">Add New Blog</a>
        </div>
        <c:if test="${not empty blogs}">
            <table>
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Author</th>
                        <th>Category</th>
                        <th>Create_At</th>
                        <th>Content</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="blog" items="${blogs}">
                        <tr>
                            <td>${blog.title}</td>
                            <td>${blog.userId != null ? blog.userId.name : 'N/A'}</td>
                            <td>${blog.blogCategoryId != null ? blog.blogCategoryId.title : 'N/A'}</td>
                            <td>${blog.createAt}</td>
                            <td></td> 
                            <td>
                                <input type="hidden" name="blogId" value="${blog.blogId}" class="blogId">
                                <input type="hidden" name="currentStatus" value="${blog.status}" class="currentStatus">
                                <button class="btn toggle-status btn-${blog.status == 'Show' ? 'danger' : 'success'}" 
                                        data-id="${blog.blogId}" 
                                        data-status="${blog.status}">
                                    ${blog.status == 'Show' ? 'Hide' : 'Show'}
                                </button>
                            </td>

                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty blogs}">
            <p class="no-articles">Không có bài viết nào!</p>
        </c:if>
        <script>
            $('.toggle-status').on('click', function (e) {
                e.preventDefault();
                const button = $(this);
                const blogId = button.data('id');
                const currentStatus = button.data('status'); // Lấy trạng thái hiện tại

                // Gọi AJAX để thay đổi trạng thái trên server
                $.ajax({
                    url: 'PostDetail', // URL servlet
                    method: 'POST',
                    data: {
                        blogId: blogId,
                        currentStatus: currentStatus
                    },
                    success: function (response) {
                        // Giả sử response trả về `newStatus`
                        const newStatus = response.trim(); // Trạng thái mới từ server

                        // Cập nhật `data-status`, văn bản, và lớp CSS của nút
                        button.data('status', newStatus); // Cập nhật trạng thái mới vào `data-status`
                        button
                                .removeClass(currentStatus === 'Show' ? 'btn-danger' : 'btn-success')
                                .addClass(newStatus === 'Show' ? 'btn-danger' : 'btn-success')
                                .text(newStatus === 'Show' ? 'Hide' : 'Show');
                    },
                    error: function () {
                        alert("Đã xảy ra lỗi khi cập nhật trạng thái.");
                    }
                });
            });
        </script>
        <%@include file="Footer.jsp"%>
    </body>
</html>
