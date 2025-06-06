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
        <link href="css/Header_Footer.css" rel="stylesheet"> 
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
        <h1>Add New Course</h1>


        <form action="newSubject" method="POST" enctype="multipart/form-data">
            <a href="SubjectList" class="back-button">Back to course list</a>

                    <%-- Course Name Input --%>
                    <label for="courseName">Course Name:</label>
                    <input type="text" id="courseName" name="courseName" required>

                    <%-- Thumbnail Upload Field 
                         Accepts image files for course thumbnail
                    --%>
                    <label for="thumbnail">Thumbnail Image:</label>
                    <input type="file" id="thumbnail" name="thumbnail" required>

                    <%-- Category Dropdown
                         Populated dynamically from database
                    --%>
                    <label for="category">Category:</label>
                    <select id="category" name="category" required>
                        <c:forEach items="${categories}" var="category">
                            <option value="${category.subjectCategoryId}">
                                ${category.title}
                            </option>
                        </c:forEach>
                    </select>

                    <%-- Instructor Dropdown
                         Populated dynamically from database
                    --%>
                    <label for="instructor">Instructor:</label>
                    <select id="instructor" name="instructor" required>
                        <c:forEach items="${Instructor}" var="instructors">
                            <option value="${instructors.userID}">
                                ${instructors.name}
                            </option>
                        </c:forEach>
                    </select>
                    
                    <%-- Status Selection
                         Controls course visibility and availability
                    --%>
                    <label for="status">Status:</label>
                    <select id="status" name="status" required>
                        <option value="">Select status</option>
                        <option value="Active">Active</option>
                        <option value="Inactive">Inactive</option>
                        <%-- Additional status options can be added here --%>
                    </select>

                    <%-- Course Description Text Area
                         Multi-line text input for detailed course information
                    --%>
                    <label for="description">Description:</label>
                    <textarea id="description" name="description" rows="4" required></textarea>

            <button type="submit">Add Course</button>
            
        </form>

    </body>
    
</html>
