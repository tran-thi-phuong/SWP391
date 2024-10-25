<%-- 
    Document   : newSubject
    Created on : Oct 7, 2024, 4:50:51 PM
    Author     : Admin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
    
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add New Course</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
                padding: 20px;
                background-color: #f8f8f8;
            }
            h1 {
                text-align: center;
            }
            form {
                max-width: 600px;
                margin: auto;
                background: #fff;
                padding: 20px;
                border-radius: 5px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }
            label {
                display: block;
                margin-bottom: 8px;
            }
            input[type="text"],
            input[type="file"],
            select,
            textarea {
                width: 100%;
                padding: 10px;
                margin-bottom: 15px;
                border: 1px solid #ccc;
                border-radius: 4px;
            }
            input[type="checkbox"] {
                margin-right: 10px;
            }
            button {
                background-color: #4CAF50;
                color: white;
                padding: 10px 15px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                width: 100%;
            }
            button:hover {
                background-color: #45a049;
            }
            .back-button {
                float: right;
                padding: 10px 15px;
                text-decoration: none;
                border-radius: 5px; 
            }
        </style>
    </head>
    <body>

        <h1>Add New Subject</h1>
         <c:choose>
            <c:when test="${empty sessionScope.user}">
                <c:redirect url="login.jsp"/>
            </c:when>
            <c:when test="${sessionScope.user.role != 'Instructor'}">
                <c:redirect url="/Homepage"/>
            </c:when>
            <c:otherwise>


        <form action="newSubject" method="POST" enctype="multipart/form-data">
            <a href="SubjectList" class="back-button">Back to subject list</a>

            <label for="courseName">Course Name:</label>
            <input type="text" id="courseName" name="courseName" required>

            <label for="thumbnail">Thumbnail Image:</label>
            <input type="file" id="thumbnail" name="thumbnail" required>

            <label for="category">Category:</label>
            <select id="category" name="category" required>
                <c:forEach items="${categories}" var="category">
                    <option value="${category.subjectCategoryId}">${category.title}</option>
                </c:forEach>
            </select>

            <label for="status">Status:</label>
            <select id="status" name="status" required>
                <option value="">Select status</option>
                <option value="active">Active</option>
                <option value="inactive">Inactive</option>
                <!-- Add more statuses as needed -->
            </select>

            <label for="description">Description:</label>
            <textarea id="description" name="description" rows="4" required></textarea>

            <button type="submit">Add Course</button>
            
        </form>
            </c:otherwise>
         </c:choose>

    </body>
</html>
