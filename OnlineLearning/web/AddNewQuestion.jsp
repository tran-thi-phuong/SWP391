<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Question" %>
<%@ page import="model.QuestionMedia" %>
<html>
    <head>
        <title>Add New Question</title>
        <style>
            .form-container {
                max-width: 600px;
                margin: 0 auto;
                padding: 20px;
                border: 1px solid #ccc;
                border-radius: 10px;
                background-color: #f9f9f9;
            }
            .form-container h1 {
                text-align: center;
            }
            .form-container p {
                margin-bottom: 15px;
            }
            .form-container strong {
                display: inline-block;
                width: 150px;
            }
            .form-container input[type="text"],
            .form-container input[type="number"] {
                width: calc(100% - 160px);
                padding: 5px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }
            .form-container button {
                margin-right: 10px;
                padding: 10px 20px;
                border: none;
                border-radius: 5px;
                background-color: #007bff;
                color: white;
                cursor: pointer;
            }
            .form-container button:hover {
                background-color: #0056b3;
            }
            .media-container {
                margin-bottom: 15px;
            }
            .media-preview {
                display: flex;
                align-items: center;
                margin-top: 10px;
            }
            .media-preview img,
            .media-preview video {
                max-width: 100%;
                max-height: 100%;
                margin-right: 10px;
            }
        </style>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <c:set var="lessonDAO" value="<%=new dal.LessonDAO()%>" />
        <div class="form-container">
            <h1>Add New Question</h1>
            <form action="AddQuestion" method="post" onsubmit="return validateInput()" enctype="multipart/form-data">
                <p>
                    <strong>Content:</strong>
                    <input type="text" name="content" id="contentInput" required />
                </p>

                <p>
                    <strong>Status:</strong>
                    <select name="status" required>
                        <option value="Active">Active</option>
                        <option value="Inactive">Inactive</option>
                    </select>
                </p>

                <p>
                    <strong>Level:</strong>
                    <select name="level" required>
                        <option value="Easy">Easy</option>
                        <option value="Medium">Medium</option>
                        <option value="Hard">Hard</option>
                    </select>
                </p>

                <p>
                    <strong>Lesson:</strong>
                    <select name="lessonID" required id="lessonID">
                        <c:forEach var="lesson" items="${lessonDAO.getAllLessons()}">
                            <option value="${lesson.lessonID}">${lesson.title}</option>
                        </c:forEach>
                    </select>
                </p>

                <h2>Media Files</h2>
                <div id="mediaList">
                    <div class="media-container">
                        <input type="file" name="mediaFiles" accept="image/*,video/*" onchange="previewMedia(this)" required />
                        <input type="text" name="mediaDescription" placeholder="Media Description" required />
                        <div class="media-preview"></div>
                        <button type="button" onclick="removeMedia(this)">Remove</button>
                    </div>
                </div>
                <button type="button" onclick="addMedia()">Add Media</button>

                <div style="text-align: center; margin-top: 20px;">
                    <button type="submit" name="action" value="add">Add Question</button>
                    <button type="button" onclick="window.location.href = 'QuestionList'">Cancel</button>
                </div>
            </form>
        </div>

        <script>
            function addMedia() {
                const mediaList = document.getElementById("mediaList");
                const mediaHTML = `
                    <div class="media-container">
                        <input type="file" name="mediaFiles" accept="image/*,video/*" onchange="previewMedia(this)" required/>
                        <input type="text" name="mediaDescription" placeholder="Media Description" required/>
                        <div class="media-preview"></div>
                        <button type="button" onclick="removeMedia(this)">Remove</button>
                    </div>`;
                mediaList.insertAdjacentHTML('beforeend', mediaHTML);
            }

            function removeMedia(button) {
                const mediaContainer = button.parentElement;
                mediaContainer.remove();
            }

            function previewMedia(input) {
                const mediaPreview = input.nextElementSibling.nextElementSibling;
                mediaPreview.innerHTML = ''; // Clear previous preview
                const file = input.files[0];

                if (file) {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        const mediaElement = document.createElement(file.type.startsWith('image/') ? 'img' : 'video');
                        mediaElement.src = e.target.result;
                        mediaElement.controls = file.type.startsWith('video/');
                        mediaPreview.appendChild(mediaElement);
                    };
                    reader.readAsDataURL(file);
                }
            }

            function validateInput() {
                const input = document.getElementById('contentInput').value;
                if (input.trim() === "") {
                    alert("Input cannot be empty or whitespace.");
                    return false; // Prevent form submission
                }
                return true; // Allow form submission
            }
        </script>
    </body>
</html>
