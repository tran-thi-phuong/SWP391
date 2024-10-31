<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Test" %>
<html>
    <head>
        <title>Add New Quiz</title>
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
            .form-container input[type="number"],
            .form-container select {
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
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
    </head>
    <%@ include file="Header.jsp" %>
    <body onload="initializeFileInput()">
        <c:set var="subjectDAO" value="<%=new dal.SubjectDAO()%>" />
        <div style="text-align: center; margin-bottom: 20px;">
            <button onclick="window.location.href = 'QuizList'" style="padding: 10px 20px; border: none; border-radius: 5px; background-color: #28a745; color: white; cursor: pointer;">
                Back to Quiz List
            </button>
        </div>
        <c:choose>
            <c:when test="${empty sessionScope.user}">
                <c:redirect url="login.jsp"/>
            </c:when>
            <c:when test="${sessionScope.user.role != 'Instructor'}">
                <c:redirect url="/Homepage"/>
            </c:when>
            <c:otherwise>
                <div class="form-container">
                    <h1>Add New Quiz</h1>
                    <form action="AddQuiz" method="post" enctype="multipart/form-data">
                        <p>
                            <strong>Title:</strong>
                            <input type="text" name="title" required/>
                        </p>
                        <p>
                            <strong>Description:</strong>
                            <input type="text" name="description" required/>
                        </p>
                        <h2>Media Files</h2>
                        <div id="mediaList"></div>

                        <button type="button" onclick="addImageMedia()">Add Image</button>
                        <button type="button" onclick="addVideoMedia()">Add Video</button>

                        <p>
                            <strong>Type:</strong>
                            <select name="type" required>
                                <option value="">Select Type</option>
                                <option value="Test">Test</option>
                                <option value="Quiz">Quiz</option>
                            </select>
                        </p>

                        <p>
                            <strong>Duration:</strong>
                            <input type="number" name="duration" required/> minutes
                        </p>
                        <p>
                            <strong>Pass Condition:</strong>
                            <input type="text" name="passCondition" required/> %
                        </p>
                        <strong for="level">Level:</strong>
                        <select id="level" name="level" required>
                            <option value="">Select Level</option>
                            <option value="Beginner">Beginner</option>
                            <option value="Intermediate">Intermediate</option>
                            <option value="Advanced">Advanced</option>
                        </select>

                        <p>
                            <strong>Number of Questions:</strong>
                            <input type="number" name="quantity" value="0" readonly/>
                        </p>
                        <p>
                            <strong>Subject:</strong>
                            <select id="subjectId" name="subjectId" required>
                                <option value="">Select Subject</option>
                                <c:forEach var="subject" items="${subjectDAO.getAllSubjects()}">
                                    <option value="${subject.subjectID}">${subject.title}</option>
                                </c:forEach>
                            </select>
                        </p>
                        <button type="submit">Add Quiz</button>
                    </form>
                </div>
            </c:otherwise>
        </c:choose>
    </body>
    <script>
        function initializeFileInput() {
            const mediaType = document.getElementById('mediaType').value;
            if (mediaType) {
                setFileInputAccept(mediaType); // Set accept attribute based on current media type
                document.getElementById('mediaURLField').style.display = 'block'; // Ensure the field is visible
            } else {
                document.getElementById('mediaURLField').style.display = 'none';
            }
        }
        function addImageMedia() {
            const mediaList = document.getElementById("mediaList");
            const mediaHTML = `
        <div class="media-container">
            <input type="text" name="mediaType" value="Image"/>
            <input type="file" name="mediaFiles" accept="image/*" onchange="previewMedia(this)" required />
            <input type="text" name="mediaDescription" placeholder="Media Description" required />
            <div class="media-preview"></div>
            <button type="button" onclick="removeMedia(this)">Remove</button>
        </div>`;
            mediaList.insertAdjacentHTML('beforeend', mediaHTML);
        }
        function addVideoMedia() {
            const mediaList = document.getElementById("mediaList");
            const mediaHTML = `
        <div class="media-container">
            <input type="text" name="mediaType" value="Video"/>
            <input type="file" name="mediaFiles" accept="video/*" onchange="previewMedia(this)" required />
            <input type="text" name="mediaDescription" placeholder="Media Description" required />
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

    </script>
    <%@ include file="Footer.jsp" %>
</html>
