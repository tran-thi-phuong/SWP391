<%-- 
    Document   : EditBlog
    Created on : Oct 31, 2024, 11:00:40 AM
    Author     : sonna
--%>

<%@ page import="model.Blog" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Edit Blog</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                font-family: 'Montserrat', sans-serif;
                background-color: #f8f9fa; /* Màu nền nhẹ */
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
            textarea {
                width: 100%;
                height: 300px; /* Tăng chiều cao */
                padding: 10px;
                margin-bottom: 20px;
                border: 1px solid #ced4da;
                border-radius: 5px;
                box-sizing: border-box;
            }
            #previewArea {
                border: 1px solid #ced4da;
                padding: 10px;
                border-radius: 5px;
                background-color: #f9f9f9;
                margin-top: 10px;
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
                            // Handle other media as needed...

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
        <form action="EditBlog" method="post">
            <input type="hidden" name="blogId" value="${blog.blogId}">

            <label>Title:</label>
            <input type="text" name="title" value="${blog.title}" required>

            <label>Content:</label>
            <textarea id="content" name="content" oninput="previewContent()" required>${blog.content}</textarea>

            <label>Preview:</label>
            <div id="previewArea"></div>

            <label>Category:</label>
            <select name="categoryId">
                <c:forEach var="category" items="${categories}">
                    <option value="${category.blogCategoryId}" ${category.blogCategoryId == blog.blogCategoryId.blogCategoryId ? 'selected' : ''}>
                        ${category.title}
                    </option>
                </c:forEach>
            </select>

            <button type="submit">Save Changes</button>
        </form>
    </body>
</html>
