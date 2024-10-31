<%-- 
    Document   : AddBlog
    Created on : Oct 27, 2024, 11:41:23 PM
    Author     : sonna
--%>

<%@ page import="model.Users" %>
<%@ page import="dal.BlogDAO" %>
<%@ page import="model.BlogCategory" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.regex.*" %>
<html>
    <head>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <link href="css/Header_Footer.css" rel="stylesheet">
        <style>
            body {
                font-family: 'Montserrat', sans-serif;
                background-color: #f8f9fa; /* Màu nền nhẹ */
            }

            h1 {
                text-align: center;
                margin: 20px 0;
                color: #333;
            }

            form {
                max-width: 600px; /* Chiều rộng tối đa của form */
                margin: 0 auto; /* Căn giữa form */
                background-color: white; /* Màu nền trắng cho form */
                padding: 20px; /* Khoảng cách bên trong form */
                border-radius: 10px; /* Bo góc cho form */
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* Đổ bóng nhẹ cho form */
            }

            label {
                font-weight: bold;
                display: block; /* Hiển thị label trên một dòng mới */
                margin-bottom: 5px; /* Khoảng cách dưới label */
            }

            input[type="text"],
            textarea,
            select {
                width: 100%; /* Đảm bảo tất cả các trường chiếm chiều rộng tối đa */
                padding: 10px; /* Khoảng cách bên trong */
                margin-bottom: 20px; /* Khoảng cách dưới các trường */
                border: 1px solid #ced4da; /* Viền nhẹ cho các trường */
                border-radius: 5px; /* Bo góc cho các trường */
                box-sizing: border-box; /* Đảm bảo padding không làm thay đổi kích thước */
            }

            input[type="submit"] {
                background-color: #007bff; /* Màu nền cho nút */
                color: white; /* Màu chữ trên nút */
                padding: 10px 15px; /* Khoảng cách bên trong nút */
                border: none; /* Bỏ viền cho nút */
                border-radius: 5px; /* Bo góc cho nút */
                cursor: pointer; /* Hiển thị con trỏ tay khi hover */
                transition: background-color 0.3s; /* Hiệu ứng chuyển màu khi hover */
            }

            input[type="submit"]:hover {
                background-color: #0056b3; /* Màu nền khi hover */
            }

            #videoPreview {
                margin-top: 20px; /* Khoảng cách trên video preview */
                text-align: center; /* Căn giữa video preview */
            }
        </style>
        <script>
            function previewContent() {
                const content = document.getElementById('content').value;
                const previewArea = document.getElementById('previewArea');

                // Clear previous preview content
                previewArea.innerHTML = '';

                // Split the content by URLs, so we can handle both text and URLs
                const textParts = content.split(/(https?:\/\/[^\s]+)/); // Separates text and URLs

                // Process each part of the content
                textParts.forEach(part => {
                    if (part.trim()) {
                        if (part.match(/https?:\/\/[^\s]+/)) { // Check if part is a URL
                            let isMediaHandled = false;

                            // Handle image URLs
                            if (part.match(/\.(jpg|jpeg|png|gif)$/)) {
                                const img = document.createElement('img');
                                img.src = part;
                                img.className = 'img-fluid';
                                img.alt = 'Image Preview';
                                previewArea.appendChild(img);
                                isMediaHandled = true;
                            }
                            // Handle YouTube video URLs
                            else if (part.match(/https?:\/\/(www\.)?youtube\.com\/watch\?v=([a-zA-Z0-9_-]+)/)) {
                                const videoId = part.match(/(?:v=)([a-zA-Z0-9_-]+)/)[1];
                                const iframe = document.createElement('iframe');
                                iframe.src = `https://www.youtube.com/embed/${videoId}`;
                                iframe.width = '560';
                                iframe.height = '315';
                                iframe.frameBorder = '0';
                                iframe.allow = 'accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture';
                                iframe.allowFullscreen = true;
                                previewArea.appendChild(iframe);
                                isMediaHandled = true;
                            }
                            // Handle Vimeo video URLs
                            else if (part.match(/https?:\/\/(www\.)?vimeo\.com\/(\d+)/)) {
                                const videoId = part.split('/').pop();
                                const iframe = document.createElement('iframe');
                                iframe.src = `https://player.vimeo.com/video/${videoId}`;
                                iframe.width = '560';
                                iframe.height = '315';
                                iframe.frameBorder = '0';
                                iframe.allowFullscreen = true;
                                previewArea.appendChild(iframe);
                                isMediaHandled = true;
                            }
                            // Handle Dailymotion video URLs
                            else if (part.match(/https?:\/\/(www\.)?dailymotion\.com\/video\/([a-zA-Z0-9_-]+)/)) {
                                const videoId = part.split('/').pop();
                                const iframe = document.createElement('iframe');
                                iframe.src = `https://www.dailymotion.com/embed/video/${videoId}`;
                                iframe.width = '560';
                                iframe.height = '315';
                                iframe.frameBorder = '0';
                                iframe.allowFullscreen = true;
                                previewArea.appendChild(iframe);
                                isMediaHandled = true;
                            }
                            // Handle local video files (mp4, webm)
                            else if (part.match(/\.(mp4|webm)$/)) {
                                const video = document.createElement('video');
                                video.width = '560';
                                video.height = '315';
                                video.controls = true;
                                const source = document.createElement('source');
                                source.src = part;
                                source.type = part.endsWith('.mp4') ? 'video/mp4' : 'video/webm';
                                video.appendChild(source);
                                previewArea.appendChild(video);
                                isMediaHandled = true;
                            }

                            // If media wasn't handled, show an unsupported format message
                            if (!isMediaHandled) {
                                const errorMsg = document.createElement('p');
                                errorMsg.textContent = `Unsupported format or invalid media link: ${part}`;
                                errorMsg.style.color = 'red';
                                previewArea.appendChild(errorMsg);
                            }
                        } else {
                            // For regular text, add it as a paragraph, handling line breaks
                            const textNode = document.createElement('p');
                            textNode.innerHTML = part.trim().replace(/\n/g, '<br>'); // Replace newline characters with <br>
                            previewArea.appendChild(textNode);
                        }
                    }
                });
            }

        </script>
    </head>
    <body>
        <%@include file="Header.jsp"%>
        <h1>Add New Blog</h1>

        <form action="AddBlog" method="post" onsubmit="previewVideo();">
            <label for="title">Title:</label>
            <input type="text" id="title" name="title" required>

            <label for="content">Content:</label>
            <textarea id="content" name="content" required oninput="previewContent()"></textarea>

            <label>Preview:</label>
            <div id="previewArea" style="border: 1px solid #ced4da; padding: 10px; border-radius: 5px; background-color: #f9f9f9; margin-top: 10px;"></div>

            <label for="category">Category:</label>
            <select id="category" name="categoryId">
                <%-- Fill categories from the database --%>
                <%
                    BlogDAO blogDAO = new BlogDAO();
                    List<BlogCategory> categories = blogDAO.getAllCategories();
                    for (BlogCategory category : categories) {
                %>
                <option value="<%= category.getBlogCategoryId() %>"><%= category.getTitle() %></option>
                <%
                    }
                %>
            </select>

            <input type="submit" value="Add Blog">
        </form>
        <%@include file="Footer.jsp"%>
    </body>
</html>
